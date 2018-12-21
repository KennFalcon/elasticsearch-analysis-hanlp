package com.hankcs.dic;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.corpus.io.IOUtil;
import com.hankcs.hanlp.corpus.tag.Nature;
import com.hankcs.hanlp.dictionary.CustomDictionary;
import com.hankcs.hanlp.dictionary.other.CharTable;
import com.hankcs.hanlp.dictionary.stopword.CoreStopWordDictionary;
import com.hankcs.hanlp.utility.LexiconUtility;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.SpecialPermission;
import org.elasticsearch.common.collect.Tuple;
import org.elasticsearch.common.logging.Loggers;
import org.elasticsearch.core.internal.io.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.AccessController;
import java.security.PrivilegedAction;

/**
 * @project: elasticsearch-analysis-hanlp
 * @description: 自定义远程词典监控线程
 * @author: Kenn
 * @create: 2018-12-14 15:10
 */
public class RemoteMonitor implements Runnable {

    private static final Logger logger = Loggers.getLogger(RemoteMonitor.class);

    private static CloseableHttpClient httpclient = HttpClients.createDefault();
    /**
     * 上次更改时间
     */
    private String last_modified;
    /**
     * 资源属性
     */
    private String eTags;
    /**
     * 请求地址
     */
    private String location;
    /**
     * 数据类型
     */
    private String type;

    private static final String SPLITTER = "\\s";

    public RemoteMonitor(String location, String type) {
        this.location = location;
        this.type = type;
        this.last_modified = null;
        this.eTags = null;
    }

    @Override
    public void run() {
        SpecialPermission.check();
        AccessController.doPrivileged((PrivilegedAction<Void>) () -> {
            runUnprivileged();
            return null;
        });
    }

    /**
     * 监控流程：
     * ①向词库服务器发送Head请求
     * ②从响应中获取Last-Modify、ETags字段值，判断是否变化
     * ③如果未变化，休眠1min，返回第①步
     * ④如果有变化，重新加载词典
     * ⑤休眠1min，返回第①步
     */

    private void runUnprivileged() {
        String path = location.split(SPLITTER)[0];

        HttpHead head = new HttpHead(path);
        head.setConfig(buildRequestConfig());

        // 设置请求头
        if (last_modified != null) {
            head.setHeader("If-Modified-Since", last_modified);
        }
        if (eTags != null) {
            head.setHeader("If-None-Match", eTags);
        }

        CloseableHttpResponse response = null;
        try {
            response = httpclient.execute(head);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                if ((response.getLastHeader("Last-Modified") != null) && !response.getLastHeader("Last-Modified").getValue().equalsIgnoreCase(last_modified)) {
                    loadRemoteCustomWords(response);
                } else if ((response.getLastHeader("ETag") != null) && !response.getLastHeader("ETag").getValue().equalsIgnoreCase(eTags)) {
                    loadRemoteCustomWords(response);
                }
            } else if (response.getStatusLine().getStatusCode() == HttpStatus.SC_NOT_MODIFIED) {
                logger.info("remote_ext_dict {} is without modified", location);
            } else {
                logger.info("remote_ext_dict {} return bad code {}", location, response.getStatusLine().getStatusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("remote_ext_dict {} error!", e, location);
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

    /**
     * 加载远程自定义词典
     *
     * @param response header响应
     */
    private void loadRemoteCustomWords(CloseableHttpResponse response) {
        switch (type) {
            case "custom":
                logger.info("load hanlp remote custom dict path: {}", location);
                loadRemoteWordsUnprivileged(location);
                logger.info("finish load hanlp remote custom dict path: {}", location);
                break;
            case "stop":
                logger.info("load hanlp remote stop words path: {}", location);
                loadRemoteStopWordsUnprivileged(location);
                logger.info("finish load hanlp remote stop words path: {}", location);
                break;
            default:
                return;
        }
        last_modified = response.getLastHeader("Last-Modified") == null ? null : response.getLastHeader("Last-Modified").getValue();
        eTags = response.getLastHeader("ETag") == null ? null : response.getLastHeader("ETag").getValue();
    }

    /**
     * 从远程服务器上下载自定义词条
     *
     * @param location 配置条目
     */
    private void loadRemoteWordsUnprivileged(String location) {
        Tuple<String, Nature> defaultInfo = analysisDefaultInfo(location);
        CloseableHttpClient httpclient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        BufferedReader in = null;
        HttpGet get = new HttpGet(defaultInfo.v1());
        get.setConfig(buildRequestConfig());
        try {
            response = httpclient.execute(get);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                in = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), analysisDefaultCharset(response)));
                String line;
                boolean firstLine = true;
                while ((line = in.readLine()) != null) {
                    if (firstLine) {
                        line = IOUtil.removeUTF8BOM(line);
                        firstLine = false;
                    }

                    // 切分
                    String[] param = line.split(SPLITTER);
                    String word = param[0];

                    // 排除空行
                    if (word.length() == 0) {
                        continue;
                    }

                    // 正规化
                    if (HanLP.Config.Normalization) {
                        word = CharTable.convert(word);
                    }
                    logger.info("hanlp remote custom word: {}", word);
                    CustomDictionary.insert(word, analysisNatureWithFrequency(defaultInfo.v2(), param));
                }
                in.close();
                response.close();
            }
            response.close();
        } catch (IllegalStateException | IOException e) {
            logger.error("get remote words {} error", e, location);
        } finally {
            try {
                IOUtils.close(in);
                IOUtils.close(response);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 从远程服务器上下载停止词词条
     *
     * @param location 配置条目
     */
    private void loadRemoteStopWordsUnprivileged(String location) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        BufferedReader in = null;
        HttpGet get = new HttpGet(location);
        get.setConfig(buildRequestConfig());
        try {
            response = httpclient.execute(get);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                in = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), analysisDefaultCharset(response)));
                String line;
                boolean firstLine = true;
                while ((line = in.readLine()) != null) {
                    if (firstLine) {
                        line = IOUtil.removeUTF8BOM(line);
                        firstLine = false;
                    }
                    CoreStopWordDictionary.add(line);
                }
                in.close();
                response.close();
            }
            response.close();
        } catch (IllegalStateException | IOException e) {
            logger.error("get remote words {} error", e, location);
        } finally {
            try {
                IOUtils.close(in);
                IOUtils.close(response);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private RequestConfig buildRequestConfig() {
        return RequestConfig.custom()
                .setConnectionRequestTimeout(10 * 1000)
                .setConnectTimeout(10 * 1000)
                .setSocketTimeout(60 * 1000)
                .build();
    }

    /**
     * 分析默认编码
     *
     * @param response 响应
     * @return 返回编码
     */
    private Charset analysisDefaultCharset(CloseableHttpResponse response) {
        Charset charset = StandardCharsets.UTF_8;
        // 获取编码，默认为utf-8
        if (response.getEntity().getContentType().getValue().contains("charset=")) {
            String contentType = response.getEntity().getContentType().getValue();
            charset = Charset.forName(contentType.substring(contentType.lastIndexOf("=") + 1));
        }
        return charset;
    }

    /**
     * 解析默认信息
     *
     * @param location 配置路径
     * @return 返回new Tuple<路径, 默认词性>
     */
    private Tuple<String, Nature> analysisDefaultInfo(String location) {
        Nature defaultNature = Nature.n;
        String path = location;
        int cut = location.indexOf(' ');
        if (cut > 0) {
            // 有默认词性
            String nature = location.substring(cut + 1);
            path = location.substring(0, cut);
            defaultNature = LexiconUtility.convertStringToNature(nature);
        }
        return Tuple.tuple(path, defaultNature);
    }

    /**
     * 分析词性和频次
     *
     * @param defaultNature 默认词性
     * @param param         行数据
     * @return 返回[单词] [词性A] [A的频次] [词性B] [B的频次] ...
     */
    private String analysisNatureWithFrequency(Nature defaultNature, String[] param) {
        int natureCount = (param.length - 1) / 2;
        StringBuilder builder = new StringBuilder();
        if (natureCount == 0) {
            builder.append(defaultNature).append(" ").append(1000);
        } else {
            for (int i = 0; i < natureCount; ++i) {
                Nature nature = LexiconUtility.convertStringToNature(param[1 + 2 * i]);
                int frequency = Integer.parseInt(param[2 + 2 * i]);
                builder.append(nature).append(" ").append(frequency);
                if (i != natureCount - 1) {
                    builder.append(" ");
                }
            }
        }
        return builder.toString();
    }
}

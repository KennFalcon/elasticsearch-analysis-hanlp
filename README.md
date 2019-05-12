# elasticsearch-analysis-hanlp
HanLP Analyzer for ElasticSearch

[![Codacy Badge](https://api.codacy.com/project/badge/Grade/dbe4103dc2da4b6b89b5562aacaa3c3f)](https://app.codacy.com/app/kennfalcon/elasticsearch-analysis-hanlp?utm_source=github.com&utm_medium=referral&utm_content=KennFalcon/elasticsearch-analysis-hanlp&utm_campaign=Badge_Grade_Settings)
[![Build Status](https://travis-ci.com/KennFalcon/elasticsearch-analysis-hanlp.svg?branch=master)](https://travis-ci.com/KennFalcon/elasticsearch-analysis-hanlp)
[![GitHub release](https://img.shields.io/github/release/KennFalcon/elasticsearch-analysis-hanlp.svg)](https://github.com/KennFalcon/elasticsearch-analysis-hanlp/releases)
[![License](https://img.shields.io/badge/license-Apache%202-4EB1BA.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)

此分词器基于[HanLP](http://www.hankcs.com/nlp)，提供了HanLP中大部分的分词方式。

🚩适配Elasticsearch 7.0.0版本

----------

版本对应
----------

| Plugin version | Elastic version |
| :------------- | :-------------- |
| master         | 7.x             |
| 7.0.0          | 7.0.0           |
| 6.7.1          | 6.7.1           |
| 6.7.0          | 6.7.0           | 
| 6.6.2          | 6.6.2           |
| 6.6.1          | 6.6.1           |
| 6.6.0          | 6.6.0           |
| 6.5.4          | 6.5.4           |
| 6.5.3          | 6.5.3           |
| 6.5.2          | 6.5.2           |
| 6.5.1          | 6.5.1           |
| 6.5.0          | 6.5.0           |
| 6.5.0          | 6.5.0           |
| 6.4.3          | 6.4.3           |
| 6.4.2          | 6.4.2           |
| 6.4.1          | 6.4.1           |
| 6.4.0          | 6.4.0           |
| 6.3.2          | 6.3.2           |
| 6.3.1          | 6.3.1           |
| 6.2.2          | 6.2.2           |
| 5.2.2          | 5.2.2           |

安装步骤
----------

### 1. 下载安装ES对应Plugin Release版本

安装方式：

方式一

   a. 下载对应的release安装包，最新release包可从baidu盘下载（链接:https://pan.baidu.com/s/1mFPNJXgiTPzZeqEjH_zifw  密码:i0o7）
   
   b. 将相关内容解压至*ES_HOME*/plugins/analysis-hanlp
   
   c. 将config目录下的文件移动至*ES_HOME*/config/analysis-hanlp
   
   d. 解压出的data目录为词典目录
   
方式二

   a. 使用elasticsearch插件脚本安装command如下：
   
   `./bin/elasticsearch-plugin install https://github.com/KennFalcon/elasticsearch-analysis-hanlp/releases/download/v6.5.4/elasticsearch-analysis-hanlp-6.5.4.zip`

### 2. 安装数据包

release包中存放的为HanLP源码中默认的分词数据，若要下载完整版数据包，请查看[HanLP Release](https://github.com/hankcs/HanLP/releases)。

数据包目录：*ES_HOME*/analysis-hanlp

**注：因原版数据包自定义词典部分文件名为中文，这里的hanlp.properties中已修改为英文，请对应修改文件名**

### 3. 重启Elasticsearch

**注：上述说明中的ES_HOME为自己的ES安装路径，需要绝对路径**

### 4. 热更新

在本版本中，增加了词典热更新，修改步骤如下：

a. 在*ES_HOME*/analysis-hanlp/data/dictionary/custom目录中新增自定义词典

b. 修改hanlp.properties，修改CustomDictionaryPath，增加自定义词典配置

c. 等待1分钟后，词典自动加载

**注：每个节点都需要做上述更改**

提供的分词方式说明
----------

hanlp: hanlp默认分词

hanlp_standard: 标准分词

hanlp_index: 索引分词

hanlp_nlp: NLP分词

hanlp_n_short: N-最短路分词

hanlp_dijkstra: 最短路分词

hanlp_crf: CRF分词（在hanlp 1.6.6已开始废弃）

hanlp_speed: 极速词典分词

样例
----------

```text
POST http://localhost:9200/twitter2/_analyze
{
  "text": "美国阿拉斯加州发生8.0级地震",
  "tokenizer": "hanlp"
}
```

```json
{
  "tokens" : [
    {
      "token" : "美国",
      "start_offset" : 0,
      "end_offset" : 2,
      "type" : "nsf",
      "position" : 0
    },
    {
      "token" : "阿拉斯加州",
      "start_offset" : 0,
      "end_offset" : 5,
      "type" : "nsf",
      "position" : 1
    },
    {
      "token" : "发生",
      "start_offset" : 0,
      "end_offset" : 2,
      "type" : "v",
      "position" : 2
    },
    {
      "token" : "8.0",
      "start_offset" : 0,
      "end_offset" : 3,
      "type" : "m",
      "position" : 3
    },
    {
      "token" : "级",
      "start_offset" : 0,
      "end_offset" : 1,
      "type" : "q",
      "position" : 4
    },
    {
      "token" : "地震",
      "start_offset" : 0,
      "end_offset" : 2,
      "type" : "n",
      "position" : 5
    }
  ]
}
```

远程词典配置
----------

配置文件为*ES_HOME*/config/analysis-hanlp/hanlp-remote.xml

```xml
<properties>
    <comment>HanLP Analyzer 扩展配置</comment>

    <!--用户可以在这里配置远程扩展字典 -->
    <entry key="remote_ext_dict">words_location</entry>

    <!--用户可以在这里配置远程扩展停止词字典-->
    <entry key="remote_ext_stopwords">stop_words_location</entry>
</properties>
```

### 1. 远程扩展字典

其中words_location为URL或者URL+" "+词性，如：

    1. http://localhost:8080/mydic
    
    2. http://localhost:8080/mydic nt

第一个样例，是直接配置URL，词典内部每一行代表一个单词，格式遵从[单词] [词性A] [A的频次] [词性B] [B的频次] ... 如果不填词性则表示采用词典的默认词性n。

第二个样例，配置词典URL，同时配置该词典的默认词性nt，当然词典内部同样遵循[单词] [词性A] [A的频次] [词性B] [B的频次] ... 如果不配置词性，则采用默认词性nt。

### 2. 远程扩展停止词字典

其中stop_words_location为URL，如：

    1. http://localhost:8080/mystopdic

样例直接配置URL，词典内部每一行代表一个单词，不需要配置词性和频次，换行符用 \n 即可。


**注意，所有的词典URL是需要满足条件即可完成分词热更新：**

- 该 http 请求需要返回两个头部(header)，一个是 Last-Modified，一个是 ETag，这两者都是字符串类型，只要有一个发生变化，该插件就会去抓取新的分词进而更新词库。

- 可以配置多个字典路径，中间用英文分号;间隔

- URL每隔1分钟访问一次

- 保证词典编码UTF-8

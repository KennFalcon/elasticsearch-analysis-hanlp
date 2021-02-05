# elasticsearch-analysis-hanlp
HanLP Analyzer for ElasticSearch

[![Codacy Badge](https://api.codacy.com/project/badge/Grade/dbe4103dc2da4b6b89b5562aacaa3c3f)](https://app.codacy.com/app/kennfalcon/elasticsearch-analysis-hanlp?utm_source=github.com&utm_medium=referral&utm_content=KennFalcon/elasticsearch-analysis-hanlp&utm_campaign=Badge_Grade_Settings)
[![Build Status](https://travis-ci.com/KennFalcon/elasticsearch-analysis-hanlp.svg?branch=master)](https://travis-ci.com/KennFalcon/elasticsearch-analysis-hanlp)
[![GitHub release](https://img.shields.io/github/release/KennFalcon/elasticsearch-analysis-hanlp.svg)](https://github.com/KennFalcon/elasticsearch-analysis-hanlp/releases)
[![License](https://img.shields.io/badge/license-Apache%202-4EB1BA.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)

此分词器基于[HanLP](http://www.hankcs.com/nlp)，提供了HanLP中大部分的分词方式。

今年一年都在忙其他事，所以插件更新基本一年没有更新，年底更新一波。今年学习了一下ES向量插件（亚马逊设计的那个方式还挺有意思的，重新设计向量索引文件，但文件管理依托于lucene），搞了搞知识图谱（JanusGraph），最近又在弄ES存储计算分离（基于共享存储）

🚩 更新日志：
1. 适配Elasticsearch 7.5.1~7.10.2版本，更新HanLP版本至1.7.8，更新日志这次就不在每个release上加了，直接看README（7.5.1重新打包订正）(陆续上传中)
2. 修改分词流程，完全采用hankcs提供的hanlp-lucene-plugin进行，详见方法com.hankcs.lucene.
   SegmentWrapper#next，该类部分代码格式虽然不太优雅，但为了保证和源码基本一致性，代码格式校验做了剔除该文件，请各位改动该文件时，尽量不对该文件进行格式改动
3. 修改模型引用方式，模型使用了简单的单例方式引用，防止重复加载，内存溢出
4. 修复自定义停用词词典未加载问题
5. 分词过程中默认会将空白字符剔除，如果有需要空白字符的场景，请自行更改源码重新编译打包
6. 优化部分代码结构，修复部分代码逻辑错误问题
7. 工程改为gradle方式部署，重新修改打包请使用`./gradlew assemble`命令，否则可能因本地gradle版本不匹配导致问题
8. github release增加MD5校验，若发现MD5值和release包计算得出的不一致，请勿使用，网盘会一直放在那。
9. 部分版本因为工作原因可能无法及时更新，请自行打包（一般ES小版本迭代不会有大的改动，只需要更改版本号即可，版本号在gradle.properties中修改）
10. 6.x分支是我重新用gradle配置的一个分支，可能和原来的maven版本不一致，请大家谅解（进行中）
11. 单测的话，因为分词用到了自定义配置的东西，ES自己的test framework对这个配置加载有些问题，所以目前单测是没有的，大部分测试都是我自己直接部署进行测试的，后续会将测试点和测试项列出来供大家参考
12. 目前来说，应该Elasticsearch开源协议更换对该插件无影响，后续未知~~（毕竟插件小众，且非云上提供）
13. 在7.6.0版本后复原增加crf分词方式，不再采用CRFSegment，而是采用CRFLexicalAnalyzer进行分词，模型使用类似NLP方式采用单例实现
14. 在7.6.0版本后，ES在启动时增加了analyzer校验，所以若没有配置NLP或CRF模型，则启动插件时就不会加载，若要使用两种分词方式，需要添加模型后重启ES
15. 若要使用NLP或CRF分词方式，则配置文件hanlp.properties中模型路径配置中的文件名必须和实际名称一致（bin or txt），目前默认配置为data-for-1.7.5.zip（该数据包可在HanLP项目中下载）中的模型路径
16. 分支结构重新规范，目前提供更新维护的主要是master、7.x、6.x分支，5.x和2.x因个人精力有限不频繁做更新迭代

最后还是要说，开源不易，有空还是会跟进改动

----------

版本对应
----------

| Plugin version | Branch version  |
| :------------- | :-------------- |
| 7.x            | 7.x             |
| 6.x            | 6.x             |

安装步骤
----------

### 1. 下载安装ES对应Plugin Release版本

安装方式：

方式一

a. 下载对应的release安装包，最新release包可从baidu盘下载（链接:https://pan.baidu.com/s/1mFPNJXgiTPzZeqEjH_zifw  密码:i0o7）

b. 执行如下命令安装，其中PATH为插件包绝对路径：

`./bin/elasticsearch-plugin install file://${PATH}`

方式二

a. 使用elasticsearch插件脚本安装command如下：

`./bin/elasticsearch-plugin install https://github.com/KennFalcon/elasticsearch-analysis-hanlp/releases/download/v6.5.4/elasticsearch-analysis-hanlp-6.5.4.zip`

### 2. 安装数据包

release包中存放的为HanLP源码中默认的分词数据，若要下载完整版数据包，请查看[HanLP Release](https://github.com/hankcs/HanLP/releases)。

数据包目录：*ES_HOME*/plugins/analysis-hanlp

**注：因原版数据包自定义词典部分文件名为中文，这里的hanlp.properties中已修改为英文，请对应修改文件名**

### 3. 重启Elasticsearch

**注：上述说明中的ES_HOME为自己的ES安装路径，需要绝对路径**

### 4. 热更新

在本版本中，增加了词典热更新，修改步骤如下：

a. 在*ES_HOME*/plugins/analysis-hanlp/data/dictionary/custom目录中新增自定义词典

b. 修改hanlp.properties，修改CustomDictionaryPath，增加自定义词典配置

c. 等待1分钟后，词典自动加载

**注：每个节点都需要做上述更改**

提供的分词方式说明
----------

hanlp: hanlp默认分词

hanlp_standard: 标准分词

hanlp_index: 索引分词

hanlp_nlp: NLP分词

hanlp_crf: CRF分词

hanlp_n_short: N-最短路分词

hanlp_dijkstra: 最短路分词

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

自定义分词配置
----------

HanLP在提供了各类分词方式的基础上，也提供了一系列的分词配置，分词插件也提供了相关的分词配置，我们可以在通过如下配置来自定义自己的分词器：

| Config                               | Elastic version     |
| :----------------------------------- | :------------------ |
| enable_custom_config                 | 是否开启自定义配置    |
| enable_index_mode                    | 是否是索引分词        |
| enable_number_quantifier_recognize   | 是否识别数字和量词    |
| enable_custom_dictionary             | 是否加载用户词典      |
| enable_translated_name_recognize     | 是否识别音译人名      |
| enable_japanese_name_recognize       | 是否识别日本人名      |
| enable_organization_recognize        | 是否识别机构         |
| enable_place_recognize               | 是否识别地名         |
| enable_name_recognize                | 是否识别中国人名      | 
| enable_traditional_chinese_mode      | 是否开启繁体中文      |
| enable_stop_dictionary               | 是否启用停用词        |
| enable_part_of_speech_tagging        | 是否开启词性标注      |
| enable_remote_dict                   | 是否开启远程词典      |
| enable_normalization                 | 是否执行字符正规化    |
| enable_offset                        | 是否计算偏移量        |

注意： 如果要采用如上配置配置自定义分词，需要设置enable_custom_config为true

例如：
```text
PUT test
{
  "settings": {
    "analysis": {
      "analyzer": {
        "my_hanlp_analyzer": {
          "tokenizer": "my_hanlp"
        }
      },
      "tokenizer": {
        "my_hanlp": {
          "type": "hanlp",
          "enable_stop_dictionary": true,
          "enable_custom_config": true
        }
      }
    }
  }
}
```

```text
POST test/_analyze
{
  "text": "美国,|=阿拉斯加州发生8.0级地震",
  "analyzer": "my_hanlp_analyzer"
}
```

结果：
```text
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
      "token" : ",|=",
      "start_offset" : 0,
      "end_offset" : 3,
      "type" : "w",
      "position" : 1
    },
    {
      "token" : "阿拉斯加州",
      "start_offset" : 0,
      "end_offset" : 5,
      "type" : "nsf",
      "position" : 2
    },
    {
      "token" : "发生",
      "start_offset" : 0,
      "end_offset" : 2,
      "type" : "v",
      "position" : 3
    },
    {
      "token" : "8.0",
      "start_offset" : 0,
      "end_offset" : 3,
      "type" : "m",
      "position" : 4
    },
    {
      "token" : "级",
      "start_offset" : 0,
      "end_offset" : 1,
      "type" : "q",
      "position" : 5
    },
    {
      "token" : "地震",
      "start_offset" : 0,
      "end_offset" : 2,
      "type" : "n",
      "position" : 6
    }
  ]
}

```

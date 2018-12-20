# elasticsearch-analysis-hanlp
HanLP Analyzer for ElasticSearch

[![Codacy Badge](https://api.codacy.com/project/badge/Grade/dbe4103dc2da4b6b89b5562aacaa3c3f)](https://app.codacy.com/app/kennfalcon/elasticsearch-analysis-hanlp?utm_source=github.com&utm_medium=referral&utm_content=KennFalcon/elasticsearch-analysis-hanlp&utm_campaign=Badge_Grade_Settings)
[![Build Status](https://travis-ci.com/KennFalcon/elasticsearch-analysis-hanlp.svg?branch=master)](https://travis-ci.com/KennFalcon/elasticsearch-analysis-hanlp)
[![GitHub release](https://img.shields.io/github/release/KennFalcon/elasticsearch-analysis-hanlp.svg)](https://github.com/KennFalcon/elasticsearch-analysis-hanlp/releases)

此分词器基于HanLP，提供了HanLP中大部分的分词方式。(http://www.hankcs.com/nlp）

🚩推迟好久对接Elasticsearch新版本了，2018年12月5日一次性发布对接了从Elasticsearch 6.4.0到Elasticsearch 6.5.1，主要推迟原因是公司忙以及想做实时自定义词典的功能，所以推迟了这么久。新功能还在摸索中，还在不断的膜拜ES代码，所以决定先给大家适配一下近几个月的ES版本。每个版本自己做了一下单点测试，都没有太多问题，如果有问题大家就提issue吧，如果我能看到会及时回复的，当然也可以发我邮箱kennfalcon@163.com联系我。

## 版本对应

1. 下载安装ES对应Plugin Release版本

| Plugin version | Elastic version |
| --- | --- |
|master|6.x|
|6.5.1|6.5.1|
|6.5.0|6.5.0|
|6.4.3|6.4.3|
|6.4.2|6.4.2|
|6.4.1|6.4.1|
|6.4.0|6.4.0|
|6.3.2|6.3.2|
|6.3.1|6.3.1|
|6.2.2|6.2.2|
|5.2.2|5.2.2|

安装方式：

方式一

   a. 下载对应的release安装包
   
   b. 将相关内容解压至*ES_HOME*/plugins/analysis-hanlp
   
   c. 将config目录下的文件移动至*ES_HOME*/config/analysis-hanlp
   
   d. 解压出的data目录为词典目录
   
方式二

   a. 使用elasticsearch插件脚本安装command如下：
   
   `./bin/elasticsearch-plugin install https://github.com/KennFalcon/elasticsearch-analysis-hanlp/releases/download/v6.5.1/elasticsearch-analysis-hanlp-6.5.1.zip`

2. 安装数据包

release包中存放的为HanLP源码中默认的分词数据，若要下载完整版数据包，请查看https://github.com/hankcs/HanLP/releases

数据包目录：*ES_HOME*/analysis-hanlp

**注：因原版数据包自定义词典部分文件为中文，这里的hanlp.properties中已修改为英文，请对应修改文件名**

3. 重启Elasticsearch

**注：上述说明中的ES_HOME为自己的ES安装路径，需要绝对路径**

4. 热更新

在本版本中，增加了词典热更新，修改步骤如下：

a. 在*ES_HOME*/analysis-hanlp/data/dictionary/custom目录中新增自定义词典

b. 修改hanlp.properties，修改CustomDictionaryPath，增加自定义词典配置

c. 等待1分钟后，词典自动加载

**注：每个节点都需要做上述更改**

## 提供的分词器说明

hanlp: hanlp默认分词

hanlp_standard: 标准分词

hanlp_index: 索引分词

hanlp_nlp: NLP分词

hanlp_n_short: N-最短路分词

hanlp_dijkstra: 最短路分词

hanlp_crf: CRF分词（在hanlp 1.6.6已开始废弃）

hanlp_speed: 极速词典分词

## 样例

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


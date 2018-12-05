# elasticsearch-analysis-hanlp
HanLP Analyzer for ElasticSearch

此分词器基于HanLP，提供了HanLP中大部分的分词方式。( http://www.hankcs.com/nlp ）

## 版本对应

1. 下载安装ES对应Plugin Release版本

| Plugin version | Elastic version |
| --- | --- |
| master | 6.x |
| 6.5.0 | 6.5.0 |
| 6.4.3 | 6.4.3 |
| 6.4.2 | 6.4.2 |
| 6.4.1 | 6.4.1 |
| 6.4.0 | 6.4.0 |
| 6.3.2 | 6.3.2 |
| 6.3.1 | 6.3.1 |
| 6.2.2 | 6.2.2 |
| 5.2.2 | 5.2.2 |

安装方式：

方式一

   a. 下载对应的release安装包
   
   b. 将相关内容解压至*ES_HOME*/plugins/analysis-hanlp
   
   c. 将config目录下的文件移动至*ES_HOME*/config/analysis-hanlp
   
   d. 解压出的data目录为词典目录
   
方式二

   a. 使用elasticsearch插件脚本安装command如下：
   
   `./bin/elasticsearch-plugin install https://github.com/KennFalcon/elasticsearch-analysis-hanlp/releases/download/v6.5.0/elasticsearch-analysis-hanlp-6.5.0.zip`

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

```
POST http://localhost:9200/twitter2/_analyze
{
  "text": "美国阿拉斯加州发生8.0级地震",
  "tokenizer": "hanlp"
}
```

```
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


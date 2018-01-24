# elasticsearch-analysis-hanlp
HanLP Analyzer for ElasticSearch

此分词器基于HanLP，提供了HanLP中大部分的分词方式。(http://www.hankcs.com/nlp）

## 版本对应

1. 下载ES对应Plugin Release版本


| Plugin version | Elastic version |
| --- | --- |
| master | 5.x |
| 5.2.2 | 5.2.2 |


copy and unzip elasticsearch-analysis-hanlp-{version}.zip to *ES_HOME*/plugins/elasticsearch-analysis-hanlp/

2. 安装数据包

release包中存放的为HanLP源码中默认的分词数据，若要下载完整版数据包，请查看https://github.com/hankcs/HanLP/releases

数据包目录：*ES_HOME*/plugins/elasticsearch-analysis-hanlp/config

3. 增加文件读取权限

修改 *ES_HOME*/config 目录下的 jvm.options 文件添加一行（读取hanlp.properties配置文件需要）

-Djava.security.policy=file:///*ES_HOME*/plugins/elasticsearch-hanlp/plugin-security.policy


4. 重启Elasticsearch

**注：上述说明中的ES_HOME为自己的ES安装路径，需要绝对路径**

## 提供的分词器说明

hanlp: hanlp默认分词

hanlp_standard: 标准分词

hanlp_index: 索引分词

hanlp_index: NLP分词

hanlp_n_short: N-最短路分词

hanlp_dijkstra: 最短路分词

hanlp_crf: CRF分词

hanlp_speed: 极速词典分词

## 样例

```
POST http://localhost:9200/twitter2/_analyze?analyzer=hanlp&pretty=true&text=美国阿拉斯加州发生8.0级地震
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


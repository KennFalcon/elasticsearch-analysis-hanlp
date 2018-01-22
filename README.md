# elasticsearch-analysis-hanlp
HanLP Analyzer for ElasticSearch

此分词器基于HanLP，提供了HanLP中大部分的分词方式。暂只支持Elasticsearch 5.x.x。(http://www.hankcs.com/nlp）

## 安装方式
1. 编译

checkout ik version respective to your elasticsearch version

git checkout tags/{version}

mvn package

copy and unzip target/releases/elasticsearch-analysis-hanlp-{version}.zip to your-es-root/plugins/hanlp

2. 安装数据包

下载数据包，具体路径请查看https://github.com/hankcs/HanLP/releases

在ES的config目录下，新建hanlp目录，将解压好的数据包放入该目录

在hanlp目录下新建config目录，将hanlp的配置文件hanlp.properties文件放入该目录

修改hanlp.properties文件，将root路径配置为数据包的路径

修改ES的bin/elasticsearch.in.sh文件

将下行：

ES_CLASSPATH="$ES_HOME/lib/elasticsearch-5.x.x.jar:$ES_HOME/lib/*"

修改为

ES_CLASSPATH="$ES_HOME/lib/elasticsearch-5.x.x.jar:$ES_HOME/lib/*:$ES_HOME/config/hanlp/config"

3. 重启Elasticsearch

## 提供的分词器说明

hanlp: hanlp默认分词

hanlp_standard: 标准分词

hanlp_index: 索引分词

hanlp_index: NLP分词

hanlp_n_short: N-最短路分词

hanlp_dijkstra: 最短路分词

hanlp_crf: CRF分词

hanlp_speed: 极速词典分词（配置为支持自定义字典以及启用数词和数量词识别，若有不同需求，请自行修改代码重新编译打包）

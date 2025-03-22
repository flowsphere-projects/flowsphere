<div align="center">
	<p></p>
	<p></p>
    <img src="https://github.com/thalossphere-projects/thalossphere/blob/main/docs/logo.png"  alt="图片名称" align=center />
	<h1>云原生流量治理解决方案</h1>


![Static Badge](https://img.shields.io/badge/thalossphere-1.5.0-blue)
![Static Badge](https://img.shields.io/badge/Spring%20Cloud-2.2.9-blue?logo=Spring)
![Static Badge](https://img.shields.io/badge/Spring%20Cloud%20Alibaba-2.2.9-blue?logo=Spring)
![Static Badge](https://img.shields.io/badge/license-Apache%202.0-blue)
![Static Badge](https://img.shields.io/badge/maven%20central-1.0.0-blue)



</div>



# 简介
thalossphere是基于bytebuddy字节码增强技术进行建设，采用插件化方式进行整体架构设计，利用字节码增强技术为微服务提供全链路流量治理能力。全方位扩展SpringCloud&SpringCloudAlibaba，
扩展Java生态提供高性能，低资源损耗，降本增效的流量治理框架。

# 架构图

## 全局架构图
![](https://github.com/thalossphere-projects/thalossphere/blob/main/docs/agent.png)

## 全链路泳道全景图
![](https://github.com/thalossphere-projects/thalossphere/blob/main/docs/panoramic.png)

# 微服务控制台

<table>
  <tbody>
    <tr align="center">
      <td width="50%"><img style="max-height:75%;max-width:75%;" src="https://github.com/thalossphere-projects/thalossphere/blob/main/docs/service-search.png"></td>
      <td width="50%"><img style="max-height:75%;max-width:75%;" src="https://github.com/thalossphere-projects/thalossphere/blob/main/docs/api.png"></td>
    </tr>
    <tr align="center">
      <td width="50%"><img style="max-height:75%;max-width:75%;" src="https://github.com/thalossphere-projects/thalossphere/blob/main/docs/ip.png"></td>
      <td width="50%"><img style="max-height:75%;max-width:75%;" src="https://github.com/thalossphere-projects/thalossphere/blob/main/docs/consumer.png"></td>
    </tr>
  </tbody>
</table>

# 主要特性

1.支持基于标签路由全链路灰度发布，支持Http、MQ、JOB多种标签过滤方式

2.支持基于用户、区域维度标签权重负载均衡路由

3.支持无侵入式接入Sentinel框架，轻松拥有限流、故障隔离等能力

4.采用字节码增强技术，对业务代码无侵入，业务性能影响最小；

5.采用微内核架构，强类隔离，简单易用的扩展和配置体系。

6.支持查看生产者接口调用方关系

# 基本能力

- 支持阿里巴巴Nacos、Eureka、Consul和Zookeeper四个服务注册发现中心

- 支持阿里巴巴Nacos、携程Apollo两个远程配置中心

- 支持阿里巴巴Sentinel熔断限流降级中间件

- 支持Java Agent解决异步跨线程ThreadLocal上下文传递

- 支持Spring Cloud Gateway、Zuul网关和微服务三大模块的灰度发布和路由等一系列功能

- 支持SpringCloud查看生产者接口调用方关系

# 使用场景
## 环境流量隔离

- 如果你们还在为了开发环境、测试环境流量无法隔离，那么请使用！

- 如果你们还在为了搭建多套开发、测试环境导致成本暴增，那么请使用！

- 如果你们还在烦恼一套环境处理多版本并行需求导致代码冲突、功能冲突，那么请使用！

## 灰度发布

- 如果你们想新版本迭代上线功能流量按比例放出，那么请使用！

- 如果你们还在为了版本迭代上线功能出故障范围不可控而苦恼，那么请使用！

## 熔断限流降级

- 如果你们不想侵入式实现熔断限流降级，那么请使用！


# 环境要求
## 语言环境

- Java 8
- Maven 3.2.5+

## 支持组件版本

| thalossphere版本 | SpringCloud版本 | SpringCloudAlibaba版本 | ElasticJob版本 |
|--------------|---------------|----------------------|----------|
| 1.x          | Spring Cloud Hoxton            | 2.2.x                 |2.1.5.RELEASE          |


# 使用方式
## JVM参数配置

-javaagent:xxx\thalossphere-agent-0.0.1-RELEASE.jar -Dflowshpere.tag=tagA

## 异步使用方式

-javaagent:xxx\thalossphere-agent-0.0.1-RELEASE.jar -Dflowshpere.tag=tagA -Dasync.thread.package.path=xxx

# 配置
## Http-Header参数
| Key | value |
|------------|-------|
| tag        | tagA  |


## agent.yaml配置文件

agent.yaml是thalossphere核心配置文件，主要配置需要加载的组件以及选择对应的配置中心。当前thalossphere支持本地模式和配置中心。配置中心支持Nacos、Apollo，需要修改agent.yaml文件来激活使用方式。具体示例如下：

### 组件配置

```yaml
plugins:
  - nacos
  - rocketmq
  - springmvc
  - springcloudgateway
  - feign
  - dubbo
  - elasticjob
  - zuul
  - eureka
  - springboot2
  - zookeeper
  - consul
  - mybatis
```

### sentinel开关

```yaml
sentinelEnabled: false/true
```

### warmup优雅上线开关

```yaml
warmupEnabled: false/true
```

### discoveryBinder开关
查看微服务接口生产者与消费者绑定关系开关
```yaml
discoveryBinderEnabled: true/false
```

### thalossphere-server配置

```yaml
serverAddr: http://127.0.0.1:8224
```

### Nacos

#### 配置项

```yaml
plugins:
 pluginConfigDataSource:
   type: nacos
   pros:
     dataId: default
     groupId: DEFAULT_GROUP
     timeout: 3000
     serverAddr: 127.0.0.1:8848
```

#### 使用方式

### Apollo

#### 配置项

```yaml
plugins:
 pluginConfigDataSource:
   type: apollo
   pros:
     appId: 101
     apolloMeta: http://localhost:8080
     env: DEV
```

#### 使用方式

### Local

```yaml
plugins:
 pluginConfigDataSource:
   type: local
```

#### 使用方式

需要配置plugin-config.yaml文件，具体参考文末"完整配置->yaml文件格式"内容配置\
 

## plugin-config.yaml配置

### SpringCloudGateway配置

#### 配置项

| Key                                              | Value                                                       | 说明                                |
| :----------------------------------------------- | :---------------------------------------------------------- | :-------------------------------- |
| springCloudGatewayConfig.regionWeight.regions    | \["1","2"]                                                  | 区域配置，数组类型                         |
| springCloudGatewayConfig.regionWeight.tagWeights | \[{"tag":"tagA","weight:0.2"},{"tag":"tagA1","weight:0.8"}] | 区域标签权重配置，0.2代表20%流量走tagA标签服务，数组类型 |
| springCloudGatewayConfig.userWeight.userIds      | \["1","2"]                                                  | 用户ID配置，数组类型                       |
| springCloudGatewayConfig.userWeight.tagWeights   | \[{"tag":"tagA","weight:0.2"},{"tag":"tagA1","weight:0.8"}] | 用户权重配置，0.2代表20%流量走tagA标签服务，数组类型   |

#### 示例

```json
{
    "springCloudGatewayConfig": {
        "regionWeight": {
            "regions": [
                "123"
            ],
            "tagWeights": [
                {
                    "tag": "tagA",
                    "weight": 0.2
                },
                {
                    "tag": "tagA1",
                    "weight": 0.8
                }
            ]
        },
        "userWeight": {
            "userIds": [
                "123"
            ],
            "tagWeights": [
                {
                    "tag": "tagA",
                    "weight": 0.2
                },
                {
                    "tag": "tagA1",
                    "weight": 0.8
                }
            ]
        }
    }
}
```

### Zuul配置

#### 配置项

| Key                                | Value                                                       | 说明                                |
| :--------------------------------- | :---------------------------------------------------------- | :-------------------------------- |
| zuulConfig.regionWeight.regions    | \["1","2"]                                                  | 区域配置，数组类型                         |
| zuulConfig.regionWeight.tagWeights | \[{"tag":"tagA","weight:0.2"},{"tag":"tagA1","weight:0.8"}] | 区域标签权重配置，0.2代表20%流量走tagA标签服务，数组类型 |
| zuulConfig.userWeight.userIds      | \["1","2"]                                                  | 用户ID配置，数组类型                       |
| zuulConfig.userWeight.tagWeights   | \[{"tag":"tagA","weight:0.2"},{"tag":"tagA1","weight:0.8"}] | 用户权重配置，0.2代表20%流量走tagA标签服务，数组类型   |

#### 示例

```json
{
    "zuulConfig": {
        "regionWeight": {
            "regions": [
                "123"
            ],
            "tagWeights": [
                {
                    "tag": "tagA",
                    "weight": 0.2
                },
                {
                    "tag": "tagA1",
                    "weight": 0.8
                }
            ]
        },
        "userWeight": {
            "userIds": [
                "123"
            ],
            "tagWeights": [
                {
                    "tag": "tagA",
                    "weight": 0.2
                },
                {
                    "tag": "tagA1",
                    "weight": 0.8
                }
            ]
        }
    }
}
```

### RocketMQ配置

#### 配置项

| Key                                      | Value               | 说明          |
| :--------------------------------------- | :------------------ | :---------- |
| consumerConfigList\[0].consumerGroupName | myConsumerGroupName | 消费者组名称      |
| consumerConfigList\[0].tag               | myTag               | 消费者Tag（非必填） |
| consumerConfigList\[0].queueIdList\[0]   | \[1,2]              | 消费队列ID      |
| producerConfigList\[0].topic             | myTopic             | 生产者主题       |
| producerConfigList\[0].tag               | myTag               | 生产者Tag（非必填） |
| producerConfigList\[0].queueIdList\[0]   | \[1,2]              | 生产者队列ID     |

#### 示例

```json
{
  "rocketMQConfig": {
    "consumerConfigList": [
      {
        "consumerGroupName": "CID_JODIE_3",
        "tag": "tagB",
        "queueList": [
            0
        ]
      }
    ],
    "producerConfigList": [
      {
        "topic": "myTopic",
        "tag": "tagB",
        "queueIdList": [
          0
        ]
      }
    ]
  }
}
```

### Sentinel配置

#### 配置

| Key                                                          | Value                                 | 说明                    |
| :----------------------------------------------------------- | :------------------------------------ | :-------------------- |
| sentinelConfig.appName                                       | myApp                                 | 应用名,apollo数据源与动态限流时使用 |
| sentinelConfig.dataId                                        | dataId                                | nacos数据源时需要配置         |
| sentinelConfig.groupId                                       | groupId                               | nacos数据源时需要配置         |
| sentinelConfig.namespace                                     | namespace                             | apollo数据源时需要配置        |
| sentinelConfig.ruleKey                                       | ruleKey                               | apollo数据源时需要配置        |
| sentinelConfig.resourceLimitEnabled                          | true\false                            | 资源限流开关                |
| sentinelConfig.limitReturnResult                             | {"code":429,"message":"服务器繁忙请稍后重试1！"} | 限流返回结果，json格式         |
| sentinelConfig.httpApiLimitConfig.allUrlLimitEnabled         | true\false                            | 是否采集所有待配置限流url        |
| sentinelConfig.httpApiLimitConfig.excludeLimitUrlList\[0]    | /helloword                            | 需要限流的url              |
| sentinelConfig.mybatisApiLimitConfig.allMethodLimitEnabled   | true                                  | 是否开启mybatis方法拦截限流     |
| sentinelConfig.mybatisApiLimitConfig.excludeLimitUrlList\[0] | findByName                            | 需要拦截的方法名              |

#### 示例

```json
{
    "sentinelConfig": {
        "namespace": "xxx",
        "ruleKey": "xxx",
        "dataId": "service-a-flow-rules",
        "groupId": "SENTINEL_GROUP",
        "namespace": "namespace",
        "resourceLimitEnabled": true,
        "circuitBreakerEnabled": true,
        "limitReturnResult": {
            "code": 429,
            "message": "服务器繁忙请稍后重试1！"
        },
        "mybatisApiLimitConfig": {
            "allMethodLimitEnabled": true,
            "excludeLimitUrlList": [
                "findByName"
            ]
        },
        "httpApiLimitConfig": {
            "allUrlLimitEnabled": true,
            "excludeLimitUrlList": [
                "/helloWord",
                "/lazy",
                "/restTemplate",
                "findById"
            ]
        }
    }
}
```

### Elastic Job配置

#### 配置

| Key         | Value      | 说明            |
| :---------- | :--------- | :------------ |
| grayEnabled | true\false | 灰度开关          |
| ip          | 127.0.0.1  | 需要灰度执行的实例ip地址 |

#### 示例

```json
{
    "elasticJobConfig": {
        "grayEnabled": true,
        "ip": "192.168.0.12"
    }
}
```

 

### Removal配置

#### 配置

| Key             | Value                           | 说明        |
| :-------------- | :------------------------------ | :-------- |
| applicationName | xxx                             | 应用名称      |
| minInstanceNum  | xxx                             | 最小实例数     |
| errorRate       | 0.1\~1                          | 错误率       |
| recoveryTime    | xxx                             | 恢复时长（毫秒）  |
| exceptions      | \["java.lang.RuntimeException"] | 异常数组      |
| httpStatus      | \[400]                          | http响应码数字 |
| scaleUpLimit    | xxx                             | 实例上线      |
| windowsTime     | xxxx                            | 检测间隔（毫秒）  |

#### 示例

```
{
    "removalConfig": {
        "applicationName": "service-b",
        "minInstanceNum": 1,
        "errorRate": 1,
        "recoveryTime": 100000,
        "exceptions": [
        ],
        "httpStatus":[
            400
        ],
        "scaleUpLimit": 1,
        "windowsTime": 10000
    }
}
```

### 完整配置

#### Yaml格式

使用范围：plugins.pluginConfigDataSource.type=local

```yaml
pluginConfig:
  rocketMQConfig:
    consumerConfigList:
      - consumerGroupName: CID_JODIE_3
        tag: tagB
        queueIdList: [0]
    producerConfigList:
      - topic: myTest
        tag: tagB
        queueIdList: [0]
  elasticJobConfig:
    grayEnabled: true
    ip: 192.168.0.12
  sentinelConfig:
    namespaceName: xxx
    ruleKey: xxx
    dataId: service-a
    groupId: SENTINEL_GROUP
    resourceLimitEnabled: true
    circuitBreakerEnabled: true
    limitReturnResult: {"code":429,"message":"服务器繁忙请稍后重试1！"}
    mybatisApiLimitConfig:
      allMethodLimitEnabled: true
      excludeLimitMethodList:
        - findByName
    httpApiLimitConfig:
      allUrlLimitEnabled: true
      excludeLimitUrlList:
        - /myTest/helloWord
  springCloudGatewayConfig:
    regionWeight:
      regions:
        - 101
      tagWeights:
        - tag: tagA
          weight: 0.2
    userWeight:
      userIds:
        - 123
      tagWeights:
        - tag: tagA
          weight: 0.2
  zuulConfig:
    regionWeight:
      regions:
        - 101
      tagWeights:
        - tag: tagA
          weight: 0.2
    userWeight:
      userIds:
        - 123
      tagWeights:
        - tag: tagA
          weight: 0.2
  removalConfig:
    applicationName: service-b
    minInstanceNum: 1
    errorRate: 1
    recoveryTime: 1000
    exceptions:
      - "java.lang.RuntimeException"
    httpStatus:
      - 400
    scaleUpLimit: 1
    windowsTime: 10000
    
```

#### Json格式

使用范围：plugins.pluginConfigDataSource.type=nacos、apollo

```json
{
    "rocketMQConfig": {
        "consumerConfigList": [
            {
                "consumerGroupName": "default_test_consumer_group_11",
                "tag": "tagA",
                "queueIdList": [
                    0,
                    1,
                    2,
                    3,
                    4,
                    5,
                    6
                ]
            }
        ],
        "producerConfigList": [
            {
                "topic": "TopicTest",
                "tag": "tagA",
                "queueIdList": [
                    0,
                    1,
                    2,
                    3,
                    4,
                    5,
                    6
                ]
            }
        ]
    },
    "elasticJobConfig": {
        "grayEnabled": true,
        "ip": "192.168.0.12"
    },
    "springCloudGatewayConfig": {
        "regionWeight": {
            "regions": [
                "123"
            ],
            "tagWeights": [
                {
                    "tag": "tagA",
                    "weight": 0.5
                },
                {
                    "tag": "tagA1",
                    "weight": 0.5
                }
            ]
        },
        "userWeight": {
            "userIds": [
                "123"
            ],
            "tagWeights": [
                {
                    "tag": "tagA",
                    "weight": 0.2
                },
                {
                    "tag": "tagA1",
                    "weight": 0.8
                }
            ]
        }
    },
    "zuulConfig": {
        "regionWeight": {
            "regions": [
                "123"
            ],
            "tagWeights": [
                {
                    "tag": "tagA",
                    "weight": 1
                },
                {
                    "tag": "tagA1",
                    "weight": 0
                }
            ]
        },
        "userWeight": {
            "userIds": [
                "123"
            ],
            "tagWeights": [
                {
                    "tag": "tagA",
                    "weight": 0.2
                },
                {
                    "tag": "tagA1",
                    "weight": 0.8
                }
            ]
        }
    },
    "removalConfig": {
        "applicationName": "service-b",
        "minInstanceNum": 1,
        "errorRate": 1,
        "recoveryTime": 100000,
        "exceptions": [

        ],
        "httpStatus": [
            400
        ],
        "scaleUpLimit": 1,
        "windowsTime": 10000
    },
    "sentinelConfig": {
        "namespace": "xxx",
        "ruleKey": "xxx",
        "dataId": "service-a-flow-rules",
        "groupId": "SENTINEL_GROUP",
        "namespace": "namespace",
        "resourceLimitEnabled": true,
        "circuitBreakerEnabled": true,
        "limitReturnResult": {
            "code": 429,
            "message": "服务器繁忙请稍后重试1！"
        },
        "mybatisApiLimitConfig": {
            "allMethodLimitEnabled": true,
            "excludeLimitUrlList": [
                "findByName"
            ]
        },
        "httpApiLimitConfig": {
            "allUrlLimitEnabled": true,
            "excludeLimitUrlList": [
                "/helloWord",
                "/lazy",
                "/restTemplate",
                "findById"
            ]
        }
    }
}
```

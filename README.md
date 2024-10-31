<div align="center">
	<p></p>
	<p></p>
    <img src="https://github.com/flowsphere-projects/flowsphere/blob/main/docs/logo.png"  alt="图片名称" align=center />
	<h1>云原生流量治理解决方案</h1>


![Static Badge](https://img.shields.io/badge/flowsphere-1.0.0-blue)
![Static Badge](https://img.shields.io/badge/Spring%20Cloud-2.2.9-blue?logo=Spring)
![Static Badge](https://img.shields.io/badge/Spring%20Cloud%20Alibaba-2.2.9-blue?logo=Spring)
![Static Badge](https://img.shields.io/badge/license-Apache%202.0-blue)
![Static Badge](https://img.shields.io/badge/maven%20central-1.0.0-blue)



</div>


# 简介
flowsphere是基于bytebuddy字节码增强技术进行建设，采用插件化方式进行整体架构设计，利用字节码增强技术为微服务提供全链路流量治理能力。全方位扩展SpringCloud&SpringCloudAlibaba，
扩展Java生态提供高性能，低资源损耗，降本增效的流量治理框架。

# 架构图

## 全局架构图
![](https://github.com/flowsphere-projects/flowsphere/blob/main/docs/agent.png)

## 全链路泳道全景图
![](https://github.com/flowsphere-projects/flowsphere/blob/main/docs/panoramic.png)

# 微服务控制台

<table>
  <tbody>
    <tr align="center">
      <td width="50%"><img style="max-height:75%;max-width:75%;" src="https://github.com/flowsphere-projects/flowsphere/blob/main/docs/service-search.png"></td>
      <td width="50%"><img style="max-height:75%;max-width:75%;" src="https://github.com/flowsphere-projects/flowsphere/blob/main/docs/api.png"></td>
    </tr>
    <tr align="center">
      <td width="50%"><img style="max-height:75%;max-width:75%;" src="https://github.com/flowsphere-projects/flowsphere/blob/main/docs/ip.png"></td>
      <td width="50%"><img style="max-height:75%;max-width:75%;" src="https://github.com/flowsphere-projects/flowsphere/blob/main/docs/consumer.png"></td>
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

| flowsphere版本 | SpringCloud版本 | SpringCloudAlibaba版本 | ElasticJob版本 |
|--------------|---------------|----------------------|----------|
| 1.0          | Spring Cloud Hoxton            | 2.2.x                 |2.1.5.RELEASE          |


# 使用方式
## JVM参数配置

-javaagent:xxx\flowsphere-agent-0.0.1-RELEASE.jar -Dflowshpere.tag=tagA

## 异步使用方式

-javaagent:xxx\flowsphere-agent-0.0.1-RELEASE.jar -Dflowshpere.tag=tagA -Dasync.thread.package.path=xxx

# 配置
## Http-Header参数
| Key | value |
|------------|-------|
| tag        | tagA  |

# 付费社区


## 社区服务

1.更加完整的flowsphere使用文档、配置文档

2.提供答疑服务，无次数限制提问，当天内必定得到详细的回复和指导

3.独家内容帮助用户深度理解flowsphere原理、源码

4.不定期分享编写简历技巧、三高（高性能、高可用、高并发）、线上紧急问题处理技巧、各种分布式场景下解决方案等知识点

## 社区二维码

![](https://github.com/flowsphere-projects/flowsphere/blob/main/docs/knowledge-planet.png)
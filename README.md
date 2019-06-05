## commons
------
Java 基础工具集合包

## Java环境
oracle-jdk12

## 模块划分

* commons-parent: 父模块, 进行项目说明及相关依赖声明
* commons-core: 核心模块, 定义最基础的Java相关操作工具集合
* commons-warp: 对一些常用的Java生态中的工具进行封装集成, 比如okhhtp, httpclient, javax.mail, poi等

## 发布

1. 发布到sonatype, 供大家引入使用

```
mvn clean deploy -Dmaven.skip.test=true -X
```

## 功能描述

* 常用签名及加解密工具类: Base64, MD5, DES, 3DES, DSA, ECDSA, RSA, SHARS;


## 引入使用

1. 在项目pom.xml文件中加入依赖

```
<dependency>
  <groupId>com.github.hinsteny</groupId>
  <artifactId>commons-core</artifactId>
  <version>0.0.2</version>
</dependency>
```

2. 在项目的module-info.java文件中添加模块依赖

```
    requires com.github.hinsteny.commons.core;
```

3. 使用模块中的工具类
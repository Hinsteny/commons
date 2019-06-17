## commons (Java 基础工具集合包)
-------------------

[![Codacy Badge](https://api.codacy.com/project/badge/Grade/f22e5eaec8e145cca26cb3cc74503b94)](https://www.codacy.com/app/Hinsteny/commons?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=Hinsteny/commons&amp;utm_campaign=Badge_Grade)
[![Travis Build Status](https://img.shields.io/travis/Hinsteny/commons.svg)](https://travis-ci.org/Hinsteny/commons)
[![Codecov](https://codecov.io/gh/Hinsteny/commons/branch/master/graph/badge.svg)](https://codecov.io/gh/Hinsteny/commons)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.hinsteny/commons-core/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.hinsteny/commons-core)

## Java环境
open-jdk12

## 模块划分

*  commons-parent: 父模块, 进行项目说明及相关依赖声明
*  commons-core: 核心模块, 定义最基础的Java相关操作工具集合
*  commons-warp: 对一些常用的Java生态中的工具进行封装集成, 比如okhhtp, httpclient, javax.mail, poi等

## 开发与发布

1.  发布到sonatype, 供大家引入使用

```
  mvn clean deploy -Dmaven.skip.test=true -X
```

2.  项目中配置使用jacoco做代码覆盖率检测, 执行命令为

```
  mvn clean test
```

## 功能描述

*  常用签名及加解密工具类: Base64, MD5, DES, 3DES, DSA, ECDSA, RSA, SHARS;
*  常见算法工具类实现: 二叉堆(堆排序)

## 引入使用

1.  在项目pom.xml文件中加入依赖

```
  <dependency>
    <groupId>com.github.hinsteny</groupId>
    <artifactId>commons-core</artifactId>
    <version>0.0.3</version>
  </dependency>
```

2.  在项目的module-info.java文件中添加模块依赖

```
  requires com.github.hinsteny.commons.core;
```

3.  使用模块中的工具类

```
  String myMessage = "Hello Hinsteny. nice to see you!";
  KeyPair keyPair = RSASignUtil.generateKeyPair();
  String publicKey = RSASignUtil.getPublicKey(keyPair);
  String privateKey = RSASignUtil.getPrivateKey(keyPair);
  System.out.println("============== 默认RSA签名前后使用Base64加解码 ==============");
  String sign = signWithRsa(myMessage, privateKey, DEFAULT_CHARSET);
  System.out.println(String.format("sign data: %s, result: %s", myMessage, sign));
  boolean verify = verifyWithRsa(myMessage, sign, publicKey, DEFAULT_CHARSET);
  System.out.println("verify result: " + verify);
```

## License

[MIT](http://opensource.org/licenses/MIT)
Copyright (c) 2019-present, Hinsteny
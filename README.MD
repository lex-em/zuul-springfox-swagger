# Swagger addon for Spring Cloud ZUUL
[![Build Status](https://travis-ci.org/lex-em/zuul-springfox-swagger.svg?branch=develop)](https://travis-ci.org/lex-em/zuul-springfox-swagger)[![Release](https://jitpack.io/v/ru.reliabletech/zuul-springfox-swagger.svg)](https://jitpack.io/#ru.reliabletech/zuul-springfox-swagger)

## Overview

Repository contains library for using springfox swagger UI under ZUUL proxy

## Requirements

 - Gradle 5.x.
 - Java 8 or later

## License information
Swagger addon for Spring Cloud ZUUL is open source software released under the [Apache 2.0 license][1].

## Usage
### Configuration
Plugin extends ZUUL configuration:
```yaml
zuul:
    prefix: /api
    default-swagger-url: v2/api-docs # additional path for swagger documentation, default: v2/api-docs
    default-protocol: http # default target service protocol, default: http
    default-swagger-resources-url: swagger-resources # API, that returns list of springfox swagger resources, default: swagger-resources
    routes:
        service1:
            path: /service-1/**
            serviceId: service1
            swaggerUri: api-documentation # additional custom path for swagger documentation, use zuul.default-swagger-url as default
            protocol: https:// # additional protocol info, default: http://
            allowedGroups: # springfox swagger groups filtering. if list not defined or empty - all groups are showed.
              - securityGroup 
        service2:
            path: /shop/**
            url: http://${some-service-host}:${some-service-port}/api/shop # mapping for subset of API
            swaggerResourcesUri: swagger-resources # for services, that implement independent API for springfox swagger resources
            directSwaggerBaseUrl: http://${some-service-host}:${some-service-port}/ # in case when mapped only subset of API
                                                                                    # we need to say where the root of app
                                                                                    # otherwise swagger docs will requested from `url` as base
            directSwaggerPath: / # direct path to docs, that is relative from zuul.prefix in external calls (i.e. from /api/shop/)
            stripPrefix: false
```
To turn plugin on just put annotation `@EnableZuulSpringfoxSwagger`:
```java
@Configuration
@EnableZuulSpringfoxSwagger
public class YourConfiguration {
    ...
}
```

Plugin will configure two `RestTemplate`, one is `@LoadBalanced` and one is pure.

Since 0.2.0 version plugin supports versioned api (and other with route transformations via ServiceRouteMapper).
Any `org.springframework.cloud.netflix.zuul.filters.discovery.ServiceRouteMapper` bean will turn on this approach, but here some nuances, described in [#3](https://github.com/lex-em/zuul-springfox-swagger/issues/3).

Since 0.3.0 version plugin supports swagger resources calls and groups ([#6](https://github.com/lex-em/zuul-springfox-swagger/issues/6)). Also since 0.3.0 supports proxied services via url, not service registry info ([#5](https://github.com/lex-em/zuul-springfox-swagger/issues/5), look at `url` and `direct*` properties).
### Dependency 
```groovy
repositories {
    ...
    jcenter() 
    // or
    maven { url 'https://jitpack.io' }
}
dependencies {
    ...
    compile "ru.reliabletech:zuul-springfox-swagger:${zuulSpringfoxSwaggerVersion}"
    ...
}
...
```
Also available snapshots in jitpack version notation develop-SNAPSHOT: 
```groovy
repositories {
    ...
    maven { url 'https://jitpack.io' }
}
dependencies {
    ...
    compile "ru.reliabletech:zuul-springfox-swagger:develop-SNAPSHOT"
    ...
}
...
```

## Build

Gradle tasks may be running via `gradle` wrapper - `gradlew` from project root folder.

* build: `gradle clean build`

[1]: http://www.apache.org/licenses/LICENSE-2.0.html

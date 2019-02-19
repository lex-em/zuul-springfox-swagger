# Swagger addon for Spring Cloud ZUUL
[![Build Status](https://travis-ci.org/lex-em/zuul-springfox-swagger.svg?branch=develop)](https://travis-ci.org/lex-em/zuul-springfox-swagger)[![Release](https://jitpack.io/v/ru.reliabletech/zuul-springfox-swagger.svg)](https://jitpack.io/#ru.reliabletech/zuul-springfox-swagger)

## Overview

Repository contains library for using springfox swagger UI under ZUUL proxy

## Requirements

 - Gradle 3.x, or 4.x.
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
    routes:
        service1:
            path: /service-1/**
            serviceId: service1
            swaggerUrl: # additional custom path for swagger documentation, use zuul.default-swagger-url as default
            protocol: http:// # additional protocol info, default: http:// 
        service2:
            path: /other-service/**
            serviceId: some-service
            swaggerUrl: api-documentation
            protocol: https://
```
To turn plugin on just put annotation `@EnableZuulSpringfoxSwagger`:
```java
@Configuration
@EnableZuulSpringfoxSwagger
public class YourConfiguration {
    ...
}
```

Also plugin needs Swagger2 and RestTemplate (load balanced is preferable) to be configured.

If the project configuration do not contain that, then it will be configured automatically (since version 0.1.1).

Since 0.2.0 version plugin supports versioned api (and other with route transformations via ServiceRouteMapper).
Any `org.springframework.cloud.netflix.zuul.filters.discovery.ServiceRouteMapper` bean will turn on this approach, but here some nuances, described in #3.
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

Gradle tasks may be running via `gradle` wrapper - `gradlew`(gradle v4.3.1) from project root folder.

* build: `gradle clean build`

[1]: http://www.apache.org/licenses/LICENSE-2.0.html

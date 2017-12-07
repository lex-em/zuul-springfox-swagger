# Swagger addon for Spring Cloud ZUUL
[![Build Status](https://travis-ci.org/lex-em/zuul-springfox-swagger.svg?branch=develop)](https://travis-ci.org/lex-em/zuul-springfox-swagger)

## Overview

Repository contains library for using springfox swagger UI under ZUUL proxy

## Requirements

 - Gradle 3.x, or 4.x.
 - Java 8 or later
 
## Version information
Version: 0.2.0-SNAPSHOT

## License information
Swagger addon for Spring Cloud ZUUL is open source software released under the [Apache 2.0 license][1].

## Usage
Addon extend ZUUL configuration:
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

For use it you should include dependency (** SNAPSHOTS DO NOT AVAILABLE AT THIS MOMENT [#1](/../../issues/1)**):
```groovy
dependencies {
    ...
    compile "ru.reliabletech:zuul-springfox-swagger:${zuulSpringfoxSwaggerVersion}"
    ...
}
...
```
And add annotation:
```java
@Configuration
@EnableZuulSpringfoxSwagger
public class YourConfiguration {
    ...
}
```

## Build

Gradle tasks may be running via `gradle` wrapper - `gradlew`(gradle v4.3.1) from project root folder.

* build: `gradle clean build`

[1]: http://www.apache.org/licenses/LICENSE-2.0.html
package ru.reliabletech.zuul.swagger;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author Alexandr Emelyanov <mr.lex91@gmail.com>
 * on 27.11.2017.
 */
@ComponentScan(basePackageClasses = ZuulSpringfoxSwaggerConfiguration.class)
@EnableConfigurationProperties(ServicesSwaggerInfo.class)
public class ZuulSpringfoxSwaggerConfiguration {

}

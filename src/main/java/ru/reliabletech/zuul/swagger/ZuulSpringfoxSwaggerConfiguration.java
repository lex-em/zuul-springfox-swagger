package ru.reliabletech.zuul.swagger;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author Alexandr Emelyanov <mr.lex91@gmail.com>
 * on 27.11.2017.
 */
@ComponentScan(basePackageClasses = ZuulSpringfoxSwaggerConfiguration.class)
@EnableConfigurationProperties(ServicesSwaggerInfo.class)
public class ZuulSpringfoxSwaggerConfiguration {

    @ConditionalOnMissingBean(Docket.class)
    @EnableSwagger2
    public static final class EnableSwagger2Config {}

    @ConditionalOnMissingBean(RestTemplate.class)
    @LoadBalanced
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}

package ru.reliabletech.zuul.swagger;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.zuul.filters.discovery.ServiceRouteMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import ru.reliabletech.zuul.swagger.props.ServicesSwaggerInfo;
import ru.reliabletech.zuul.swagger.service.GenericRouteService;
import ru.reliabletech.zuul.swagger.service.RouteService;
import ru.reliabletech.zuul.swagger.service.ServiceRouteMapperRouteService;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Main configuration for ZuulSpringfoxPlugin
 *
 * @author Alexandr Emelyanov <mr.lex91@gmail.com>
 * on 27.11.2017.
 */
@Configuration
@ComponentScan(basePackageClasses = ZuulSpringfoxSwaggerConfiguration.class)
@EnableConfigurationProperties(ServicesSwaggerInfo.class)
public class ZuulSpringfoxSwaggerConfiguration {

    @ConditionalOnMissingBean(Docket.class)
    @EnableSwagger2
    public static final class EnableSwagger2Config {}

    @LoadBalanced
    @Bean
    public RestTemplate loadBalancedRestTemplate() {
        return new RestTemplate();
    }

    @Bean
    public RestTemplate pureRestTemplate() {
        return new RestTemplate();
    }

    /**
     * Configure automatically {@link ServiceRouteMapper } based {@link RouteService }
     *
     * @return
     */
    @Bean
    @ConditionalOnBean(ServiceRouteMapper.class)
    public RouteService versionedRouteService() {
        return new ServiceRouteMapperRouteService();
    }

    /**
     * Default {@link RouteService}
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(RouteService.class)
    public RouteService genericRouteService() {
        return new GenericRouteService();
    }
}

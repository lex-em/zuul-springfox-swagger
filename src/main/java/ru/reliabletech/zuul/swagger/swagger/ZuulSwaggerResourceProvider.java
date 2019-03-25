package ru.reliabletech.zuul.swagger.swagger;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import ru.reliabletech.zuul.swagger.props.ServicesSwaggerInfo;
import ru.reliabletech.zuul.swagger.service.SwaggerService;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Provide swagger resources for swagger-ui using discovery client available services list and configured statically route mappings
 *
 * @author Alexandr Emelyanov <mr.lex91@gmail.com>
 * on 27.11.2017.
 */
@Slf4j
@Primary
@Component
public class ZuulSwaggerResourceProvider implements SwaggerResourcesProvider {

    @Autowired
    private ServicesSwaggerInfo servicesSwaggerInfo;
    @Autowired
    private SwaggerService swaggerService;
    @Autowired
    private DiscoveryClient discoveryClient;

    @Override
    public List<SwaggerResource> get() {
        return Stream.concat(discoveryClient.getServices().stream(), servicesSwaggerInfo.getRouteNames().stream())
                .map(this::generateSwaggerDocumentationResource)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private SwaggerResource generateSwaggerDocumentationResource(String route) {
        String swaggerVersion;
        try {
            swaggerVersion = swaggerService.getSwaggerVersion(route);
        } catch (Exception e) {
            log.error(String.format("Some error during obtain swagger documentation for route '%s'", route), e);
            return null;
        }
        SwaggerResource swaggerResource = new SwaggerResource();
        swaggerResource.setName(route);
        swaggerResource.setLocation("/api-docs?route=" + route);
        swaggerResource.setSwaggerVersion(swaggerVersion);
        return swaggerResource;
    }

}
package ru.reliabletech.zuul.swagger.swagger;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;
import ru.reliabletech.zuul.swagger.props.ServicesSwaggerInfo;
import ru.reliabletech.zuul.swagger.service.SwaggerService;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.net.URI;
import java.net.URISyntaxException;
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
                .distinct()
                .flatMap(this::generateSwaggerDocumentationResource)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private Stream<SwaggerResource> generateSwaggerDocumentationResource(String route) {
        try {
            return swaggerService.getSwaggerResources(route)
                    .stream()
                    .map(swaggerResource -> {
                        MultiValueMap<String, String> queryParams = UriComponentsBuilder.fromUriString(swaggerResource.getUrl())
                                .build()
                                .getQueryParams();
                        String group = queryParams.getFirst("group");
                        if (group != null && !servicesSwaggerInfo.groupAllowed(route, group)) {
                            return null;
                        }
                        UriComponentsBuilder swaggerDocRouteBuilder = UriComponentsBuilder.fromPath("/api-docs")
                                .queryParam("route", route);
                        String routeName = route;
                        if (group != null) {
                            swaggerDocRouteBuilder.queryParam("group", group);
                            routeName = route + "-" +  group;
                        }
                        String swaggerVersion = swaggerResource.getSwaggerVersion();
                        SwaggerResource pluginSwaggerResource = new SwaggerResource();
                        pluginSwaggerResource.setName(routeName);
                        pluginSwaggerResource.setUrl(swaggerDocRouteBuilder.build().toUriString());
                        pluginSwaggerResource.setSwaggerVersion(swaggerVersion);
                        return pluginSwaggerResource;
                    })
                    .filter(Objects::nonNull);
        } catch (Exception e) {
            log.error(String.format("Some error during obtain swagger documentation for route %s", route), e);
            return Stream.empty();
        }
    }

}
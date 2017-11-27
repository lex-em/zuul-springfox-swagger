package ru.reliabletech.zuul.swagger;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Alexandr Emelyanov <mr.lex91@gmail.com>
 * on 27.11.2017.
 */
@Slf4j
@Primary
@Component
public class ZuulSwaggerResourceProvider implements SwaggerResourcesProvider {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ServicesSwaggerInfo servicesSwaggerInfo;
    @Autowired
    private SwaggerService swaggerService;

    @Override
    public List<SwaggerResource> get() {
        return servicesSwaggerInfo.getRouteNames()
                                  .stream()
                                  .map(route -> {
                                      ObjectNode swaggerDocumentation = swaggerService.getSwaggerDoc(route);
                                      return generateSwaggerDocumentationResource(route,
                                                                                  "/api-docs?route=" + route,
                                                                                  swaggerDocumentation.get("swagger").asText());
                                  })
                                  .collect(Collectors.toList());
    }

    private SwaggerResource generateSwaggerDocumentationResource(String name, String location, String version) {
        SwaggerResource swaggerResource = new SwaggerResource();
        swaggerResource.setName(name);
        swaggerResource.setLocation(location);
        swaggerResource.setSwaggerVersion(version);
        return swaggerResource;
    }

}
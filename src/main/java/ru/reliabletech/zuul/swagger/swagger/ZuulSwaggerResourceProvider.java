package ru.reliabletech.zuul.swagger.swagger;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import ru.reliabletech.zuul.swagger.props.ServicesSwaggerInfo;
import ru.reliabletech.zuul.swagger.service.SwaggerService;
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
    private ServicesSwaggerInfo servicesSwaggerInfo;
    @Autowired
    private SwaggerService swaggerService;

    @Override
    public List<SwaggerResource> get() {
        return servicesSwaggerInfo.getRouteNames() // TODO by available services
                                  .stream()
                                  .map(this::generateSwaggerDocumentationResource)
                                  .collect(Collectors.toList());
    }

    private SwaggerResource generateSwaggerDocumentationResource(String route) {
        String swaggerVersion = swaggerService.getSwaggerVersion(route);
        SwaggerResource swaggerResource = new SwaggerResource();
        swaggerResource.setName(route);
        swaggerResource.setLocation("/api-docs?route=" + route);
        swaggerResource.setSwaggerVersion(swaggerVersion);
        return swaggerResource;
    }

}
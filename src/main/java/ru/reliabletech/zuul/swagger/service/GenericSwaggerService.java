package ru.reliabletech.zuul.swagger.service;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.reliabletech.zuul.swagger.exception.NotFoundException;
import ru.reliabletech.zuul.swagger.props.ServicesSwaggerInfo;

/**
 * General implementation
 *
 * @author Alexandr Emelyanov <mr.lex91@gmail.com>
 * on 27.11.2017.
 */
@Component
public class GenericSwaggerService implements SwaggerService {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ServicesSwaggerInfo servicesSwaggerInfo;
    @Autowired
    private RouteService routeService;

    @Override
    public ObjectNode getSwaggerDoc(String route) {
        ObjectNode swaggerDocumentation = getOriginalSwaggerDoc(route);
        swaggerDocumentation.set("host", new TextNode(""));
        swaggerDocumentation.set("basePath", new TextNode(servicesSwaggerInfo.getPrefix() + routeService.getPath(route)));
        return swaggerDocumentation;
    }

    @Override
    public ObjectNode getOriginalSwaggerDoc(String route) {
        String serviceUrl = servicesSwaggerInfo.getServiceUrl(route)
                .orElseGet(() -> servicesSwaggerInfo.getDefaultProtocol() + route);
        String url = String.format("%s/%s",
                serviceUrl,
                servicesSwaggerInfo.getSwaggerUrl(route));
        try {
            return restTemplate.getForObject(url, ObjectNode.class);
        } catch (IllegalStateException e) {
            if (e.getMessage() == null || !e.getMessage().startsWith("No instances available for")) {
                throw e;
            }
            throw new NotFoundException();
        }
    }
}

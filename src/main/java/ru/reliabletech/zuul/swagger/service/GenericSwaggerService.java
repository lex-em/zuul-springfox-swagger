package ru.reliabletech.zuul.swagger.service;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.reliabletech.zuul.swagger.exception.NotFoundException;
import ru.reliabletech.zuul.swagger.props.ServicesSwaggerInfo;

import java.util.Optional;

/**
 * General implementation
 *
 * @author Alexandr Emelyanov <mr.lex91@gmail.com>
 * on 27.11.2017.
 */
@Component
@Slf4j
public class GenericSwaggerService implements SwaggerService {

    @Autowired
    @Qualifier("pureRestTemplate")
    private RestTemplate pureRestTemplate;
    @Autowired
    @Qualifier("loadBalancedRestTemplate")
    private RestTemplate loadBalancedRestTemplate;
    @Autowired
    private ServicesSwaggerInfo servicesSwaggerInfo;
    @Autowired
    private RouteService routeService;

    @Override
    public ObjectNode getSwaggerDoc(String route) {
        ObjectNode swaggerDocumentation = getOriginalSwaggerDoc(route);
        swaggerDocumentation.set("host", new TextNode(""));
        String path = new StringBuilder().append(servicesSwaggerInfo.getPrefix())
                .append("/")
                .append(routeService.getPath(route))
                .toString()
                .replaceAll("[/]+", "/");
        swaggerDocumentation.set("basePath", new TextNode(path));
        return swaggerDocumentation;
    }

    @Override
    public ObjectNode getOriginalSwaggerDoc(String route) {
        Optional<String> serviceUrlOpt = servicesSwaggerInfo.getServiceUrl(route);
        RestTemplate restTemplate = serviceUrlOpt
                .map(x -> pureRestTemplate)
                .orElse(loadBalancedRestTemplate);
        String url = servicesSwaggerInfo.getDirectSwaggerDocUrl(route)
                .orElseGet(() -> {
                    String serviceUrl = serviceUrlOpt
                            .orElseGet(() -> servicesSwaggerInfo.getDefaultProtocol() + route);
                    return String.format("%s/%s",
                            serviceUrl,
                            servicesSwaggerInfo.getSwaggerUrl(route));
                });
        try {
            return restTemplate.getForObject(url, ObjectNode.class);
        } catch (IllegalStateException e) {
            if (e.getMessage() == null || !e.getMessage().startsWith("No instances available for")) {
                throw e;
            }
            log.error("Requested resources URL is wrong", e);
            throw new NotFoundException();
        }
    }
}

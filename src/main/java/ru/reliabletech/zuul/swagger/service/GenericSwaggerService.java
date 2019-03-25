package ru.reliabletech.zuul.swagger.service;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.reliabletech.zuul.swagger.exception.NotFoundException;
import ru.reliabletech.zuul.swagger.props.ServicesSwaggerInfo;
import springfox.documentation.swagger.web.SwaggerResource;

import java.util.List;

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
    public ObjectNode getSwaggerDoc(String route, String group) {
        ObjectNode swaggerDocumentation = getOriginalSwaggerDoc(route, group);
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
    public List<SwaggerResource> getSwaggerResources(String route) {
        String serviceUrl = servicesSwaggerInfo.getServiceUrl(route)
                .orElseGet(() -> servicesSwaggerInfo.getDefaultProtocol() + route);
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(serviceUrl)
                .path(servicesSwaggerInfo.getSwaggerResourcesUrl(route));
        String url =  uriBuilder.build().toUriString();
        try {
            return restTemplate.exchange(url, HttpMethod.GET, HttpEntity.EMPTY, new ParameterizedTypeReference<List<SwaggerResource>>(){})
                    .getBody();
        } catch (IllegalStateException e) {
            if (e.getMessage() == null || !e.getMessage().startsWith("No instances available for")) {
                throw e;
            }
            throw new NotFoundException();
        }
    }

    @Override
    public ObjectNode getOriginalSwaggerDoc(String route, String group) {
        String serviceUrl = servicesSwaggerInfo.getServiceUrl(route)
                .orElseGet(() -> servicesSwaggerInfo.getDefaultProtocol() + route);
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(serviceUrl)
                .path(servicesSwaggerInfo.getSwaggerUrl(route));
        if (group != null) {
            uriBuilder.queryParam("group", group);
        }
        String url =  uriBuilder.build().toUriString();
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

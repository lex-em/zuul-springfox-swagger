package ru.reliabletech.zuul.swagger.service;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
        Optional<String> serviceUrlOpt = servicesSwaggerInfo.getServiceUrl(route);
        RestTemplate restTemplate = getRestTemplate(serviceUrlOpt.isPresent());
        String url = getServiceSwaggerBaseUrlBuilder(route, serviceUrlOpt)
                .path(servicesSwaggerInfo.getSwaggerResourcesUrl(route))
                .build()
                .toUriString();
        try {
            return restTemplate.exchange(url, HttpMethod.GET, HttpEntity.EMPTY, new ParameterizedTypeReference<List<SwaggerResource>>(){})
                    .getBody();
        } catch (IllegalStateException e) {
            log.error("Some unexpected error while requesting swagger resources from: {}", url);
            if (e.getMessage() == null || !e.getMessage().startsWith("No instances available for")) {
                throw e;
            }
            throw new NotFoundException(e);
        }
    }

    @Override
    public ObjectNode getOriginalSwaggerDoc(String route, String group) {
        Optional<String> serviceUrlOpt = servicesSwaggerInfo.getServiceUrl(route);
        RestTemplate restTemplate = getRestTemplate(serviceUrlOpt.isPresent());
        UriComponentsBuilder uriBuilder = getServiceSwaggerBaseUrlBuilder(route, serviceUrlOpt)
                .path(servicesSwaggerInfo.getSwaggerUrl(route));
        if (group != null) {
            uriBuilder.queryParam("group", group);
        }
        String url =  uriBuilder.build().toUriString();
        try {
            return restTemplate.getForObject(url, ObjectNode.class);
        } catch (IllegalStateException e) {
            log.error("Some unexpected error while requesting swagger docs from: {}", url);
            if (e.getMessage() == null || !e.getMessage().startsWith("No instances available for")) {
                throw e;
            }
            throw new NotFoundException(e);
        }
    }

    private RestTemplate getRestTemplate(boolean isUrlBased) {
        if (isUrlBased) {
            return pureRestTemplate;
        }
        return loadBalancedRestTemplate;
    }

    private UriComponentsBuilder getServiceSwaggerBaseUrlBuilder(String route, Optional<String> serviceUrlOpt) {
        String serviceUrl = servicesSwaggerInfo.getDirectSwaggerBaseUrl(route)
                .orElseGet(() -> serviceUrlOpt.orElseGet(() -> servicesSwaggerInfo.getProtocol(route) + route));
        return UriComponentsBuilder.fromHttpUrl(serviceUrl);
    }
}

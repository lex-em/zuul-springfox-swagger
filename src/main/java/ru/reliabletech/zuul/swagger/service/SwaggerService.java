package ru.reliabletech.zuul.swagger.service;

import com.fasterxml.jackson.databind.node.ObjectNode;
import springfox.documentation.swagger.web.SwaggerResource;

import java.util.List;

/**
 * Service for operations over connected services swagger documentations
 *
 * @author Alexandr Emelyanov <mr.lex91@gmail.com>
 * on 27.11.2017.
 */
public interface SwaggerService {

    /**
     * Obtain original documentation of service, represented by route
     *
     * @param route
     * @param group
     * @return
     */
    ObjectNode getOriginalSwaggerDoc(String route, String group);

    /**
     * Obtain modified for proxy's swagger-ui documentation of service, represented by route
     *
     * @param route
     * @param group
     * @return
     */
    ObjectNode getSwaggerDoc(String route, String group);

    /**
     * Requesting swagger resources list from route
     *
     * @param route
     * @return
     */
    List<SwaggerResource> getSwaggerResources(String route);

}

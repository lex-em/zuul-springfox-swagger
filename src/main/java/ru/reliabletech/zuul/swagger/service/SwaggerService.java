package ru.reliabletech.zuul.swagger.service;

import com.fasterxml.jackson.databind.node.ObjectNode;

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
     * @return
     */
    ObjectNode getOriginalSwaggerDoc(String route);

    /**
     * Obtain modified for proxy's swagger-ui documentation of service, represented by route
     *
     * @param route
     * @return
     */
    ObjectNode getSwaggerDoc(String route);

    /**
     * Obtain swagger documentation version for service, represented by route
     *
     * @param route
     * @return
     */
    default String getSwaggerVersion(String route) {
        ObjectNode swaggerDocumentation = getOriginalSwaggerDoc(route);
        return swaggerDocumentation == null ? "" : swaggerDocumentation.get("swagger").asText();
    }

}

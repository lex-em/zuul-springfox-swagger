package ru.reliabletech.zuul.swagger.service;

import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * @author Alexandr Emelyanov <mr.lex91@gmail.com>
 * on 27.11.2017.
 */
public interface SwaggerService {

    ObjectNode getOriginalSwaggerDoc(String route);

    ObjectNode getSwaggerDoc(String route);

    default String getSwaggerVersion(String route) {
        ObjectNode swaggerDocumentation = getOriginalSwaggerDoc(route);
        return swaggerDocumentation == null ? "" : swaggerDocumentation.get("swagger").asText();
    }

}

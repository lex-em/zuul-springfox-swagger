package ru.reliabletech.zuul.swagger;

import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * @author Alexandr Emelyanov <mr.lex91@gmail.com>
 * on 27.11.2017.
 */
public interface SwaggerService {

    ObjectNode getSwaggerDoc(String route);

}

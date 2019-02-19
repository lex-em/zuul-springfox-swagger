package ru.reliabletech.zuul.swagger.controller;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.reliabletech.zuul.swagger.service.SwaggerService;

/**
 * Controller for swagger-ui documentation requests
 *
 * @author Alexandr Emelyanov <mr.lex91@gmail.com>
 * on 27.11.2017.
 */
@RestController
public class ZuulSwaggerController {

    @Autowired
    private SwaggerService swaggerService;

    @GetMapping("/api-docs")
    public JsonNode getApiDocs(@RequestParam("route") String route) {
        return swaggerService.getSwaggerDoc(route);
    }
}

package ru.reliabletech.zuul.swagger;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @author Alexandr Emelyanov <mr.lex91@gmail.com>
 * on 27.11.2017.
 */
@RestController
public class ZuulSwaggerController {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ServicesSwaggerInfo servicesSwaggerInfo;
    @Autowired
    private SwaggerService swaggerService;

    @GetMapping("/api-docs")
    public JsonNode getApiDocs(@RequestParam("route") String route) {
        String path = servicesSwaggerInfo.getServicePath(route)
                                         .orElseThrow(NotFoundException::new)
                                         .replace("/**", "");
        ObjectNode swaggerDocumentation = swaggerService.getSwaggerDoc(route);
        swaggerDocumentation.set("host", new TextNode(""));
        swaggerDocumentation.set("basePath", new TextNode(servicesSwaggerInfo.getPrefix() + path));
        return swaggerDocumentation;
    }
}

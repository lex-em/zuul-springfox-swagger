package ru.reliabletech.zuul.swagger;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * @author Alexandr Emelyanov <mr.lex91@gmail.com>
 * on 27.11.2017.
 */
@Component
public class GenericSwaggerService implements SwaggerService {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ServicesSwaggerInfo servicesSwaggerInfo;

    @Override
    public ObjectNode getSwaggerDoc(String route) {
        return restTemplate.getForObject("{service-url}/{doc-url}",
                                         ObjectNode.class,
                                         servicesSwaggerInfo.getServiceUrl(route),
                                         servicesSwaggerInfo.getSwaggerUrl(route));
    }
}

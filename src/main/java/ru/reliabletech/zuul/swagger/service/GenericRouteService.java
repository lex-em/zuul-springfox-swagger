package ru.reliabletech.zuul.swagger.service;

import org.springframework.beans.factory.annotation.Autowired;
import ru.reliabletech.zuul.swagger.props.ServicesSwaggerInfo;

/**
 * General implementation
 *
 * @author Alexandr Emelyanov <mr.lex91@gmail.com>
 * on 15.02.19.
 */
public class GenericRouteService implements RouteService {

    @Autowired
    private ServicesSwaggerInfo servicesSwaggerInfo;

    @Override
    public String getPath(String route) {
        return servicesSwaggerInfo
                .getServicePath(route)
                .orElse(route);
    }
}

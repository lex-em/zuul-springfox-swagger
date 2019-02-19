package ru.reliabletech.zuul.swagger.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.discovery.ServiceRouteMapper;
import ru.reliabletech.zuul.swagger.props.ServicesSwaggerInfo;

/**
 * Realization? based on {@link ServiceRouteMapper }
 *
 * @author Alexandr Emelyanov <mr.lex91@gmail.com>
 * on 15.02.19.
 */
public class ServiceRouteMapperRouteService implements RouteService {

    @Autowired
    private ServicesSwaggerInfo servicesSwaggerInfo;
    @Autowired
    private ServiceRouteMapper serviceRouteMapper;

    @Override
    public String getPath(String route) {
        return servicesSwaggerInfo
                .getServicePath(route)
                .orElseGet(() -> serviceRouteMapper.apply(route));
    }
}

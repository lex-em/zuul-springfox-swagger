package ru.reliabletech.zuul.swagger.service;

import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.cloud.netflix.zuul.filters.discovery.ServiceRouteMapper;
import org.springframework.stereotype.Component;
import ru.reliabletech.zuul.swagger.props.ServicesSwaggerInfo;

/**
 * @author Alexandr Emelyanov <mr.lex91@gmail.com>
 * on 15.02.19.
 */
@Component
@ConditionalOnBean(ServiceRouteMapper.class)
@AllArgsConstructor
public class VersionedPatternRouteService implements RouteService {

    private ServicesSwaggerInfo servicesSwaggerInfo;
    private ServiceRouteMapper serviceRouteMapper;

    @Override
    public String getPath(String route) {
        return servicesSwaggerInfo.getServicePath(route)
                .orElseGet(() -> serviceRouteMapper.apply(route));
    }
}

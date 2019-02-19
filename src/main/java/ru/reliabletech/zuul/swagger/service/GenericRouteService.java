package ru.reliabletech.zuul.swagger.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;
import ru.reliabletech.zuul.swagger.props.ServicesSwaggerInfo;

/**
 * @author Alexandr Emelyanov <mr.lex91@gmail.com>
 * on 15.02.19.
 */
@ConditionalOnMissingBean(RouteService.class)
@Component
@AllArgsConstructor
public class GenericRouteService implements RouteService {

    private ServicesSwaggerInfo servicesSwaggerInfo;

    @Override
    public String getPath(String route) {
        return servicesSwaggerInfo
                .getServicePath(route)
                .orElse(route);
    }
}

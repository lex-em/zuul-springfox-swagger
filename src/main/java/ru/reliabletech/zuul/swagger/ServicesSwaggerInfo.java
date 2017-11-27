package ru.reliabletech.zuul.swagger;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * @author Alexandr Emelyanov <mr.lex91@gmail.com>
 * on 27.11.2017.
 */
@ConfigurationProperties(prefix = "zuul")
@Data
public class ServicesSwaggerInfo {

    public static final String DEFAULT_SWAGGER_API_URL = "v2/api-docs";

    private String prefix;

    private String defaultSwaggerUrl = DEFAULT_SWAGGER_API_URL;

    private Map<String, ServiceInfo> routes = new HashMap<>();

    public Set<String> getRouteNames() {
        return routes.keySet();
    }

    public String getSwaggerUrl(String route) {
        return Optional.ofNullable(routes.get(route))
                       .map(ServiceInfo::getSwaggerUrl)
                       .orElse(defaultSwaggerUrl);
    }

    public String getServiceUrl(String route) {
        return Optional.ofNullable(routes.get(route))
                       .map(ServiceInfo::getServiceUrl)
                       .orElse("");
    }

    public Optional<String> getServicePath(String route) {
        return Optional.ofNullable(routes.get(route))
                       .map(ServiceInfo::getPath);
    }
}
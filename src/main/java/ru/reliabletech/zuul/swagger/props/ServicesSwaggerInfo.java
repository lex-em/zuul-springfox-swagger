package ru.reliabletech.zuul.swagger.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Extended Zuul configuration for plugin purposes
 *
 * @author Alexandr Emelyanov <mr.lex91@gmail.com>
 * on 27.11.2017.
 */
@ConfigurationProperties(prefix = "zuul")
@Data
public class ServicesSwaggerInfo {

    public static final String DEFAULT_SWAGGER_API_URL = "v2/api-docs";
    public static final String PROTOCOL_DEFAULT = "http://";

    private String prefix;

    private String defaultSwaggerUrl = DEFAULT_SWAGGER_API_URL;

    private String defaultProtocol = PROTOCOL_DEFAULT;

    private Map<String, ServiceInfo> routes = new HashMap<>();

    public Set<String> getRouteNames() {
        return routes.keySet();
    }

    public String getSwaggerUrl(String route) {
        return Optional.ofNullable(routes.get(route))
                .map(ServiceInfo::getSwaggerUri)
                .orElse(defaultSwaggerUrl);
    }

    public String getDefaultProtocol(String route) {
        return Optional.ofNullable(routes.get(route))
                .map(ServiceInfo::getProtocol)
                .orElse(defaultProtocol);
    }

    public Optional<String> getServiceUrl(String route) {
        return Optional.ofNullable(routes.get(route))
                .map(ServiceInfo::getUrl)
                .map(url -> String.format("%s%s/", getDefaultProtocol(route), url));
    }

    public Optional<String> getServicePath(String route) {
        return Optional.ofNullable(routes.get(route))
                .map(ServiceInfo::getPath)
                .map(path -> path.replaceAll("^/", "").replaceAll("/\\*\\*", ""));
    }

    public Optional<String> getDirectSwaggerBaseUrl(String route) {
        return Optional.ofNullable(routes.get(route))
                .map(ServiceInfo::getDirectSwaggerBaseUrl);
    }
}
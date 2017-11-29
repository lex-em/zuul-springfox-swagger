package ru.reliabletech.zuul.swagger;

import lombok.Data;
import org.springframework.util.StringUtils;

import java.util.Optional;

/**
 * @author Alexandr Emelyanov <mr.lex91@gmail.com>
 * on 27.11.2017.
 */
@Data
public class ServiceInfo {

    private static final String PROTOCOL_DEFAULT = "http://";
    private String path;
    private String serviceId;
    private String url;
    private String swaggerUrl;
    private String protocol = PROTOCOL_DEFAULT;

    public String getServiceUrl() {
        return Optional.ofNullable(url)
                       .filter(url -> !StringUtils.isEmpty(url))
                       .orElseGet(() -> String.format("%s%s/", protocol, serviceId));
    }
}
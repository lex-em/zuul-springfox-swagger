package ru.reliabletech.zuul.swagger.props;

import lombok.Data;

import java.util.Set;

/**
 * Service info
 *
 * @author Alexandr Emelyanov <mr.lex91@gmail.com>
 * on 27.11.2017.
 */
@Data
class ServiceInfo {

    private String path;

    private String serviceId;

    private String url;

    private String swaggerUri;

    private String directSwaggerBaseUrl;

    private String directSwaggerPath;

    private String swaggerResourcesUri;

    private String protocol;

    private Set<String> allowedGroups;

    public boolean groupAllowed(String group) {
        return allowedGroups == null || allowedGroups.isEmpty() || allowedGroups.contains(group);
    }

}
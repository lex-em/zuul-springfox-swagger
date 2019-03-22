package ru.reliabletech.zuul.swagger.props;

import lombok.Data;

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

    private String protocol = "";

}
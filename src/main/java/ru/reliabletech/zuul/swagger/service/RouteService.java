package ru.reliabletech.zuul.swagger.service;

/**
 * Service for mapping routes to proxy path
 *
 * @author Alexandr Emelyanov <mr.lex91@gmail.com>
 * on 15.02.19.
 */
public interface RouteService {

    /**
     * Perform route mapping to proxy path
     *
     * @param route
     * @return
     */
    String getPath(String route);
}

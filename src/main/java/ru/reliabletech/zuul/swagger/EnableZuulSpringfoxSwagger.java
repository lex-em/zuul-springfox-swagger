package ru.reliabletech.zuul.swagger;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * Enabling ZuulSpringfoxSwaggerPlugin annotation.
 *
 * @author Alexandr Emelyanov <mr.lex91@gmail.com>
 * on 27.11.2017.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
@Import(ZuulSpringfoxSwaggerConfiguration.class)
public @interface EnableZuulSpringfoxSwagger {

}

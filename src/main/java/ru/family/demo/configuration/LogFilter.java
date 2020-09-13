package ru.family.demo.configuration;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import ru.family.demo.services.AuthUtilsService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@RequiredArgsConstructor
public class LogFilter extends ZuulFilter {

    private final AuthUtilsService service;

    @Value("${zuul.routes.auth.url}")
    private String AUTH_URL;

    @Value("${zuul.routes.core.url}")
    private String CORE_SERVICE_URL;

    private static final Logger LOG = LoggerFactory.getLogger(LogFilter.class);

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        HttpServletResponse response = ctx.getResponse();
        response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
        response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "GET, POST, DELETE, PUT");
        response.setHeader(HttpHeaders.ACCESS_CONTROL_MAX_AGE, "3600");
        response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, "*");
        ctx.addZuulRequestHeader("X-FAMILY-APP-ID", "FAMILY");
        LOG.info(String.format("%s request to %s", request.getMethod(), request.getRequestURL().toString()));

        LOG.info(">> Authenticated");
        LOG.info("-------------------------------------------------------------------------------------------");
        LOG.info(String.format(">> Begin request: %s: %s", request.getMethod(), request.getRequestURI()));
        LOG.info(String.format(">> Headers: %s", service.parseHeaders(request.getHeaderNames(), request)));

        var URI = request.getRequestURI();
        if (URI.contains("auth")) {
            LOG.warn(String.format(">> Redirecting to : %s", AUTH_URL));
        } else if (URI.contains("task-provider")) {
            LOG.warn(String.format(">> Redirecting to : %s", CORE_SERVICE_URL));
        }
        LOG.info(String.format(">> End request: %s", request.getMethod()));
        LOG.info("-------------------------------------------------------------------------------------------");
        return null;
    }
}


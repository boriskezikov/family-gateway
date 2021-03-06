package ru.family.demo.configuration;

import com.netflix.zuul.exception.ZuulException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.family.demo.utils.ServiceUnavailableException;

import java.net.SocketTimeoutException;

@ControllerAdvice
public class ExceptionHandlerInterceptor extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {ZuulException.class, SocketTimeoutException.class})
    protected ResponseEntity<Object> handle(Exception ex, WebRequest request) {
        throw new ServiceUnavailableException("Sorry, heroku issues:)");
    }

}

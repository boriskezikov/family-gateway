package ru.family.demo.services;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;


@Service
@NoArgsConstructor
public class AuthUtilsService {

    public Map<String, String> parseHeaders(Enumeration<String> headersNames, HttpServletRequest httpRequest) {

        Map<String, String> headers = new HashMap<>();

        if (headersNames != null) {
            while (headersNames.hasMoreElements()) {
                var next = headersNames.nextElement();
                headers.put(next, httpRequest.getHeader(next));
            }
        }
        return headers;
    }
}


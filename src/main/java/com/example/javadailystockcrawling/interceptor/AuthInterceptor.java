package com.example.javadailystockcrawling.interceptor;

import com.example.javadailystockcrawling.annotation.Auth;
import com.example.javadailystockcrawling.exception.AuthException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import java.util.Enumeration;

@Component
@Slf4j
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        boolean hasAnnotation = checkAnnotation(handler, Auth.class);

        if (hasAnnotation) {
            Enumeration<String> crawlingUserHeaders = request.getHeaders("x-crawling-user");
            while (crawlingUserHeaders.hasMoreElements()) {
                String crawlingUserHeader = crawlingUserHeaders.nextElement();
                log.info("crawlingUserHeader : {}", crawlingUserHeader);
                if (crawlingUserHeader.equals("buru")) {
                    return true;
                }
            }

            throw new AuthException();
        }

        return true;
//        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    // @Auth 어노테이션 있는 여부 화인
    private boolean checkAnnotation(Object handler, Class<Auth> authClass) {
        // resource javascript, html .....
        if (handler instanceof ResourceHttpRequestHandler) {
            return true;
        }

        // @Auth 어노테이션 체크
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        if (null != handlerMethod.getMethodAnnotation(authClass) || null != handlerMethod.getBeanType().getAnnotation(authClass)) {
            return true;
        }

        return false;
    }
}

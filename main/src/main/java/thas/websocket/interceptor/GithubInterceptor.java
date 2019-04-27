package thas.websocket.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import thas.websocket.util.RenderUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author thas
 * @date 2019/4/26 22:17
 */
@Component
@Slf4j
public class GithubInterceptor implements HandlerInterceptor {

    @Value("${github.secret}")
    private String secret;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("收到来自github的请求");
        String userAgent = request.getHeader("User-Agent");
        if(userAgent.startsWith("GitHub-Hookshot/")){
           return true;
        }
        log.info("User-Agent不匹配");
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        RenderUtil.renderJson(response,"User-Agent不匹配");
        return false;
    }


}

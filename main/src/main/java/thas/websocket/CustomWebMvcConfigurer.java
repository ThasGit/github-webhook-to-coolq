package thas.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import thas.websocket.interceptor.GithubInterceptor;

/**
 * @author thas
 * @date 2019/4/26 22:19
 */
@Component
public class CustomWebMvcConfigurer implements WebMvcConfigurer {

    @Autowired
    private GithubInterceptor githubInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(githubInterceptor).addPathPatterns("/github");
    }
}

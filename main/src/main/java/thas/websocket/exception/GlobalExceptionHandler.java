package thas.websocket.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author thas
 * @date 2019/4/27 20:05
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(GithubSignCheckFailedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public String githubSignCheckFailedException(GithubSignCheckFailedException e){
        return e.getMessage();
    }
}

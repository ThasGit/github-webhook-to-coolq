package thas.websocket.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * @author thas
 * @date 2019/4/27 19:52
 */
@Setter
@Getter
public class GithubSignCheckFailedException extends RuntimeException {

    private String expected;

    private String actual;

    public GithubSignCheckFailedException(String expected,String actual){
        this.expected = expected;
        this.actual = actual;
    }

    public GithubSignCheckFailedException(){
    }

    @Override
    public String getMessage() {
        if(actual == null){
            return "请求中未携带签名";
        }else{
            return "签名验证失败，actual:" + actual + ",expect:" + expected;
        }
    }
}

package thas.websocket.coolq;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import thas.websocket.coolq.dto.CoolqResponse;

import java.util.HashMap;
import java.util.Map;

/**
 * @author thas
 * @date 2019/4/26 21:51
 */
@Component("httpClient")
@Primary
public class CoolqHttpClient implements CoolqService {

    private RestTemplate restTemplate = new RestTemplate();

    @Value("${coolq.http_url}")
    private String baseUrl;

    @Override
    public CoolqResponse sendPrivateMsg(long to, String msg) {
        Map<String,Object> params = new HashMap<>(4);
        params.put("user_id",to);
        params.put("message",msg);
        return post("/send_private_msg", params, CoolqResponse.class);
    }

    private <T> T post(String url,Object params,Class<T> clazz){
        ResponseEntity<T> coolqResponseResponseEntity = restTemplate.postForEntity(baseUrl + url, params, clazz);
        if(coolqResponseResponseEntity.getStatusCode() == HttpStatus.OK){
            return coolqResponseResponseEntity.getBody();
        }
        return null;
    }
}

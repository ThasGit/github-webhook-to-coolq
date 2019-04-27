package thas.websocket.converter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import thas.websocket.controller.dto.GithubEventMessage;
import thas.websocket.exception.GithubSignCheckFailedException;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.List;

/**
 * @author thas
 * @date 2019/4/27 18:34
 */
@Component
@Slf4j
public class GithubMessageConverter extends FastJsonHttpMessageConverter {

    @Value("${github.secret}")
    private String secret;

    @Override
    public Object read(Type type, Class<?> contextClass, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        InputStream in = inputMessage.getBody();
        byte[] bytes = streamToBytes(in);
        if(type instanceof Class && GithubEventMessage.class.isAssignableFrom((Class<?>) type)){

            List<String> strings = inputMessage.getHeaders().get("X-Hub-Signature");
            if(strings == null || strings.size() == 0 || strings.get(0).length() < 5){
                log.info("请求中未携带签名");
                throw new GithubSignCheckFailedException();
            }else{
                StopWatch stopWatch = new StopWatch();
                stopWatch.start();
                String expect = new HmacUtils(HmacAlgorithms.HMAC_SHA_1,secret).hmacHex(bytes);
                stopWatch.stop();
                log.info("验证签名总时长：" + stopWatch.getTotalTimeMillis());
                String sig = strings.get(0).substring(5);
                if(!StringUtils.equalsIgnoreCase(expect,sig)){
                    log.info("签名验证失败，actual:" + sig + ",expected:" + expect);
                    throw new GithubSignCheckFailedException(expect,sig);
                }
            }
            log.info("签名验证通过");
        }
        return JSON.parseObject(bytes,type,super.getFastJsonConfig().getFeatures());
    }

    private byte[] streamToBytes(InputStream inputStream) throws IOException {
        byte[] bytes = new byte[1024 * 64];
        int offset = 0;
        for (;;) {
            int readCount = inputStream.read(bytes, offset, bytes.length - offset);
            if (readCount == -1) {
                break;
            }
            offset += readCount;
            if (offset == bytes.length) {
                byte[] newBytes = new byte[bytes.length * 3 / 2];
                System.arraycopy(bytes, 0, newBytes, 0, bytes.length);
                bytes = newBytes;
            }
        }
        byte[] newBytes = new byte[offset];
        System.arraycopy(bytes, 0, newBytes, 0, offset);
        return newBytes;
    }
}

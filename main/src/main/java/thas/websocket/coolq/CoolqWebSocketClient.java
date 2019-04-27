package thas.websocket.coolq;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import thas.websocket.coolq.dto.CoolqRequest;
import thas.websocket.coolq.dto.CoolqResponse;

import javax.security.auth.Destroyable;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author thas
 * @date 2019/4/25 21:43
 */
@Component
@Slf4j
public class CoolqWebSocketClient implements InitializingBean, Destroyable, CoolqService,AsyncCoolqService {

    private WebSocketClient webSocketClient;

    private Lock lock = new ReentrantLock(true);

    private Condition condition = lock.newCondition();

    private CoolqResponse syncResult;

    @Override
    public void sendPrivateMsgAsync(long to,String msg){
        CoolqRequest coolqRequest = new CoolqRequest();
        coolqRequest.setAction("send_private_msg");
        Map<String,Object> params = new HashMap<>(4);
        params.put("user_id",to);
        params.put("message",msg);
        coolqRequest.setParams(params);
        String request = JSON.toJSONString(coolqRequest);
        log.info("发送私信给" + to + "：" + request);
        webSocketClient.send(request);
    }

    @Override
    public CoolqResponse sendPrivateMsg(long to,String msg){
        try{
            lock.lock();
            sendPrivateMsgAsync(to,msg);
            try {
                condition.await(3000, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                log.info("等待响应结果超时",e);
            }
            return syncResult;
        }finally {
            lock.unlock();
        }
    }

    @Override
    public void afterPropertiesSet(){
        initWebSocketClient();
    }

    private void initWebSocketClient(){
        try{
            WebSocketClient client = new WebSocketClient(new URI("ws://ali.thas.cc:9003/"),new Draft_6455()) {
                @Override
                public void onOpen(ServerHandshake serverHandshake) {
                    log.info("建立与coolq服务器的websocket连接");
                }

                @Override
                public void onMessage(String s) {
                    try{
                        lock.lock();
                        log.info("收到消息" + s);
                        if(!s.contains("message_type")&&s.contains("retcode")){
                            syncResult = JSON.parseObject(s, CoolqResponse.class);
                            condition.signal();
                        }
                    }finally {
                        lock.unlock();
                    }
                }

                @Override
                public void onClose(int i, String s, boolean b) {
                    log.info("与coolq服务器的websocket连接已断开");
                }

                @Override
                public void onError(Exception e) {
                log.error("与coolq服务器的websocket连接发生错误",e);
                    log.info("3s后重新尝试初始化websocket连接");
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException ex) {
                        log.error("与coolq服务器的websocket重连过程发生InterruptedException",e);
                    }
                    initWebSocketClient();
                }
            };
            client.connectBlocking();
            this.webSocketClient = client;
        }catch (Exception e) {
            log.error("无法初始化websocket连接",e);
        }
    }

    @Override
    public void destroy(){
        webSocketClient.close();
    }
}

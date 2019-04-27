package thas.websocket.coolq;

/**
 * @author thas
 * @date 2019/4/26 21:47
 */
public interface AsyncCoolqService {

    void sendPrivateMsgAsync(long to,String msg);
}

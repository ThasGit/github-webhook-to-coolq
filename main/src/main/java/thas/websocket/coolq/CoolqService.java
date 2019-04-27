package thas.websocket.coolq;

import thas.websocket.coolq.dto.CoolqResponse;

/**
 * @author thas
 * @date 2019/4/26 21:47
 */
public interface CoolqService {

    CoolqResponse sendPrivateMsg(long to, String msg);
}

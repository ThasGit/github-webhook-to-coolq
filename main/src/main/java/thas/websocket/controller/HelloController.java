package thas.websocket.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import thas.websocket.coolq.dto.CoolqResponse;
import thas.websocket.coolq.CoolqService;

/**
 * @author thas
 * @date 2019/4/25 22:56
 */
@RestController
public class HelloController {

    @Autowired
    private CoolqService coolqService;

    @RequestMapping("send_private_msg")
    public CoolqResponse hello(String msg){
        CoolqResponse response = coolqService.sendPrivateMsg(1308683451,msg);
        return response;
    }

}

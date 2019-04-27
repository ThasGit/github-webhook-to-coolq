package thas.websocket.coolq.dto;

import lombok.Data;

/**
 * @author thas
 * @date 2019/4/25 21:55
 */
@Data
public class CoolqResponse {

    private String status;

    private int retcode;

    private Object data;
}

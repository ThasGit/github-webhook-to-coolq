package thas.websocket.coolq.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author thas
 * @date 2019/4/25 21:53
 */
@Data
public class CoolqRequest implements Serializable {

    private String action;

    private Object params;
}

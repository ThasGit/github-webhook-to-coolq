package thas.websocket.controller.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author thas
 * @date 2019/4/27 11:27
 */
@Data
public class GithubPushEventMessageVO implements GithubEventMessage {

    private String ref;

    private String compare;

    private List<Map<String,Object>> commits;

    private Map<String,Object> headCommit;

    private Map<String,Object> repository;

    private Map<String,Object> pusher;

    private Map<String,Object> sender;
}

package thas.websocket.controller;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import thas.websocket.controller.dto.GithubPushEventMessageVO;
import thas.websocket.coolq.CoolqService;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author thas
 * @date 2019/4/26 22:08
 */
@RestController
@RequestMapping("github")
@Slf4j
public class GithubController {

    @Autowired
    private CoolqService coolqService;

    @RequestMapping(value = "",headers = {"X-GitHub-Event=push"})
    @ResponseBody
    public ResponseEntity<?> event(
            @RequestBody GithubPushEventMessageVO messageVO
    ){
        String pusherName = (String) messageVO.getPusher().get("name");
        String repositoryName = (String) messageVO.getRepository().get("full_name");
        String ref = messageVO.getRef();
        Map<String,Object> result = new LinkedHashMap<>(8);
        result.put("title",pusherName + "推送了新提交到仓库" + repositoryName + "分支" + ref);
        String pusherEmail = (String) messageVO.getPusher().get("email");
        result.put("pusher",pusherEmail);
        String compare = messageVO.getCompare();
        result.put("compare",compare);
        List<Map<String, Object>> commits = messageVO.getCommits();
        List<String> commitMessages = commits.stream().map(stringObjectMap -> (String)stringObjectMap.get("message")).collect(Collectors.toList());
        result.put("commits",commitMessages);
        coolqService.sendPrivateMsg(1308683451, JSON.toJSONString(result));
        return ResponseEntity.ok().build();
    }
}

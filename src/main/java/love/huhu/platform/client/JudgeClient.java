package love.huhu.platform.client;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import love.huhu.platform.authorization.UserHolder;
import love.huhu.platform.domain.AnswerRecord;
import love.huhu.platform.domain.JudgeMachine;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

/**
 * @Description
 * @Author nwl
 * @Create 2023-02-25 下午2:59
 */
@Component
@RequiredArgsConstructor
public class JudgeClient {

    private List<JudgeMachine> judgeMachines;
    @Value("${api.manager-server}")
    String managerServerApi;
    private final ManagerClient managerClient;
    public void judge(AnswerRecord record) {
        //获取判题机
//        if (judgeMachines == null || judgeMachines.isEmpty()) {
        judgeMachines = managerClient.getEnabledJudgeMachine();
//        }
        //随机获取判题机
        JudgeMachine judgeMachine = judgeMachines.get(new Double(Math.floor(Math.random() * judgeMachines.size())).intValue());

        //发送判题请求

        String result = HttpRequest.post(judgeMachine.getUrl() + "/v1/judge")
                .body(JSONUtil.toJsonStr(record))
                .execute().body();
        //结果封装回record
        AnswerRecord resultRecord = JSONUtil.toBean(result, AnswerRecord.class);
        record.copy(resultRecord);
        //todo 保存record到数据库

        record.setUserId(UserHolder.getUserId());

    }

    public AnswerRecord judgeTest(String code, String input) {
        //获取判题机
//        if (judgeMachines == null || judgeMachines.isEmpty()) {
        judgeMachines = managerClient.getEnabledJudgeMachine();

//        }
        //随机获取判题机
        JudgeMachine judgeMachine = judgeMachines.get(new Double(Math.floor(Math.random() * judgeMachines.size())).intValue());
        judgeMachine.setUrl("http://127.0.0.1:8888");
        //包装请求
        HashMap<String, String> map = new HashMap<>();
        map.put("code",code);
        map.put("input",input);
        //发送判题请求
        String result = HttpRequest.post(judgeMachine.getUrl() + "/api/v1/test")
                .body(JSONUtil.parseObj(map).toString())
                .execute().body();
        AnswerRecord answerRecord = dealResult(result);
        return answerRecord;
    }

    private AnswerRecord dealResult(String result) {
        JSONObject temp = JSONUtil.parseObj(result);
        String error = temp.get("Error", String.class);
        String log = temp.get("Log",String.class);
        AnswerRecord record = new AnswerRecord();
        record.setError(error);
        record.setLog(log);
        return record;
    }

}

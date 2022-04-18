package com.wisdri.epms.Util;

import com.wisdri.epms.Entity.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class LogUtil {

    /**
     * 创建一条log日志
     * @return
     */
    public Operation CreateOneLog(String personid, String ip, String action, String operateTime, int successFlag){
        Operation operation = new Operation();
        operation.setPersonid(personid);
        operation.setIp(ip);
        operation.setAction(action);
        operation.setOperateTime(operateTime);
        operation.setSuccessFlag(successFlag);
        return operation;
    }
}

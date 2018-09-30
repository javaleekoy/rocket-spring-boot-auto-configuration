package com.pd.app.service;

import com.pd.app.dto.Demo;
import com.pd.app.utils.ProducerUtils;
import org.apache.rocketmq.common.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author peramdy on 2018/9/29.
 */
@Service
public class SendMsgService {
    @Autowired
    private ProducerUtils producerUtils;


    public void sendMsg() {
        Demo demo = new Demo();
        demo.setId(1);
        demo.setName("demo");
        demo.setTime(new Date());
        Message msg = producerUtils.getMsg("starter-msg", "1001", null, null, demo);
        producerUtils.syncSendMsg(msg);
    }


}

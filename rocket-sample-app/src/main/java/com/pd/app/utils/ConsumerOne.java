package com.pd.app.utils;

import com.pd.app.dto.Demo;
import com.pd.rt.annotation.RtMQConsumer;
import com.pd.rt.base.AbstractRtMqPushConsumer;

import java.util.Map;

/**
 * @author peramdy on 2018/9/30.
 */
@RtMQConsumer(groupName = "${rt.consume.groupNme}", topic = "starter-msg")
public class ConsumerOne extends AbstractRtMqPushConsumer<Demo> {

    @Override
    public boolean doAfterConsumeMsg(Demo msg, Map<String, Object> extMap) {
        System.out.println("consume: " + msg.toString());
        return true;
    }
}

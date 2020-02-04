package com.pd.app.utils;

import com.pd.rt.annotation.RtMQProducer;
import com.pd.rt.base.AbstractRtMqProducer;
import com.pd.rt.builder.MessageBuilder;
import com.pd.rt.exception.RtException;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;

/**
 * @author peramdy on 2018/9/29.
 */
@RtMQProducer
public class ProducerUtils extends AbstractRtMqProducer {

    /**
     * 同步发送消息
     *
     * @param message
     * @throws RtException
     */
    @Override
    public void syncSendMsg(Message message) throws RtException {
        super.syncSendMsg(message);
    }

    /**
     * 发送消息后的操作
     *
     * @param message
     * @param sendResult
     */
    @Override
    public void doAfterSyncSendMsg(Message message, SendResult sendResult) {
        System.out.println("msgId : " + sendResult.getMsgId());
        System.out.println("msgBody : " + message.getBody().toString());
        super.doAfterSyncSendMsg(message, sendResult);
    }


    /**
     * 消息封装
     *
     * @param topic
     * @param tag
     * @param key
     * @param delayLevel
     * @param msg
     * @return
     */
    public Message getMsg(String topic, String tag, String key, Integer delayLevel, Object msg) {
        MessageBuilder builder = new MessageBuilder(topic, tag);
        if (StringUtils.isNotBlank(key)) {
            builder.key(key);
        }
        if (delayLevel != null) {
            builder.delayLevel(delayLevel);
        }
        if (msg == null) {
            return null;
        }
        builder.msg(msg);
        return builder.build();
    }

}

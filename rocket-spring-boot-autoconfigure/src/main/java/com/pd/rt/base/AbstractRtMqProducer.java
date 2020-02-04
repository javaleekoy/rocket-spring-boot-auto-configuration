package com.pd.rt.base;

import com.pd.rt.builder.MessageBuilder;
import com.pd.rt.exception.RtException;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;

/**
 * @author peramdy on 2018/9/28.
 */
public abstract class AbstractRtMqProducer {

    private static Logger logger = LoggerFactory.getLogger(AbstractRtMqProducer.class);

    @Autowired(required = false)
    private DefaultMQProducer defaultMQProducer;


    /**
     * synchronized send msg
     *
     * @param message
     */
    public void syncSendMsg(Message message) throws RtException {
        try {
            SendResult sendResult = defaultMQProducer.send(message);
            if (sendResult.getSendStatus().equals(SendStatus.SEND_OK)) {
                logger.debug("sync send msg ,messageId : {}", sendResult.getMsgId());
            }
            doAfterSyncSendMsg(message, sendResult);
        } catch (Exception e) {
            logger.error("sync send msg error, topic : {}, message : {}", message.getTopic(), message.toString());
            throw new RtException(e.getMessage());
        }
    }

    /**
     * synchronized send msg
     *
     * @param topic
     * @param tag
     * @param key
     * @param delayLevel
     * @param content
     * @throws RtException
     */
    public void syncSendMsg(String topic, String tag, String key, Integer delayLevel, Object content) throws RtException {
        Message message = getMsg(topic, tag, key, delayLevel, content);
        syncSendMsg(message);
    }

    /**
     *
     * @param topic
     * @param tag
     * @param content
     * @throws RtException
     */
    public void syncSendMsg(String topic, String tag, Object content) throws RtException {
        Message message = getMsg(topic, tag, null, null, content);
        syncSendMsg(message);
    }

    /**
     *
     * @param topic
     * @param content
     * @throws RtException
     */
    public void syncSendMsg(String topic, Object content) throws RtException {
        Message message = getMsg(topic, null, null, null, content);
        syncSendMsg(message);
    }

    /**
     *
     * @param topic
     * @param tag
     * @param key
     * @param content
     * @throws RtException
     */
    public void syncSendMsg(String topic, String tag, String key, Object content) throws RtException {
        Message message = getMsg(topic, tag, key, null, content);
        syncSendMsg(message);
    }

    /**
     *
     * @param topic
     * @param tag
     * @param delayLevel
     * @param content
     * @throws RtException
     */
    public void syncSendMsg(String topic, String tag, Integer delayLevel, Object content) throws RtException {
        Message message = getMsg(topic, tag, null, delayLevel, content);
        syncSendMsg(message);
    }


    /**
     * todo sth after send msg finish
     *
     * @param message
     * @param sendResult
     */
    public void doAfterSyncSendMsg(Message message, SendResult sendResult) {
    }

    /**
     * @param message
     * @param sendCallback
     */
    public void asyncSendMsg(Message message, SendCallback sendCallback) {
        try {
            defaultMQProducer.send(message, sendCallback);
            logger.debug("async send msg ");
        } catch (Exception e) {
            logger.error("async send msg error, topic : {}, message : {}", message.getTopic(), message.toString());
            throw new RtException(e.getMessage());
        }
    }

    /**
     *
     * @param topic
     * @param tag
     * @param key
     * @param delayLevel
     * @param msg
     * @return
     */
    private Message getMsg(String topic, String tag, String key, Integer delayLevel, Object msg) {
        if (msg == null) {
            throw new RtException("message content is null", new NullPointerException());
        }
        if (StringUtils.isBlank(topic)) {
            throw new RtException("topic is null", new NullPointerException());
        }
        MessageBuilder builder = new MessageBuilder(msg);
        builder.topic(topic);
        if (StringUtils.isBlank(tag)) {
            builder.tag(tag);
        }
        if (StringUtils.isNotBlank(key)) {
            builder.key(key);
        }
        if (delayLevel != null) {
            builder.delayLevel(delayLevel);
        }
        return builder.build();
    }
}

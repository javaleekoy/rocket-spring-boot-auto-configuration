package com.pd.rt.base;

import com.pd.rt.exception.RtException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author peramdy on 2018/9/28.
 */
public class AbstractRtMqProducer {

    private static Logger logger = LoggerFactory.getLogger(AbstractRtMqProducer.class);

    @Autowired
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
}

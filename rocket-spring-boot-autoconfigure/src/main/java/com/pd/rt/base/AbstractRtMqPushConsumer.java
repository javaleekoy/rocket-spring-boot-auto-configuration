package com.pd.rt.base;

import lombok.Getter;
import lombok.Setter;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * @author peramdy on 2018/9/29.
 */
public abstract class AbstractRtMqPushConsumer<T> extends AbstractRtConsumer<T> {

    private static Logger logger = LoggerFactory.getLogger(AbstractRtMqProducer.class);

    @Getter
    @Setter
    private DefaultMQPushConsumer defaultMQPushConsumer;

    /**
     * do after consume msg
     *
     * @param msg
     * @param extMap
     * @return
     */
    public abstract boolean doAfterConsumeMsg(T msg, Map<String, Object> extMap);


    /**
     * dealConcurrentlyMessage
     *
     * @param msgs
     * @param context
     * @param retryConsumeTimes
     * @return
     */
    public ConsumeConcurrentlyStatus dealConcurrentlyMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context, Integer retryConsumeTimes) {
        for (MessageExt messageExt : msgs) {
            T t = parseMsg(messageExt);
            Map<String, Object> ext = parseExtParam(messageExt);
            if (retryConsumeTimes != null && messageExt.getReconsumeTimes() > retryConsumeTimes) {
                logger.warn("consume concurrently " + retryConsumeTimes + " times fail , msg : {}", messageExt.toString());
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
            if (null != t && !doAfterConsumeMsg(t, ext)) {
                logger.warn("consume concurrently fail , will be re-consumed msgId : {}", messageExt.getMsgId());
                return ConsumeConcurrentlyStatus.RECONSUME_LATER;
            }
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }

    /**
     * dealOrderlyMessage
     *
     * @param msgs
     * @param context
     * @param retryConsumeTimes
     * @return
     */
    public ConsumeOrderlyStatus dealOrderlyMessage(List<MessageExt> msgs, ConsumeOrderlyContext context, Integer retryConsumeTimes) {
        for (MessageExt messageExt : msgs) {
            logger.info("receive msgId: {}, tags : {}", messageExt.getMsgId(), messageExt.getTags());
            T t = parseMsg(messageExt);
            Map<String, Object> ext = parseExtParam(messageExt);
            if (retryConsumeTimes != null && messageExt.getReconsumeTimes() > retryConsumeTimes) {
                logger.warn("consume orderly " + retryConsumeTimes + " times fail , msg : {}", messageExt.toString());
                return ConsumeOrderlyStatus.SUCCESS;
            }
            if (null != t && !doAfterConsumeMsg(t, ext)) {
                logger.warn("consume orderly fail ,will be re-consumed msgId : {}", messageExt.getMsgId());
                return ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT;
            }
        }
        return ConsumeOrderlyStatus.SUCCESS;
    }

}

package com.pd.rt.annotation;

import com.pd.rt.utils.RtListenerMode;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * @author peramdy on 2018/9/28.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface RtMQConsumer {

    /**
     * consumer group name
     *
     * @return
     */
    String groupName();

    /**
     * consumer topic
     *
     * @return
     */
    String topic();

    /**
     * message model
     *
     * @return
     */
    MessageModel messageModel() default MessageModel.CLUSTERING;


    /**
     * consume from where
     *
     * @return
     */
    ConsumeFromWhere consumeFromWhere() default ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET;


    /**
     * message listener mode
     *
     * @return
     */
    RtListenerMode listenerMode() default RtListenerMode.CONSUME_MODE_CONCURRENTLY;

    /**
     * tag
     *
     * @return
     */
    String[] tag() default {"*"};


    /**
     * retry consume times
     *
     * @return
     */
    int retryConsumeTimes() default 0;

}

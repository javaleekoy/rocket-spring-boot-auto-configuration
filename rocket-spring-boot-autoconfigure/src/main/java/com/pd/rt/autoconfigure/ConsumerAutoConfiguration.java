package com.pd.rt.autoconfigure;

import com.pd.rt.annotation.RtMQConsumer;
import com.pd.rt.base.AbstractRtMqPushConsumer;
import com.pd.rt.exception.RtException;
import com.pd.rt.utils.RtListenerMode;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author peramdy on 2018/9/28.
 */
@Configuration
@ConditionalOnBean(value = BaseAutoConfiguration.class, annotation = RtMQConsumer.class)
public class ConsumerAutoConfiguration extends BaseAutoConfiguration {

    private static Logger logger = LoggerFactory.getLogger(ConsumerAutoConfiguration.class);

    /**
     * valid consume group name
     */
    private Map<String, String> validConsumerMap;

    /**
     * consumer message
     */
    @PostConstruct
    public void consumerMsg() {
        Map<String, Object> beans = applicationContext.getBeansWithAnnotation(RtMQConsumer.class);
        if (CollectionUtils.isEmpty(beans)) {
            throw new RtException("no annotation @RtMQConsumer");
        }
        if (StringUtils.isBlank(rtProperties.getNameServerAddress())) {
            throw new RtException("nameServerAddress is null");
        }
        validConsumerMap = new HashMap<>(beans.size());
        beans.keySet().stream().forEach(clzName -> publishConsumer(clzName, beans.get(clzName)));
        validConsumerMap = null;
    }


    /**
     * publish consume massage
     *
     * @param clzName
     * @param clz
     */
    private void publishConsumer(String clzName, Object clz) {
        RtMQConsumer rtMQConsumer = applicationContext.findAnnotationOnBean(clzName, RtMQConsumer.class);
        if (StringUtils.isBlank(rtMQConsumer.groupName())) {
            throw new RtException("@RtMQConsumer property groupName is null");
        }
        if (StringUtils.isBlank(rtMQConsumer.topic())) {
            throw new RtException("@RtMQConsumer property topic is null");
        }
        if (!AbstractRtMqPushConsumer.class.isAssignableFrom(clz.getClass())) {
            throw new RtException(clzName + " must extends AbstractRtMqConsumer");
        }
        Environment environment = applicationContext.getEnvironment();
        String groupName = environment.resolvePlaceholders(rtMQConsumer.groupName());
        logger.debug("consume groupName : {}" + groupName);
        String topic = environment.resolvePlaceholders(rtMQConsumer.topic());
        logger.debug("consume topic : {}" + topic);
        String tags;
        Integer retryConsumeTimes = null;
        if (rtMQConsumer.retryConsumeTimes() > 0) {
            retryConsumeTimes = rtMQConsumer.retryConsumeTimes();
        } else if (rtMQConsumer.retryConsumeTimes() < 0) {
            throw new RtException("@RtMQConsumer property retryConsumeTimes must be positive integer");
        }
        if (rtMQConsumer.tag().length == 1) {
            tags = environment.resolvePlaceholders(rtMQConsumer.tag()[0]);
        } else {
            tags = StringUtils.join(rtMQConsumer.tag(), "||");
        }
        logger.debug("consume tags : {}" + tags);
        if (StringUtils.isNotBlank(validConsumerMap.get(groupName))) {
            String exist = validConsumerMap.get(groupName);
            throw new RtException(exist + " is existed");
        } else {
            validConsumerMap.put(groupName, "topic : " + topic + " , tags :" + tags);
        }
        try {
            DefaultMQPushConsumer defaultMQPushConsumer = new DefaultMQPushConsumer(groupName);
            defaultMQPushConsumer.setNamesrvAddr(rtProperties.getNameServerAddress());
            defaultMQPushConsumer.subscribe(topic, tags);
            defaultMQPushConsumer.setInstanceName(UUID.randomUUID().toString());
            defaultMQPushConsumer.setVipChannelEnabled(rtProperties.getVipChannel());
            defaultMQPushConsumer.setConsumeFromWhere(rtMQConsumer.consumeFromWhere());
            defaultMQPushConsumer.setMessageModel(rtMQConsumer.messageModel());
            AbstractRtMqPushConsumer abstractRtMqPushConsumer = (AbstractRtMqPushConsumer) clz;
            Integer finalRetryConsumeTimes = retryConsumeTimes;
            if (rtMQConsumer.listenerMode() == RtListenerMode.CONSUME_MODE_ORDERLY) {
                defaultMQPushConsumer.registerMessageListener((List<MessageExt> msgs, ConsumeOrderlyContext context) ->
                        abstractRtMqPushConsumer.dealOrderlyMessage(msgs, context, finalRetryConsumeTimes)
                );
            } else if (rtMQConsumer.listenerMode() == RtListenerMode.CONSUME_MODE_CONCURRENTLY) {
                defaultMQPushConsumer.registerMessageListener((List<MessageExt> msgs, ConsumeConcurrentlyContext context) ->
                        abstractRtMqPushConsumer.dealConcurrentlyMessage(msgs, context, finalRetryConsumeTimes)
                );
            } else {
                throw new RtException("@RtMQConsumer property RtListenerMode is error");
            }
            abstractRtMqPushConsumer.setDefaultMQPushConsumer(defaultMQPushConsumer);
            defaultMQPushConsumer.start();
            logger.info("rocketMq default push consumer started !");
        } catch (MQClientException e) {
            throw new RtException(e.getErrorMessage());
        }
    }

}

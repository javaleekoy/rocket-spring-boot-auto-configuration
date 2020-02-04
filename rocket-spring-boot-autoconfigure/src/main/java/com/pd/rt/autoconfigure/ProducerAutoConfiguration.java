package com.pd.rt.autoconfigure;

import com.pd.rt.annotation.RtMQProducer;
import com.pd.rt.exception.RtException;
import lombok.Setter;
import lombok.Value;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;

import java.util.Map;

/**
 * @author peramdy on 2018/9/25.
 */
@Configuration
@ConditionalOnBean(value = BaseAutoConfiguration.class,annotation = RtMQProducer.class)
public class ProducerAutoConfiguration extends BaseAutoConfiguration {

    private static Logger logger = LoggerFactory.getLogger(ProducerAutoConfiguration.class);

    @Setter
    private DefaultMQProducer defaultMQProducer;

    /**
     * defaultMQProducer
     *
     * @return
     * @throws MQClientException
     * @throws RemotingException
     * @throws InterruptedException
     * @throws MQBrokerException
     */
    @Bean
    @ConditionalOnMissingBean
    public DefaultMQProducer defaultMQProducer() throws MQClientException, RemotingException, InterruptedException, MQBrokerException {
        Map<String, Object> rtMQProducer = applicationContext.getBeansWithAnnotation(RtMQProducer.class);
        if (CollectionUtils.isEmpty(rtMQProducer)) {
            throw new RtException("no annotation @RtMQProducer");
        }
        if (defaultMQProducer == null) {
            if (StringUtils.isBlank(rtProperties.getNameServerAddress())) {
                throw new RtException("nameServerAddress is null");
            }
            defaultMQProducer = new DefaultMQProducer(getGroupName(rtProperties.getProducer().getGroupName()));
            defaultMQProducer.setNamesrvAddr(rtProperties.getNameServerAddress());
            if (rtProperties.getProducer().getGroupName() != null) {
                defaultMQProducer.setVipChannelEnabled(rtProperties.getVipChannel());
            }
            if (rtProperties.getProducer().getMessageSize() != null) {
                defaultMQProducer.setMaxMessageSize(rtProperties.getProducer().getMessageSize());
            }
            if (rtProperties.getProducer().getTimeout() != null) {
                defaultMQProducer.setSendMsgTimeout(rtProperties.getProducer().getTimeout());
            }
            if (rtProperties.getProducer().getRetryTimes() != null) {
                defaultMQProducer.setRetryTimesWhenSendFailed(rtProperties.getProducer().getRetryTimes());
            }
            defaultMQProducer.start();
        }
        logger.debug("rocketMq default producer started !");
        return defaultMQProducer;
    }
}

package com.pd.rt.autoconfigure;

import com.pd.rt.annotation.EnableRtMQConfiguration;
import com.pd.rt.base.AbstractRtConsumer;
import com.pd.rt.base.AbstractRtMqProducer;
import com.pd.rt.config.RtProperties;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;

/**
 * @author peramdy on 2018/9/28.
 */
@Configuration
@ConditionalOnBean(annotation = EnableRtMQConfiguration.class)
@AutoConfigureAfter({AbstractRtMqProducer.class, AbstractRtConsumer.class})
@EnableConfigurationProperties(RtProperties.class)
public class BaseAutoConfiguration implements ApplicationContextAware {

    @Value("${spring.application.name}")
    private String defaultGroupName;

    /**
     * configuration properties
     */
    protected RtProperties rtProperties;

    @Autowired
    public void setRegistryConfig(RtProperties rtProperties) {
        this.rtProperties = rtProperties;
    }

    /**
     * get annotation info
     */
    protected ConfigurableApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = (ConfigurableApplicationContext) applicationContext;
    }

    /**
     * getGroupName
     *
     * @param groupName
     * @return
     */
    protected String getGroupName(String groupName) {
        if (StringUtils.isNotBlank(groupName)) {
            return groupName;
        } else if (StringUtils.isNotBlank(defaultGroupName)) {
            return defaultGroupName;
        }
        return null;
    }

}

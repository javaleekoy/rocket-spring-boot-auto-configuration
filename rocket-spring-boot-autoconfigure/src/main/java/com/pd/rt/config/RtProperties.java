package com.pd.rt.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author peramdy on 2018/9/25.
 */
@Data
@ConfigurationProperties(prefix = RtProperties.PREFIX)
public class RtProperties extends AbstractProperties {

    public static final String PREFIX = "spring.rocketmq";

    /**
     * list address (a.a.a.a:aa,b.b.b.b:bb)
     */
    private String nameServerAddress;

    /**
     * send msg with VIPChannel
     */
    private Boolean vipChannel = Boolean.TRUE;

    /**
     * msg producer
     */
    private Producer producer = new Producer();

    /**
     * msg consumer
     */
    private Consumer consumer = new Consumer();


    @Data
    public static class Producer {
        /**
         * rocket producer group name
         */
        private String groupName;

        /**
         * retry times when send msg failed
         */
        private Integer retryTimes = 3;

        /**
         * send msg size
         */
        private Integer messageSize;

        /**
         * send msg timeout
         */
        private Integer timeout;
    }

    @Data
    @Deprecated
    public static class Consumer {
        /**
         * rocket producer group name
         */
        @Deprecated
        private String groupName;

        /**
         * retry times when consumer msg failed
         */
        @Deprecated
        private Integer retryTimes = 3;

        /**
         * send msg timeout
         */
        @Deprecated
        private Integer timeout;
    }

}

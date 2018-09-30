package com.pd.rt.builder;

import com.google.gson.Gson;
import com.pd.rt.annotation.RtMQKey;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.common.message.Message;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * @author peramdy on 2018/9/28.
 */
public class MessageBuilder {

    private Gson gson = new Gson();

    private static final String[] DELAY_ARRAY = "1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h".split(" ");

    /**
     * msg topic
     */
    private String topic;

    /**
     * msg tag
     */
    private String tag;

    /**
     * msg key
     */
    private String key;

    /**
     * msg content
     */
    private Object msg;

    /**
     * delay time level
     */
    private Integer delayLevel;


    public MessageBuilder(String topic, String tag) {
        this.topic = topic;
        this.tag = tag;
    }

    public MessageBuilder(Object msg) {
        this.msg = msg;
    }

    public MessageBuilder topic(String topic) {
        this.topic = topic;
        return this;
    }

    public MessageBuilder tag(String tag) {
        this.tag = tag;
        return this;
    }

    public MessageBuilder key(String key) {
        this.key = key;
        return this;
    }

    public MessageBuilder msg(Object msg) {
        this.msg = msg;
        return this;
    }

    public MessageBuilder delayLevel(Integer delayLevel) {
        this.delayLevel = delayLevel;
        return this;
    }

    public Message build() {
        final String[] msgKey = new String[]{null};
        Field[] fields = msg.getClass().getDeclaredFields();
        Arrays.stream(fields).forEach(field -> {
            Annotation[] annotations = field.getAnnotations();
            if (annotations.length > 0) {
                Arrays.stream(annotations).forEach(annotation -> {
                    if (annotation.annotationType().equals(RtMQKey.class)) {
                        field.setAccessible(true);
                        RtMQKey rtMQKey = RtMQKey.class.cast(annotation);
                        try {
                            if (StringUtils.isBlank(rtMQKey.prefix())) {
                                msgKey[0] = field.get(msg).toString();
                            } else {
                                msgKey[0] = rtMQKey.prefix() + field.get(msg).toString();
                            }
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
        String msgStr = gson.toJson(msg);
        if (StringUtils.isBlank(topic)) {
            throw new RuntimeException("topic is null");
        }
        Message message = new Message();
        message.setTopic(topic);
        message.setBody(msgStr.getBytes(Charset.forName(StandardCharsets.UTF_8.name())));
        if (StringUtils.isNotBlank(tag)) {
            message.setTags(tag);
        }
        if (StringUtils.isNotBlank(msgKey[0])) {
            message.setKeys(msgKey[0]);
        }
        if (delayLevel != null && delayLevel > 0 && delayLevel <= DELAY_ARRAY.length) {
            message.setDelayTimeLevel(delayLevel);
        }
        return message;
    }

}

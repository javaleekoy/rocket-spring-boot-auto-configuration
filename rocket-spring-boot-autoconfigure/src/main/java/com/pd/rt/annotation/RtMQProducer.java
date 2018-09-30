package com.pd.rt.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * @author peramdy on 2018/9/28.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface RtMQProducer {
}

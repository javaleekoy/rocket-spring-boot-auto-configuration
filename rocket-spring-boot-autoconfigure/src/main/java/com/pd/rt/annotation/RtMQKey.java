package com.pd.rt.annotation;

import java.lang.annotation.*;

/**
 * @author peramdy on 2018/9/28.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RtMQKey {
    String prefix() default "";
}

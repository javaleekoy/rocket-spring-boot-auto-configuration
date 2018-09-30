package com.pd.rt.annotation;

import java.lang.annotation.*;

/**
 * @author peramdy on 2018/9/28.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface EnableRtMQConfiguration {
}

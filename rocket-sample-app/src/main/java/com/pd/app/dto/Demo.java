package com.pd.app.dto;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Date;

/**
 * @author peramdy on 2018/9/30.
 */
public class Demo {

    private Integer id;

    private String name;

    private Date time;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}

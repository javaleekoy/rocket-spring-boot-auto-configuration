package com.pd.app;

import com.pd.rt.annotation.EnableRtMQConfiguration;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author peramdy on 2018/9/29.
 */
@SpringBootApplication
//@EnableRtMQConfiguration
public class App {
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(App.class);
        application.setBannerMode(Banner.Mode.CONSOLE);
        application.run(args);
    }
}

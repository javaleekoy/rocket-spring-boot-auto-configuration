package com.pd.rt.context.event;

import com.pd.rt.utils.RtMqLogo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.Banner;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.boot.context.logging.LoggingApplicationListener;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;

import static com.pd.rt.utils.RtConstants.LINE_SEPARATOR;

/**
 * @author peramdy on 2018/9/25.
 */
@Order(LoggingApplicationListener.DEFAULT_ORDER)
public class WelcomeLogoApplicationListener implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {

    private static final Logger logger = LoggerFactory.getLogger(WelcomeLogoApplicationListener.class);

    private static Banner.Mode BANNER_MODE = Banner.Mode.CONSOLE;

    public static void setBannerMode(Banner.Mode bannerMode) {
        BANNER_MODE = bannerMode;
    }

    @Override
    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
        String bannerText = buildBannerText();
        if (BANNER_MODE == Banner.Mode.OFF) {
            return;
        } else if (BANNER_MODE == Banner.Mode.CONSOLE) {
            System.out.println(bannerText);
        } else if (BANNER_MODE == Banner.Mode.LOG) {
            logger.info(bannerText);
        }
    }


    /**
     * load banner
     *
     * @return
     */
    private String buildBannerText() {
        StringBuilder bannerTextBuilder = new StringBuilder();
        bannerTextBuilder
                .append(LINE_SEPARATOR)
                .append(RtMqLogo.logo)
                .append(LINE_SEPARATOR)
                .append(" :: Rocket MQ Spring Boot (v1.0.0)")
                .append(LINE_SEPARATOR);
        return bannerTextBuilder.toString();
    }

}

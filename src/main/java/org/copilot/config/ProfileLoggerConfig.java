package org.copilot.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import jakarta.annotation.PostConstruct;
import java.util.Arrays;

@Configuration
public class ProfileLoggerConfig {
    private static final Logger logger = LoggerFactory.getLogger(ProfileLoggerConfig.class);

    @Autowired
    private Environment env;

    @PostConstruct
    public void logActiveProfiles() {
        String[] profiles = env.getActiveProfiles();
        if (profiles.length == 0) {
            profiles = env.getDefaultProfiles();
        }
        logger.info("Active Spring profiles: {}", Arrays.toString(profiles));
    }
}

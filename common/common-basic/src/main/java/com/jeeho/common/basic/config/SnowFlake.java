package com.jeeho.common.basic.config;

import com.jeeho.common.basic.properties.SnowflakeProperties;
import com.jeeho.common.core.utils.SnowflakeIdWorker;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
public class SnowFlake {

    private final SnowflakeProperties snowflakeProperties;

    @Bean
    public SnowflakeIdWorker initTokenWorker() {
        return new SnowflakeIdWorker(Integer.parseInt(snowflakeProperties.getWorkId()),
                Integer.parseInt(snowflakeProperties.getDataCenterId()));
    }
}

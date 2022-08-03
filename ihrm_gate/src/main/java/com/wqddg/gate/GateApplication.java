package com.wqddg.gate;

import com.wqddg.common.utils.JwtUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;

/**
 * @Author: wqddg
 * @ClassName GateApplication
 * @DateTime: 2022/1/13 23:04
 * @remarks : #
 */
@SpringBootApplication(scanBasePackages = "com.wqddg")
@EnableZuulProxy
@EnableDiscoveryClient
public class GateApplication {
    public static void main(String[] args) {
        SpringApplication.run(GateApplication.class);
    }

    @Bean
    public JwtUtils getJwtUtils(){
        return new JwtUtils();
    }
}

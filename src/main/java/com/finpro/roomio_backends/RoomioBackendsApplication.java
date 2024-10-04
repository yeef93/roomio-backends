package com.finpro.roomio_backends;

import com.finpro.roomio_backends.config.EnvConfigurationProperties;
import com.finpro.roomio_backends.config.RsaKeyConfigProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties({RsaKeyConfigProperties.class, EnvConfigurationProperties.class})
public class RoomioBackendsApplication {

	public static void main(String[] args) {
		SpringApplication.run(RoomioBackendsApplication.class, args);
	}

}

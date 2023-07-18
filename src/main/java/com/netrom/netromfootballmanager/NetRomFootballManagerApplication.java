package com.netrom.netromfootballmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication(scanBasePackages = "com.netrom.netromfootballmanager")
@RestController
@EnableScheduling
public class NetRomFootballManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(NetRomFootballManagerApplication.class, args);
	}
}

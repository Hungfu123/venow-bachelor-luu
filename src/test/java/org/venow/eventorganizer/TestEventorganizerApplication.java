package org.venow.eventorganizer;

import org.springframework.boot.SpringApplication;

public class TestEventorganizerApplication {

	public static void main(String[] args) {
		SpringApplication.from(EventorganizerApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}

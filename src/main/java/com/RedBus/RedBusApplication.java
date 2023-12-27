package com.RedBus;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@OpenAPIDefinition(
		info = @Info(title = "Bus Booking Application APIs",
		version = "1.0.0",
		description = "It is a bus booking application apis documentation"),
		servers=@Server(url = "http://localhost:8080",
		description = "bus booking APIs")

)
@SpringBootApplication
public class RedBusApplication {

	public static void main(String[] args) {
		SpringApplication.run(RedBusApplication.class, args);
	}

	@Bean
	public ModelMapper modelMapper()
	{
		return new ModelMapper();
	}

	@Bean
	public ObjectMapper objectMapper() {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new Hibernate5Module());
		objectMapper.registerModule(new JavaTimeModule()); // Include JSR310 module
		return objectMapper;
	}

}

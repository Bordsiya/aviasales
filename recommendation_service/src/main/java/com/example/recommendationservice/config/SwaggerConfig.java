package com.example.recommendationservice.config;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
        info = @Info(
                title = "Aviasales Api",
                description = "lab3 BLPS", version = "1.0.0",
                contact = @Contact(
                        name = "Bordun Anastasiya",
                        email = "bordun.2002@mail.ru",
                        url = "https://m.vk.com/bordsiya"
                )
        )
)
public class SwaggerConfig {

}

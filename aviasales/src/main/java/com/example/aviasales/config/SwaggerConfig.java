package com.example.aviasales.config;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@OpenAPIDefinition(
        info = @Info(
                title = "Aviasales Api",
                description = "lab1 BLPS", version = "1.0.0",
                contact = @Contact(
                        name = "Bordun Anastasiya",
                        email = "bordun.2002@mail.ru",
                        url = "https://m.vk.com/bordsiya"
                )
        )
)
@SecurityScheme(
        type = SecuritySchemeType.HTTP,
        name = "basicAuth",
        scheme = "basic")
public class SwaggerConfig {

}

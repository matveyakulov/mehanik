package ru.neirodev.mehanik.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.List;

@PropertySource(value = "file:${catalina.base}/conf/mehanik.properties")
@Configuration
public class SwaggerConfig {

    @Value("${swagger.url}")
    private String url;

    @Bean
    public OpenAPI customOpenApi() {
        return new OpenAPI().info(new Info().title("Mehanik")
                        .version("1.0")
                        .description("Приложение Механик")
                        .contact(new Contact().name("Матвей Акулов")
                                .email("akulov-gosha@mail.ru")))
                .servers(List.of(new Server().url(url)
                                .description("Mehanik API")));
    }

}

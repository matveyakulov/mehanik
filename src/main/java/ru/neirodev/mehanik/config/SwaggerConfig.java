package ru.neirodev.mehanik.config;

import io.swagger.v3.oas.models.OpenAPI;
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
    public OpenAPI customOpenAPI() {
        Server server = new Server();
        server.setUrl(url);
        return new OpenAPI().servers(List.of(server));
    }

}

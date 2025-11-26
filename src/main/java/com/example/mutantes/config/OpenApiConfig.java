package com.example.mutantes.config;

import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.OpenAPI;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

  @Bean
  public OpenAPI openApiConfig() {
    Server devServer = new Server();
    devServer.setUrl("http://localhost:8080");
    devServer.setDescription("Servidor de desarrollo local");

    Info info = new Info()
        .title("API de gestion de mutantes")
        .version("1.0.0")
        .description("API REST completa para la administración de un catálogo de mutantes. " +
            "Permite realizar operaciones CRUD sobre personas, analizar ADN, " +
            "obtener estadísticas especificas sobre la cantidad de mutantes y personas.");

    return new OpenAPI()
        .info(info)
        .servers(List.of(devServer));
  }
}

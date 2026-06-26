package cl.municipalidad.usuarios.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI usuariosOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Microservicio de Usuarios")
                        .description("Endpoints para la administración, validación y control de usuarios municipales")
                        .version("1.0.0"));
    }
}
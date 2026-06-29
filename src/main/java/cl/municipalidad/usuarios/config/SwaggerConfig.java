package cl.municipalidad.usuarios.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI usuariosOpenAPI() {
        final String securitySchemeName = "bearerAuth";

        return new OpenAPI()
                .info(new Info()
                        .title("API de Microservicio de Usuarios")
                        .description("Endpoints para la administración, validación y control de usuarios municipales")
                        .version("1.0.0")
                        .description("Servicio crítico del ecosistema distribuido encargado de la administración corporativa de cuentas, validación de identidades (RUT), perfiles y provisión de datos de autenticación internos.")
                        .contact(new Contact()
                                .name("Departamento de Informática Municipal")
                                .email("soporte.ti@municipalidad.cl")))
                        .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                        .components(new Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .name(securitySchemeName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Introduce tu token JWT obtenido del microservicio de autenticación (ms-auth) para interactuar con los endpoints protegidos.")));
                        }
}
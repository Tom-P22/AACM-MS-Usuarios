package cl.municipalidad.usuarios.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "Estructura de datos optimizada e interna mapeada para procesos de autenticación criptográfica")
public class UsuarioAuthDTO {

    @Schema(description = "Correo del usuario que intenta loguearse", example = "admin.ti@municipalidad.cl")
    private String email;

    @Schema(description = "Hash Bcrypt de la contraseña guardada en base de datos", example = "$2a$10$eImiTxAkamDfMoM3asdf3453asdf...")
    private String password;

    @Schema(description = "Nombre del portador de la cuenta", example = "Administrador de Sistemas")
    private String nombre;

    @Schema(description = "String representativo del rol para el token JWT", example = "ADMIN")
    private String rolUsuario;

    @Schema(description = "Estado actual de vigencia operativa", example = "true")
    private Boolean activo;
}
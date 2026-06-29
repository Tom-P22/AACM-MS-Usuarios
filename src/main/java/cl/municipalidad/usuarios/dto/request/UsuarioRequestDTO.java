package cl.municipalidad.usuarios.dto.request;

import cl.municipalidad.usuarios.enums.RolUsuario;
import cl.municipalidad.usuarios.enums.TipoUsuario;
import cl.municipalidad.usuarios.validations.RutValido;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Estructura de datos requerida para registrar o actualizar un usuario en el sistema municipal")
public class UsuarioRequestDTO {

    @NotBlank(message = "El campo 'rut' es obligatorio")
    @RutValido(message = "El RUT ingresado no es válido")
    @Schema(description = "RUT de la persona o institución (con puntos y guion)", example = "12.345.678-9", requiredMode = Schema.RequiredMode.REQUIRED)
    private String rut;

    @NotBlank(message = "El campo 'nombre' es obligatorio")
    @Schema(description = "Nombre completo del usuario o Razón Social de la entidad", example = "Juan Carlos Pérez Muñoz", requiredMode = Schema.RequiredMode.REQUIRED)
    private String nombre;

    @NotBlank(message = "El campo 'email' es obligatorio")
    @Email(message = "El campo 'email' debe ser una dirección de correo electrónico válida")
    @Schema(description = "Dirección de correo electrónico único y de contacto", example = "juan.perez@municipalidad.cl", requiredMode = Schema.RequiredMode.REQUIRED)
    private String email;

    @NotBlank(message = "El campo 'password' es obligatorio")
    @Schema(description = "Contraseña secreta de acceso al sistema", example = "MuniSecure2026!", requiredMode = Schema.RequiredMode.REQUIRED)
    private String password;
    
    @NotNull(message = "El tipo de usuario es obligatorio (PERSONA_NATURAL, EMPRESA_PRIVADA, FUNDACION_BENEFICA o COLEGIO)")
    @Schema(description = "Clasificación legal u operacional del usuario dentro de la comuna", example = "PERSONA_NATURAL", requiredMode = Schema.RequiredMode.REQUIRED)
    private TipoUsuario tipoUsuario;

    @NotNull(message = "El campo rolUsuario es obligatorio (ADMIN o USER)")
    @Schema(description = "Nivel de acceso autorizado en la plataforma", example = "USER", requiredMode = Schema.RequiredMode.REQUIRED)
    private RolUsuario rolUsuario;
}
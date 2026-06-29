package cl.municipalidad.usuarios.dto.response;

import cl.municipalidad.usuarios.enums.RolUsuario;
import cl.municipalidad.usuarios.enums.TipoUsuario;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Información pública devuelta tras operaciones exitosas sobre un usuario")
public class UsuarioResponseDTO {

    @Schema(description = "Identificador único incremental asignado por la base de datos relacional", example = "1")
    private Long id;

    @Schema(description = "RUT registrado y verificado de la cuenta", example = "12.345.678-9")
    private String rut;

    @Schema(description = "Nombre o Razón Social registrada", example = "Juan Carlos Pérez Muñoz")
    private String nombre;

    @Schema(description = "Correo electrónico institucional o ciudadano", example = "juan.perez@municipalidad.cl")
    private String email;

    @Schema(description = "Tipo de entidad asociada", example = "PERSONA_NATURAL")
    private TipoUsuario tipoUsuario;

    @Schema(description = "Rol asignado para el control de accesos", example = "USER")
    private RolUsuario rolUsuario;

    @Schema(description = "Bandera que indica si el usuario está vigente (Eliminado lógico = false)", example = "true")
    private Boolean activo;
}
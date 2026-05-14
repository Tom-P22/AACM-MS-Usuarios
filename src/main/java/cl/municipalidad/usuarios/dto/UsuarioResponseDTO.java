package cl.municipalidad.usuarios.dto;

import cl.municipalidad.usuarios.enums.RolUsuario;
import cl.municipalidad.usuarios.enums.TipoUsuario;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UsuarioResponseDTO {
    private Long id;
    private String rut;
    private String nombre;
    private String email;
    private TipoUsuario tipoUsuario;
    private RolUsuario rolUsuario;
}

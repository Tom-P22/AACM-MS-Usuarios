package cl.municipalidad.usuarios.repository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import cl.municipalidad.usuarios.enums.RolUsuario;
import cl.municipalidad.usuarios.enums.TipoUsuario;
import cl.municipalidad.usuarios.model.Usuario;

@DataJpaTest
@ActiveProfiles("test")
class UsuarioRepositoryTest {

    @Autowired
    private UsuarioRepository usuarioRepository;

    // --- Test: findByRutAndActivoTrueRetornaUsuario ---
    @Test
    void findByRutAndActivoTrueRetornaUsuario() {
        Usuario usuario = Usuario.builder()
                .rut("12345678-9")
                .nombre("Diego Silva")
                .email("diego@muni.cl")
                .password("password")
                .tipoUsuario(TipoUsuario.PERSONA_NATURAL)
                .rolUsuario(RolUsuario.USER)
                .activo(true)
                .build();
        usuarioRepository.save(usuario);

        Optional<Usuario> resultado = usuarioRepository.findByRutAndActivoTrue("12345678-9");

        assertTrue(resultado.isPresent());
        assertEquals("Diego Silva", resultado.get().getNombre());
    }
}
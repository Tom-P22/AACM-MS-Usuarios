package cl.municipalidad.usuarios.service;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import cl.municipalidad.usuarios.dto.request.UsuarioRequestDTO;
import cl.municipalidad.usuarios.dto.response.UsuarioResponseDTO;
import cl.municipalidad.usuarios.enums.RolUsuario;
import cl.municipalidad.usuarios.enums.TipoUsuario;
import cl.municipalidad.usuarios.exception.BadRequestException;
import cl.municipalidad.usuarios.model.Usuario;
import cl.municipalidad.usuarios.repository.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioService usuarioService;

    private UsuarioRequestDTO requestDTO;
    private Usuario usuarioEntidad;

    @BeforeEach
    void setUp() {
        requestDTO = new UsuarioRequestDTO("12345678-9", "Juan Perez", "juan@muni.cl", "secure123", TipoUsuario.PERSONA_NATURAL, RolUsuario.USER);
        
        usuarioEntidad = Usuario.builder()
                .id(1L)
                .rut("12345678-9")
                .nombre("Juan Perez")
                .email("juan@muni.cl")
                .password("encoded123")
                .tipoUsuario(TipoUsuario.PERSONA_NATURAL)
                .rolUsuario(RolUsuario.USER)
                .activo(true)
                .build();
    }

    // --- Test: crearUsuarioExitoso ---
    @Test
    void crearUsuarioExitoso() {
        when(usuarioRepository.existsByEmail(requestDTO.getEmail())).thenReturn(false);
        when(usuarioRepository.existsByRut(requestDTO.getRut())).thenReturn(false);
        when(passwordEncoder.encode(requestDTO.getPassword())).thenReturn("encoded123");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioEntidad);

        UsuarioResponseDTO response = usuarioService.crearUsuario(requestDTO);

        assertNotNull(response);
        assertEquals("juan@muni.cl", response.getEmail());
        assertTrue(response.getActivo());
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    // --- Test: crearUsuarioFallaPorEmailDuplicado ---
    @Test
    void crearUsuarioFallaPorEmailDuplicado() {
        when(usuarioRepository.existsByEmail(requestDTO.getEmail())).thenReturn(true);

        assertThrows(BadRequestException.class, () -> usuarioService.crearUsuario(requestDTO));
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    // --- Test: eliminarUsuarioAplicaBorradoLogico ---
    @Test
    void eliminarUsuarioAplicaBorradoLogico() {
        when(usuarioRepository.findByIdAndActivoTrue(1L)).thenReturn(Optional.of(usuarioEntidad));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioEntidad);

        usuarioService.eliminarUsuario(1L);

        assertFalse(usuarioEntidad.isActivo());
        verify(usuarioRepository, times(1)).save(usuarioEntidad);
    }
}
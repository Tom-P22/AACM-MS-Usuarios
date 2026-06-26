package cl.municipalidad.usuarios.controller;

import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import cl.municipalidad.usuarios.service.UsuarioService;

@WebMvcTest(UsuarioController.class)
@AutoConfigureMockMvc(addFilters = false)
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UsuarioService usuarioService;

    // --- Test: registrarUsuarioFallaPorRutInvalido ---
    @Test
    void registrarUsuarioFallaPorRutInvalido() throws Exception {
        String jsonPayload = """
                {
                    "rut": "11111111-K",
                    "nombre": "Carlos Perez",
                    "email": "carlos@muni.cl",
                    "password": "password123",
                    "tipoUsuario": "PERSONA_NATURAL",
                    "rolUsuario": "USER"
                }
                """;

        mockMvc.perform(post("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonPayload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("El RUT ingresado no es válido"));

        verify(usuarioService, never()).crearUsuario(any());
    }
}
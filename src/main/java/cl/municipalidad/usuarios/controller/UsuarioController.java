package cl.municipalidad.usuarios.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.municipalidad.usuarios.dto.UsuarioAuthDTO;
import cl.municipalidad.usuarios.dto.request.UsuarioRequestDTO;
import cl.municipalidad.usuarios.dto.response.UsuarioResponseDTO;
import cl.municipalidad.usuarios.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/usuarios")
@RequiredArgsConstructor

public class UsuarioController {

    private final UsuarioService usuarioService;


    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> registrarUsuario(@Valid @RequestBody UsuarioRequestDTO request) {
        UsuarioResponseDTO nuevoUsuario = usuarioService.crearUsuario(request);
        return new ResponseEntity<>(nuevoUsuario, HttpStatus.CREATED);
    }

    @GetMapping("/internal/buscar/email/{email}")
    public ResponseEntity<UsuarioAuthDTO> obtenerDatosParaAuth(@PathVariable String email) {
    UsuarioAuthDTO authData = usuarioService.obtenerUsuarioParaAuth(email);
    return ResponseEntity.ok(authData);
    }

    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> listarUsuarios(
        @RequestHeader(value = "X-User-Email", required = false) String emailLogueado) {
        System.out.println("El usuario " + emailLogueado + " está listando todos los usuarios.");
        return ResponseEntity.ok(usuarioService.obtenerTodosLosUsuarios());
    }

    @GetMapping("/buscar/{id}")
    public ResponseEntity<UsuarioResponseDTO> listarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.obtenerUsuarioPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> actualizarUsuario(@PathVariable Long id, @Valid @RequestBody UsuarioRequestDTO request) {
        return ResponseEntity.ok(usuarioService.actualizarUsuario(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id) {
        usuarioService.eliminarUsuario(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/buscar/email/{email}")
    public ResponseEntity<UsuarioResponseDTO> listarPorEmail(@PathVariable String email) {
        return ResponseEntity.ok(usuarioService.obtenerUsuarioPorEmail(email));
    }

    @GetMapping("/buscar/rut/{rut}")
    public ResponseEntity<UsuarioResponseDTO> listarPorRut(@PathVariable String rut) {
        return ResponseEntity.ok(usuarioService.obtenerUsuarioPorRut(rut));
    }
}

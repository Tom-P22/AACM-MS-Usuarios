package cl.municipalidad.usuarios.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import cl.municipalidad.usuarios.dto.UsuarioAuthDTO;
import cl.municipalidad.usuarios.dto.request.UsuarioRequestDTO;
import cl.municipalidad.usuarios.dto.response.UsuarioResponseDTO;
import cl.municipalidad.usuarios.dto.response.ErrorResponseDTO;
import cl.municipalidad.usuarios.service.UsuarioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/usuarios")
@RequiredArgsConstructor
@Tag(name = "Módulo Usuarios", description = "Endpoints y operaciones CRUD para la gestión y control de cuentas de usuarios de la Municipalidad")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PostMapping
    @Operation(summary = "Registrar un nuevo usuario", description = "Crea una cuenta verificando reglas de negocio, formato del RUT chileno y unicidad de correo electrónico corporativo o ciudadano.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Usuario creado y registrado con éxito",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Petición incorrecta (Errores de validación de campos o RUT inválido)",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
        @ApiResponse(responseCode = "409", description = "Conflicto (El RUT o Email ya se encuentran registrados en el sistema)",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    public ResponseEntity<UsuarioResponseDTO> registrarUsuario(@Valid @RequestBody UsuarioRequestDTO request) {
        UsuarioResponseDTO nuevoUsuario = usuarioService.crearUsuario(request);
        return new ResponseEntity<>(nuevoUsuario, HttpStatus.CREATED);
    }

    @GetMapping("/internal/buscar/email/{email}")
    @Operation(summary = "Consulta interna de Auth", description = "Endpoint de comunicación inter-servicio (REST/Feign) exclusivo para que `ms-auth` valide credenciales cifradas.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario localizado, retorna credenciales cifradas",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioAuthDTO.class))),
        @ApiResponse(responseCode = "404", description = "No existe ninguna cuenta asociada al correo provisto",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    public ResponseEntity<UsuarioAuthDTO> obtenerDatosParaAuth(
            @Parameter(description = "Correo electrónico a consultar", example = "juan.perez@municipalidad.cl", required = true)
            @PathVariable String email) {
        UsuarioAuthDTO authData = usuarioService.obtenerUsuarioParaAuth(email);
        return ResponseEntity.ok(authData);
    }

    @GetMapping
    @Operation(summary = "Listar usuarios vigentes", description = "Recupera una colección completa con todos los usuarios activos del sistema corporativo municipal.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Listado recuperado exitosamente (La lista puede venir vacía)")
    })
    public ResponseEntity<List<UsuarioResponseDTO>> listarUsuarios(
            @Parameter(name = "X-User-Email", in = ParameterIn.HEADER, description = "Header opcional inyectado por el API Gateway con el correo del usuario solicitante logueado", example = "admin@municipalidad.cl", required = false)
            @RequestHeader(value = "X-User-Email", required = false) String emailLogueado) {
        System.out.println("El usuario " + emailLogueado + " está listando todos los usuarios.");
        return ResponseEntity.ok(usuarioService.obtenerTodosLosUsuarios());
    }

    @GetMapping("/buscar/{id}")
    @Operation(summary = "Buscar usuario por ID", description = "Recupera los datos de una cuenta activa basándose estrictamente en su ID numérico relacional.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario localizado exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioResponseDTO.class))),
        @ApiResponse(responseCode = "404", description = "El ID provisto no concuerda con ningún registro activo",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    public ResponseEntity<UsuarioResponseDTO> listarPorId(
            @Parameter(description = "Identificador único numérico (ID)", example = "1", required = true)
            @PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.obtenerUsuarioPorId(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar usuario", description = "Modifica los atributos editables de un registro existente buscando por su ID numérico relacional.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario actualizado correctamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Errores en las validaciones de los nuevos datos provistos",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
        @ApiResponse(responseCode = "404", description = "El ID provisto no pertenece a ningún usuario registrado",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    public ResponseEntity<UsuarioResponseDTO> actualizarUsuario(
            @Parameter(description = "ID del usuario a modificar", example = "1", required = true) @PathVariable Long id, 
            @Valid @RequestBody UsuarioRequestDTO request) {
        return ResponseEntity.ok(usuarioService.actualizarUsuario(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminación lógica de un usuario", description = "No borra físicamente de la base de datos el registro, sino que actualiza su estado `activo` a false para mantener integridad referencial.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Eliminación lógica realizada con éxito (Sin contenido de respuesta)"),
        @ApiResponse(responseCode = "404", description = "El usuario con el ID especificado no existe",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    public ResponseEntity<Void> eliminarUsuario(
            @Parameter(description = "ID del usuario a desactivar", example = "1", required = true)
            @PathVariable Long id) {
        usuarioService.eliminarUsuario(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/buscar/email/{email}")
    @Operation(summary = "Buscar usuario por Email", description = "Consulta pública o externa para dar con la información de un usuario a partir de su dirección exacta de correo electrónico.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario encontrado correctamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioResponseDTO.class))),
        @ApiResponse(responseCode = "404", description = "No se halló ningún usuario activo con ese Email",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    public ResponseEntity<UsuarioResponseDTO> listarPorEmail(
            @Parameter(description = "Email único a buscar", example = "juan.perez@municipalidad.cl", required = true)
            @PathVariable String email) {
        return ResponseEntity.ok(usuarioService.obtenerUsuarioPorEmail(email));
    }

    @GetMapping("/buscar/rut/{rut}")
    @Operation(summary = "Buscar usuario por RUT", description = "Permite a los módulos externos auditar y obtener la información de una cuenta mediante su identificador nacional (RUT).")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario encontrado con éxito",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioResponseDTO.class))),
        @ApiResponse(responseCode = "404", description = "El RUT provisto no corresponde a ningún usuario del sistema",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    public ResponseEntity<UsuarioResponseDTO> listarPorRut(
            @Parameter(description = "RUT a buscar (formato con puntos y guion)", example = "12.345.678-9", required = true)
            @PathVariable String rut) {
        return ResponseEntity.ok(usuarioService.obtenerUsuarioPorRut(rut));
    }
}
package cl.municipalidad.usuarios.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "Modelo estándar global para la notificación estructurada de excepciones de la API")
public class ErrorResponseDTO {

    @Schema(description = "Instante exacto con zona horaria en el que se gatilló el error", example = "2026-06-28T22:15:30")
    private LocalDateTime timestamp;

    @Schema(description = "Código de estado numérico HTTP", example = "404")
    private int status;

    @Schema(description = "Glosa o nombre oficial estándar del error HTTP", example = "NOT_FOUND")
    private String error;

    @Schema(description = "Mensaje detallado y descriptivo del motivo del fallo de negocio o validación", example = "El usuario con ID 45 no se encuentra registrado en el sistema municipal.")
    private String message;
}
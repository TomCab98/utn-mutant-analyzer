package com.example.mutantes.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(
    description = "DTO que representa los datos de una persona para el análisis de ADN mutante. " +
        "Contiene información personal y la secuencia genética a analizar."
)
public class PersonaDTO {

  @Schema(
      description = "Identificador único de la persona en el sistema. " +
          "Este campo es generado automáticamente y solo debe incluirse en respuestas, " +
          "no en peticiones de creación.",
      example = "1",
      accessMode = Schema.AccessMode.READ_ONLY
  )
  @Positive(message = "El ID debe ser un número positivo")
  private Long id;

  @Schema(
      description = "Nombre de la persona. Debe contener solo letras y espacios. " +
          "Es un campo obligatorio para el registro.",
      example = "Charles",
      requiredMode = Schema.RequiredMode.REQUIRED,
      minLength = 2,
      maxLength = 100
  )
  @NotNull(message = "El nombre no puede ser nulo")
  @NotBlank(message = "El nombre no puede estar vacío")
  @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
  @Pattern(
      regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$",
      message = "El nombre solo puede contener letras y espacios"
  )
  private String nombre;

  @Schema(
      description = "Apellido de la persona. Debe contener solo letras y espacios. " +
          "Es un campo obligatorio para el registro.",
      example = "Xavier",
      requiredMode = Schema.RequiredMode.REQUIRED,
      minLength = 2,
      maxLength = 100
  )
  @NotNull(message = "El apellido no puede ser nulo")
  @NotBlank(message = "El apellido no puede estar vacío")
  @Size(min = 2, max = 100, message = "El apellido debe tener entre 2 y 100 caracteres")
  @Pattern(
      regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$",
      message = "El apellido solo puede contener letras y espacios"
  )
  private String apellido;

  @Schema(
      description = "Indica si la persona ha sido identificada como mutante después del análisis de ADN. " +
          "Este campo es calculado automáticamente por el sistema tras el análisis y no debe " +
          "ser proporcionado en las peticiones de creación o análisis.",
      example = "true",
      accessMode = Schema.AccessMode.READ_ONLY
  )
  private boolean esMutante;

  @Schema(
      description = "Secuencia de ADN de la persona representada como una matriz NxN. " +
          "Cada elemento del array representa una fila de la matriz de ADN. " +
          "Requisitos: " +
          "- Debe ser una matriz cuadrada (NxN) " +
          "- Cada fila debe tener la misma longitud " +
          "- Solo se permiten las letras A, T, C, G (representando las bases nitrogenadas: Adenina, Timina, Citosina, Guanina) " +
          "- Todas las letras deben estar en mayúsculas " +
          "- Se recomienda un mínimo de 4x4 para poder detectar secuencias mutantes",
      example = "[\"ATGCGA\", \"CAGTGC\", \"TTATGT\", \"AGAAGG\", \"CCCCTA\", \"TCACTG\"]",
      requiredMode = Schema.RequiredMode.REQUIRED
  )
  @NotNull(message = "La secuencia de ADN no puede ser nula")
  @NotEmpty(message = "La secuencia de ADN no puede estar vacía")
  @Size(min = 4, message = "La secuencia de ADN debe tener al menos 4 filas")
  private List<
      @NotNull(message = "Las filas de ADN no pueden ser nulas")
      @NotBlank(message = "Las filas de ADN no pueden estar vacías")
      @Pattern(
          regexp = "^[ATCG]+$",
          message = "Cada fila de ADN solo puede contener las letras A, T, C, G en mayúsculas"
      )
          String
      > adn;
}

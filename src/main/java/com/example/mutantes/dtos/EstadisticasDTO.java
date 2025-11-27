package com.example.mutantes.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(
    description = "DTO que contiene las estadísticas globales del sistema sobre la detección " +
        "de mutantes y humanos. Proporciona métricas agregadas para análisis estadístico " +
        "y reportes del sistema."
)
public class EstadisticasDTO {

  @Schema(
      description = "Cantidad total de personas identificadas como mutantes en el sistema. " +
          "Este contador se incrementa cada vez que se detecta una secuencia de ADN mutante. " +
          "Un mutante es aquel que tiene más de una secuencia de cuatro letras iguales " +
          "en forma oblicua, horizontal o vertical.",
      example = "40",
      minimum = "0",
      accessMode = Schema.AccessMode.READ_ONLY
  )
  @PositiveOrZero(message = "La cantidad de mutantes no puede ser negativa")
  private long cantidadMutantes;

  @Schema(
      description = "Cantidad total de personas identificadas como humanos (no mutantes) en el sistema. " +
          "Este contador incluye todas las personas cuyo ADN no cumple con los criterios " +
          "para ser clasificado como mutante.",
      example = "100",
      minimum = "0",
      accessMode = Schema.AccessMode.READ_ONLY
  )
  @PositiveOrZero(message = "La cantidad de humanos no puede ser negativa")
  private long cantidadHumanos;

  @Schema(
      description = "Ratio o proporción de mutantes respecto al total de humanos en el sistema. " +
          "Se calcula mediante la fórmula: ratio = cantidadMutantes / cantidadHumanos. " +
          "Consideraciones especiales: " +
          "- Si cantidadHumanos es 0, el ratio será 0.0 para evitar división por cero " +
          "- Si solo hay mutantes y ningún humano, el ratio será 0.0 " +
          "- Un ratio de 0.4 significa que hay 4 mutantes por cada 10 humanos " +
          "- Este valor es útil para análisis estadísticos y proyecciones demográficas",
      example = "0.4",
      minimum = "0.0",
      accessMode = Schema.AccessMode.READ_ONLY
  )
  @DecimalMin(value = "0.0", message = "El ratio no puede ser negativo")
  private double ratio;
}

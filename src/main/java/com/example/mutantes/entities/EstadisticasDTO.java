package com.example.mutantes.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class EstadisticasDTO {
  private long cantidadMutantes;
  private long cantidadHumanos;
  private double ratio;
}
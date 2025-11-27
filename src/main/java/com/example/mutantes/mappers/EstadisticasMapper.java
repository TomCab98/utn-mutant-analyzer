package com.example.mutantes.mappers;

import com.example.mutantes.dtos.EstadisticasDTO;
import com.example.mutantes.repositories.entities.Estadisticas;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EstadisticasMapper {

  EstadisticasDTO toDto(Estadisticas model);
}

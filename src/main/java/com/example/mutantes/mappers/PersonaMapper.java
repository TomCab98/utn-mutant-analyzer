package com.example.mutantes.mappers;

import com.example.mutantes.dtos.PersonaDTO;
import com.example.mutantes.repositories.entities.Persona;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PersonaMapper {

  PersonaDTO toDto(Persona model);

  @Mapping(target = "id", ignore = true)
  Persona toEntity(PersonaDTO dto);

  List<PersonaDTO> toDtoList(List<Persona> models);
}

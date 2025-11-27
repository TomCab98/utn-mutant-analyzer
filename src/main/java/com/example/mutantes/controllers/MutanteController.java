package com.example.mutantes.controllers;

import com.example.mutantes.dtos.EstadisticasDTO;
import com.example.mutantes.dtos.PersonaDTO;
import com.example.mutantes.services.MutanteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/api/mutant")
@Tag(name = "Mutante", description = "API para la gestión y análisis de mutantes")
public class MutanteController {

  private final MutanteService mutanteService;

  public MutanteController(MutanteService mutanteService) {
    this.mutanteService = mutanteService;
  }

  @Operation(
      summary = "Obtener todos los mutantes",
      description = "Retorna una lista completa de todas las personas registradas en el sistema, tanto mutantes como humanos"
  )
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "Lista de personas obtenida exitosamente",
          content = @Content(
              mediaType = "application/json",
              array = @ArraySchema(schema = @Schema(implementation = PersonaDTO.class))
          )
      )
  })
  @GetMapping
  public ResponseEntity<?> getAll() {
    List<PersonaDTO> mutantes = mutanteService.findAll();
    return ResponseEntity.status(HttpStatus.OK).body(mutantes);
  }

  @Operation(
      summary = "Obtener persona por ID",
      description = "Retorna los detalles de una persona específica identificada por su ID"
  )
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "Persona encontrada exitosamente",
          content = @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = PersonaDTO.class)
          )
      ),
      @ApiResponse(
          responseCode = "404",
          description = "Persona no encontrada"
      )
  })
  @GetMapping("/{id}")
  public ResponseEntity<?> get(
      @Parameter(description = "ID de la persona a buscar", required = true)
      @PathVariable Long id
  ) {
    PersonaDTO mutante = mutanteService.findById(id);
    return ResponseEntity.status(HttpStatus.OK).body(mutante);
  }

  @Operation(
      summary = "Actualizar persona",
      description = "Actualiza los datos de una persona existente en el sistema"
  )
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "Persona actualizada exitosamente",
          content = @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = PersonaDTO.class)
          )
      ),
      @ApiResponse(
          responseCode = "404",
          description = "Persona no encontrada"
      ),
      @ApiResponse(
          responseCode = "400",
          description = "Datos de entrada inválidos"
      )
  })
  @PutMapping("/{id}")
  public ResponseEntity<?> update(
      @Parameter(description = "ID de la persona a actualizar", required = true)
      @PathVariable Long id,
      @Parameter(description = "Datos actualizados de la persona", required = true)
      @Valid @RequestBody PersonaDTO entity
  ) {
    PersonaDTO mutante = mutanteService.update(id, entity);
    return ResponseEntity.status(HttpStatus.OK).body(mutante);
  }

  @Operation(
      summary = "Eliminar persona",
      description = "Elimina una persona del sistema de forma permanente"
  )
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "204",
          description = "Persona eliminada exitosamente"
      ),
      @ApiResponse(
          responseCode = "404",
          description = "Persona no encontrada"
      )
  })
  @DeleteMapping("/{id}")
  public ResponseEntity<?> delete(
      @Parameter(description = "ID de la persona a eliminar", required = true)
      @PathVariable Long id
  ) {
    mutanteService.delete(id);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  @Operation(
      summary = "Analizar ADN",
      description = "Analiza la secuencia de ADN de una persona para determinar si es mutante o humano. " +
          "Se considera mutante si se encuentran más de una secuencia de cuatro letras iguales " +
          "de forma oblicua, horizontal o vertical."
  )
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "Análisis completado exitosamente",
          content = @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = String.class, example = "La persona es un mutante")
          )
      ),
      @ApiResponse(
          responseCode = "400",
          description = "Secuencia de ADN inválida"
      )
  })
  @PostMapping("/analizar")
  public ResponseEntity<?> analyze(
      @Parameter(
          description = "Objeto persona con la secuencia de ADN a analizar. " +
              "El ADN debe ser una matriz NxN representada como un array de Strings.",
          required = true
      )
      @Valid @RequestBody PersonaDTO persona
  ) {
    boolean esMutante = mutanteService.analyze(persona);
    return esMutante ?
        ResponseEntity.status(HttpStatus.OK).body("La persona es un mutante") :
        ResponseEntity.status(HttpStatus.OK).body("La persona no es un mutante");
  }

  @Operation(
      summary = "Obtener estadísticas",
      description = "Retorna las estadísticas globales del sistema incluyendo la cantidad de mutantes, " +
          "humanos y el ratio entre ellos"
  )
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "Estadísticas obtenidas exitosamente",
          content = @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = EstadisticasDTO.class)
          )
      )
  })
  @GetMapping("/estadisticas")
  public ResponseEntity<EstadisticasDTO> obtenerEstadisticas() {
    return ResponseEntity.ok(mutanteService.obtenerEstadisticas());
  }
}
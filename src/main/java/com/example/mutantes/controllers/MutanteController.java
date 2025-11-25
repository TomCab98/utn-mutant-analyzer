package com.example.mutantes.controllers;

import com.example.mutantes.entities.Mutante;
import com.example.mutantes.entities.EstadisticasDTO;
import com.example.mutantes.services.MutanteService;
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
@RequestMapping(path = "/api/mutant/")
public class MutanteController {

  private final MutanteService mutanteService;

  public MutanteController(MutanteService mutanteService) {
    this.mutanteService = mutanteService;
  }

  @GetMapping("")
  public ResponseEntity<?> getAll() {
    List<Mutante> mutantes = mutanteService.findAll();

    return ResponseEntity.status(HttpStatus.OK).body(mutantes);
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> get(@PathVariable Long id) {
    Mutante mutante = mutanteService.findById(id);

    return ResponseEntity.status(HttpStatus.OK).body(mutante);
  }


  @PutMapping("/{id}")
  public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Mutante entity) {
    Mutante mutante = mutanteService.update(id, entity);

    return ResponseEntity.status(HttpStatus.OK).body(mutante);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> delete(@PathVariable Long id) {
    mutanteService.delete(id);

    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  @PostMapping("/analizar")
  public ResponseEntity<?> analyze(@RequestBody Mutante persona) {
    boolean esMutante = mutanteService.analyze(persona);
    return esMutante ?
        ResponseEntity.status(HttpStatus.OK).body("La persona es un mutante") :
        ResponseEntity.status(HttpStatus.OK).body("La persona no es un mutante");
  }

  @GetMapping("/estadisticas")
  public ResponseEntity<EstadisticasDTO> obtenerEstadisticas() {
    return ResponseEntity.ok(mutanteService.obtenerEstadisticas());
  }
}
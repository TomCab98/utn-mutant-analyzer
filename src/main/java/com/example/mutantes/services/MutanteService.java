package com.example.mutantes.services;

import com.example.mutantes.config.AuditContextHolder;
import com.example.mutantes.entities.Mutante;
import com.example.mutantes.entities.EstadisticasDTO;
import com.example.mutantes.exceptions.ArgumentoNoValidoException;
import com.example.mutantes.exceptions.MutanteNoEncontradoException;
import com.example.mutantes.repositories.MutanteRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Service;

@Service
public class MutanteService implements BaseService<Mutante> {

  private static final int SEQ_LENGTH = 4;
  private static final Set<String> MUTANT_SEQUENCES = Set.of("AAAA", "TTTT", "CCCC", "GGGG");

  private final MutanteRepository mutanteRepository;

  public MutanteService(MutanteRepository mutanteRepository) {
    this.mutanteRepository = mutanteRepository;
  }

  @Transactional
  public boolean analyze(Mutante persona) {
    validarEntradaADN(persona.getAdn());

    boolean resultado = isMutant(persona.getAdn());
    persona.setEsMutante(resultado);

    AuditContextHolder.setOperacion("CREATE");
    mutanteRepository.save(persona);
    return resultado;
  }

  @Override
  public List<Mutante> findAll() {
    return mutanteRepository.findAll();
  }

  @Override
  public Mutante findById(Long id) {
    return mutanteRepository.findById(id)
        .orElseThrow(() -> new MutanteNoEncontradoException("Mutante con id: " + id + " no encontrado"));
  }

  @Override
  public Mutante update(Long id, Mutante entity) {
    Mutante existente = findById(id);
    AuditContextHolder.setOperacion("UPDATE");
    return mutanteRepository.save(existente);
  }

  @Override
  public void delete(Long id) {
    if (!mutanteRepository.existsById(id)) {
      throw new MutanteNoEncontradoException("Mutante con id: " + id + " no encontrado");
    }
    AuditContextHolder.setOperacion("DELETE");
    mutanteRepository.deleteById(id);
  }

  protected boolean isMutant(List<String> adn) {
    int totalSecuencias = countSequencesInAllDirections(adn);

    return totalSecuencias > 1;
  }

  private int countSequencesInAllDirections(List<String> adn) {
    int count = 0;
    int n = adn.size();

    for (int i = 0; i < n; i++) {
      for (int j = 0; j < n; j++) {

        count += checkHorizontal(adn, i, j);
        count += checkVertical(adn, i, j);
        count += checkDiagonal(adn, i, j);
        count += checkDiagonalInversa(adn, i, j);

        if (count > 1) {
          return count;
        }
      }
    }

    return count;
  }

  private int checkHorizontal(List<String> adn, int row, int col) {
    if (col + SEQ_LENGTH > adn.size()) {
      return 0;
    }

    String seq = adn.get(row).substring(col, col + SEQ_LENGTH);
    return MUTANT_SEQUENCES.contains(seq) ? 1 : 0;
  }

  private int checkVertical(List<String> adn, int row, int col) {
    if (row + SEQ_LENGTH > adn.size()) {
      return 0;
    }

    StringBuilder sb = new StringBuilder(SEQ_LENGTH);
    for (int k = 0; k < SEQ_LENGTH; k++) {
      sb.append(adn.get(row + k).charAt(col));
    }
    return MUTANT_SEQUENCES.contains(sb.toString()) ? 1 : 0;
  }

  private int checkDiagonal(List<String> adn, int row, int col) {
    if (row + SEQ_LENGTH > adn.size() || col + SEQ_LENGTH > adn.size()) {
      return 0;
    }

    StringBuilder sb = new StringBuilder(SEQ_LENGTH);
    for (int k = 0; k < SEQ_LENGTH; k++) {
      sb.append(adn.get(row + k).charAt(col + k));
    }
    return MUTANT_SEQUENCES.contains(sb.toString()) ? 1 : 0;
  }

  private int checkDiagonalInversa(List<String> adn, int row, int col) {
    if (row + SEQ_LENGTH > adn.size() || col - (SEQ_LENGTH - 1) < 0) {
      return 0;
    }

    StringBuilder sb = new StringBuilder(SEQ_LENGTH);
    for (int k = 0; k < SEQ_LENGTH; k++) {
      sb.append(adn.get(row + k).charAt(col - k));
    }
    return MUTANT_SEQUENCES.contains(sb.toString()) ? 1 : 0;
  }

  private void validarEntradaADN(List<String> adn) {
    if (adn == null || adn.isEmpty()) {
      throw new ArgumentoNoValidoException("ADN invalido");
    }

    adn.replaceAll(String::toUpperCase);

    int n = adn.size();

    for (String fila : adn) {
      if (fila == null || fila.length() != n) {
        throw new ArgumentoNoValidoException("El ADN debe ser una matriz NxN");
      }

      if (!fila.matches("[ACGT]+")) {
        throw new ArgumentoNoValidoException("ADN contiene caracteres invalidos");
      }
    }
  }

  public EstadisticasDTO obtenerEstadisticas() {
    long totalMutantes = mutanteRepository.countByEsMutante(true);
    long totalHumanos = mutanteRepository.countByEsMutante(false);
    double ratio = totalHumanos == 0 ? 0 : (double) totalMutantes / totalHumanos;

    return new EstadisticasDTO(totalMutantes, totalHumanos, ratio);
  }
}

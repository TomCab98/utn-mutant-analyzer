package com.example.mutantes.services;

import com.example.mutantes.dtos.EstadisticasDTO;
import com.example.mutantes.dtos.PersonaDTO;
import com.example.mutantes.exceptions.ArgumentoNoValidoException;
import com.example.mutantes.exceptions.MutanteNoEncontradoException;
import com.example.mutantes.mappers.PersonaMapper;
import com.example.mutantes.repositories.PersonaRepository;
import com.example.mutantes.repositories.entities.Persona;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests del Servicio de Mutantes")
class MutanteServiceTest {

  @Mock
  private PersonaRepository personaRepository;

  @Mock
  private PersonaMapper personaMapper;

  @InjectMocks
  private MutanteService mutanteService;

  private PersonaDTO personaMutanteDTO;
  private PersonaDTO personaHumanoDTO;
  private Persona personaMutanteEntity;
  private Persona personaHumanoEntity;

  @BeforeEach
  void setUp() {
    List<String> adnMutante = Arrays.asList(
        "ATGCGA",
        "CAGTGC",
        "TTATGT",
        "AGAAGG",
        "CCCCTA",
        "TCACTG"
    );

    List<String> adnHumano = Arrays.asList(
        "ATGCGA",
        "CAGTGC",
        "TTATTT",
        "AGACGG",
        "GCGTCA",
        "TCACTG"
    );

    personaMutanteDTO = new PersonaDTO();
    personaMutanteDTO.setId(1L);
    personaMutanteDTO.setNombre("Charles");
    personaMutanteDTO.setApellido("Xavier");
    personaMutanteDTO.setEsMutante(true);
    personaMutanteDTO.setAdn(adnMutante);

    personaHumanoDTO = new PersonaDTO();
    personaHumanoDTO.setId(2L);
    personaHumanoDTO.setNombre("Peter");
    personaHumanoDTO.setApellido("Parker");
    personaHumanoDTO.setEsMutante(false);
    personaHumanoDTO.setAdn(adnHumano);

    personaMutanteEntity = new Persona();
    personaMutanteEntity.setId(1L);
    personaMutanteEntity.setNombre("Charles");
    personaMutanteEntity.setApellido("Xavier");
    personaMutanteEntity.setEsMutante(true);
    personaMutanteEntity.setAdn(adnMutante);

    personaHumanoEntity = new Persona();
    personaHumanoEntity.setId(2L);
    personaHumanoEntity.setNombre("Peter");
    personaHumanoEntity.setApellido("Parker");
    personaHumanoEntity.setEsMutante(false);
    personaHumanoEntity.setAdn(adnHumano);
  }

  @Nested
  @DisplayName("Tests del método analyze()")
  class AnalyzeTests {

    @Test
    @DisplayName("Debería detectar ADN mutante y guardar en base de datos")
    void testAnalyze_DeberiaDetectarMutanteYGuardar() {
      when(personaMapper.toEntity(any(PersonaDTO.class))).thenReturn(personaMutanteEntity);
      when(personaRepository.save(any(Persona.class))).thenReturn(personaMutanteEntity);

      boolean resultado = mutanteService.analyze(personaMutanteDTO);

      assertTrue(resultado, "Debería detectar que es mutante");
      verify(personaMapper, times(1)).toEntity(any(PersonaDTO.class));
      verify(personaRepository, times(1)).save(any(Persona.class));
    }

    @Test
    @DisplayName("Debería detectar ADN humano y guardar en base de datos")
    void testAnalyze_DeberiaDetectarHumanoYGuardar() {
      when(personaMapper.toEntity(any(PersonaDTO.class))).thenReturn(personaHumanoEntity);
      when(personaRepository.save(any(Persona.class))).thenReturn(personaHumanoEntity);

      boolean resultado = mutanteService.analyze(personaHumanoDTO);

      assertFalse(resultado, "Debería detectar que es humano");
      verify(personaMapper, times(1)).toEntity(any(PersonaDTO.class));
      verify(personaRepository, times(1)).save(any(Persona.class));
    }

    @Test
    @DisplayName("Debería lanzar excepción si ADN es null")
    void testAnalyze_DeberiaLanzarExcepcionSiAdnEsNull() {
      personaMutanteDTO.setAdn(null);
      Persona personaConAdnNull = new Persona();
      personaConAdnNull.setAdn(null);
      when(personaMapper.toEntity(any(PersonaDTO.class))).thenReturn(personaConAdnNull);

      assertThrows(ArgumentoNoValidoException.class, () -> {
        mutanteService.analyze(personaMutanteDTO);
      }, "Debería lanzar ArgumentoNoValidoException para ADN null");

      verify(personaRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debería lanzar excepción si ADN está vacío")
    void testAnalyze_DeberiaLanzarExcepcionSiAdnEstaVacio() {
      personaMutanteDTO.setAdn(Arrays.asList());
      Persona personaConAdnVacio = new Persona();
      personaConAdnVacio.setAdn(Arrays.asList());
      when(personaMapper.toEntity(any(PersonaDTO.class))).thenReturn(personaConAdnVacio);

      assertThrows(ArgumentoNoValidoException.class, () -> {
        mutanteService.analyze(personaMutanteDTO);
      }, "Debería lanzar ArgumentoNoValidoException para ADN vacío");

      verify(personaRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debería lanzar excepción si ADN no es NxN")
    void testAnalyze_DeberiaLanzarExcepcionSiAdnNoEsNxN() {
      List<String> adnInvalido = Arrays.asList(
          "ATGCGA",
          "CAGTGC",
          "TTATGT"
      );
      personaMutanteDTO.setAdn(adnInvalido);
      Persona personaConAdnInvalido = new Persona();
      personaConAdnInvalido.setAdn(adnInvalido);
      when(personaMapper.toEntity(any(PersonaDTO.class))).thenReturn(personaConAdnInvalido);

      assertThrows(ArgumentoNoValidoException.class, () -> {
        mutanteService.analyze(personaMutanteDTO);
      }, "Debería lanzar ArgumentoNoValidoException para ADN no NxN");

      verify(personaRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debería lanzar excepción si ADN contiene caracteres inválidos")
    void testAnalyze_DeberiaLanzarExcepcionSiAdnContieneCaracteresInvalidos() {
      List<String> adnInvalido = Arrays.asList(
          "ATGCGA",
          "CAGTGC",
          "TTXTGT",
          "AGAAGG",
          "CCCCTA",
          "TCACTG"
      );
      personaMutanteDTO.setAdn(adnInvalido);
      Persona personaConAdnInvalido = new Persona();
      personaConAdnInvalido.setAdn(adnInvalido);
      when(personaMapper.toEntity(any(PersonaDTO.class))).thenReturn(personaConAdnInvalido);

      assertThrows(ArgumentoNoValidoException.class, () -> {
        mutanteService.analyze(personaMutanteDTO);
      }, "Debería lanzar ArgumentoNoValidoException para caracteres inválidos");

      verify(personaRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debería detectar mutante con secuencias horizontales")
    void testAnalyze_DeberiaDetectarMutanteConSecuenciasHorizontales() {
      List<String> adnConHorizontales = Arrays.asList(
          "AAAATG",
          "CAGTGC",
          "TTATGT",
          "AGTTTT",
          "CCCCTA",
          "TCACTG"
      );
      personaMutanteDTO.setAdn(adnConHorizontales);
      Persona persona = new Persona();
      persona.setAdn(adnConHorizontales);
      when(personaMapper.toEntity(any(PersonaDTO.class))).thenReturn(persona);
      when(personaRepository.save(any(Persona.class))).thenReturn(persona);

      boolean resultado = mutanteService.analyze(personaMutanteDTO);

      assertTrue(resultado, "Debería detectar mutante con secuencias horizontales");
    }

    @Test
    @DisplayName("Debería detectar mutante con secuencias verticales")
    void testAnalyze_DeberiaDetectarMutanteConSecuenciasVerticales() {
      List<String> adnConVerticales = Arrays.asList(
          "ATGCGA",
          "AAGTGC",
          "ATATGT",
          "AGAAGG",
          "CCCCTA",
          "TCACTG"
      );
      personaMutanteDTO.setAdn(adnConVerticales);
      Persona persona = new Persona();
      persona.setAdn(adnConVerticales);
      when(personaMapper.toEntity(any(PersonaDTO.class))).thenReturn(persona);
      when(personaRepository.save(any(Persona.class))).thenReturn(persona);

      boolean resultado = mutanteService.analyze(personaMutanteDTO);

      assertTrue(resultado, "Debería detectar mutante con secuencias verticales");
    }

    @Test
    @DisplayName("Debería detectar mutante con secuencias diagonales")
    void testAnalyze_DeberiaDetectarMutanteConSecuenciasDiagonales() {
      List<String> adnConDiagonales = Arrays.asList(
          "ATGCGA",
          "CAGTGC",
          "TTATGT",
          "AGAAGG",
          "CCCCTA",
          "TCACTG"
      );
      personaMutanteDTO.setAdn(adnConDiagonales);
      Persona persona = new Persona();
      persona.setAdn(adnConDiagonales);
      when(personaMapper.toEntity(any(PersonaDTO.class))).thenReturn(persona);
      when(personaRepository.save(any(Persona.class))).thenReturn(persona);

      boolean resultado = mutanteService.analyze(personaMutanteDTO);

      assertTrue(resultado, "Debería detectar mutante con secuencias diagonales");
    }
  }

  @Nested
  @DisplayName("Tests del método findAll()")
  class FindAllTests {

    @Test
    @DisplayName("Debería retornar lista de todas las personas")
    void testFindAll_DeberiaRetornarListaDePersonas() {
      List<Persona> personas = Arrays.asList(personaMutanteEntity, personaHumanoEntity);
      List<PersonaDTO> personasDTO = Arrays.asList(personaMutanteDTO, personaHumanoDTO);

      when(personaRepository.findAll()).thenReturn(personas);
      when(personaMapper.toDtoList(personas)).thenReturn(personasDTO);

      List<PersonaDTO> resultado = mutanteService.findAll();

      assertNotNull(resultado);
      assertEquals(2, resultado.size());
      assertEquals("Charles", resultado.get(0).getNombre());
      assertEquals("Peter", resultado.get(1).getNombre());
      verify(personaRepository, times(1)).findAll();
      verify(personaMapper, times(1)).toDtoList(personas);
    }

    @Test
    @DisplayName("Debería retornar lista vacía cuando no hay personas")
    void testFindAll_DeberiaRetornarListaVaciaCuandoNoHayPersonas() {
      when(personaRepository.findAll()).thenReturn(Arrays.asList());
      when(personaMapper.toDtoList(any())).thenReturn(Arrays.asList());

      List<PersonaDTO> resultado = mutanteService.findAll();

      assertNotNull(resultado);
      assertTrue(resultado.isEmpty());
      verify(personaRepository, times(1)).findAll();
    }
  }

  @Nested
  @DisplayName("Tests del método findById()")
  class FindByIdTests {

    @Test
    @DisplayName("Debería retornar persona cuando existe")
    void testFindById_DeberiaRetornarPersonaCuandoExiste() {
      Long id = 1L;
      when(personaRepository.findById(id)).thenReturn(Optional.of(personaMutanteEntity));
      when(personaMapper.toDto(personaMutanteEntity)).thenReturn(personaMutanteDTO);

      PersonaDTO resultado = mutanteService.findById(id);

      assertNotNull(resultado);
      assertEquals(id, resultado.getId());
      assertEquals("Charles", resultado.getNombre());
      assertEquals("Xavier", resultado.getApellido());
      verify(personaRepository, times(1)).findById(id);
      verify(personaMapper, times(1)).toDto(personaMutanteEntity);
    }

    @Test
    @DisplayName("Debería lanzar excepción cuando persona no existe")
    void testFindById_DeberiaLanzarExcepcionCuandoNoExiste() {
      Long id = 999L;
      when(personaRepository.findById(id)).thenReturn(Optional.empty());

      MutanteNoEncontradoException exception = assertThrows(
          MutanteNoEncontradoException.class,
          () -> mutanteService.findById(id)
      );

      assertTrue(exception.getMessage().contains("no encontrado"));
      verify(personaRepository, times(1)).findById(id);
      verify(personaMapper, never()).toDto(any());
    }
  }

  @Nested
  @DisplayName("Tests del método update()")
  class UpdateTests {

    @Test
    @DisplayName("Debería actualizar persona existente")
    void testUpdate_DeberiaActualizarPersonaExistente() {
      Long id = 1L;
      when(personaRepository.findById(id)).thenReturn(Optional.of(personaMutanteEntity));
      when(personaMapper.toDto(personaMutanteEntity)).thenReturn(personaMutanteDTO);
      when(personaMapper.toEntity(personaMutanteDTO)).thenReturn(personaMutanteEntity);
      when(personaRepository.save(personaMutanteEntity)).thenReturn(personaMutanteEntity);

      PersonaDTO resultado = mutanteService.update(id, personaMutanteDTO);

      assertNotNull(resultado);
      assertEquals(id, resultado.getId());
      verify(personaRepository, times(1)).findById(id);
      verify(personaRepository, times(1)).save(any(Persona.class));
    }

    @Test
    @DisplayName("Debería lanzar excepción al actualizar persona inexistente")
    void testUpdate_DeberiaLanzarExcepcionSiNoExiste() {
      Long id = 999L;
      when(personaRepository.findById(id)).thenReturn(Optional.empty());

      assertThrows(MutanteNoEncontradoException.class, () -> {
        mutanteService.update(id, personaMutanteDTO);
      });

      verify(personaRepository, times(1)).findById(id);
      verify(personaRepository, never()).save(any());
    }
  }

  @Nested
  @DisplayName("Tests del método delete()")
  class DeleteTests {

    @Test
    @DisplayName("Debería eliminar persona cuando existe")
    void testDelete_DeberiaEliminarPersonaCuandoExiste() {
      Long id = 1L;
      when(personaRepository.existsById(id)).thenReturn(true);
      doNothing().when(personaRepository).deleteById(id);

      mutanteService.delete(id);

      verify(personaRepository, times(1)).existsById(id);
      verify(personaRepository, times(1)).deleteById(id);
    }

    @Test
    @DisplayName("Debería lanzar excepción al eliminar persona inexistente")
    void testDelete_DeberiaLanzarExcepcionCuandoNoExiste() {
      Long id = 999L;
      when(personaRepository.existsById(id)).thenReturn(false);

      MutanteNoEncontradoException exception = assertThrows(
          MutanteNoEncontradoException.class,
          () -> mutanteService.delete(id)
      );

      assertTrue(exception.getMessage().contains("no encontrado"));
      verify(personaRepository, times(1)).existsById(id);
      verify(personaRepository, never()).deleteById(any());
    }
  }

  @Nested
  @DisplayName("Tests del método obtenerEstadisticas()")
  class ObtenerEstadisticasTests {

    @Test
    @DisplayName("Debería calcular estadísticas correctamente")
    void testObtenerEstadisticas_DeberiaCalcularCorrectamente() {
      when(personaRepository.countByEsMutante(true)).thenReturn(40L);
      when(personaRepository.countByEsMutante(false)).thenReturn(100L);

      EstadisticasDTO resultado = mutanteService.obtenerEstadisticas();

      assertNotNull(resultado);
      assertEquals(40L, resultado.getCantidadMutantes());
      assertEquals(100L, resultado.getCantidadHumanos());
      assertEquals(0.4, resultado.getRatio(), 0.001);
      verify(personaRepository, times(1)).countByEsMutante(true);
      verify(personaRepository, times(1)).countByEsMutante(false);
    }

    @Test
    @DisplayName("Debería retornar ratio 0 cuando no hay humanos")
    void testObtenerEstadisticas_DeberiaRetornarRatioCeroCuandoNoHayHumanos() {
      when(personaRepository.countByEsMutante(true)).thenReturn(10L);
      when(personaRepository.countByEsMutante(false)).thenReturn(0L);

      EstadisticasDTO resultado = mutanteService.obtenerEstadisticas();

      assertNotNull(resultado);
      assertEquals(10L, resultado.getCantidadMutantes());
      assertEquals(0L, resultado.getCantidadHumanos());
      assertEquals(0.0, resultado.getRatio(), 0.001);
    }

    @Test
    @DisplayName("Debería retornar estadísticas vacías cuando no hay datos")
    void testObtenerEstadisticas_DeberiaRetornarEstadisticasVacias() {
      when(personaRepository.countByEsMutante(true)).thenReturn(0L);
      when(personaRepository.countByEsMutante(false)).thenReturn(0L);

      EstadisticasDTO resultado = mutanteService.obtenerEstadisticas();

      assertNotNull(resultado);
      assertEquals(0L, resultado.getCantidadMutantes());
      assertEquals(0L, resultado.getCantidadHumanos());
      assertEquals(0.0, resultado.getRatio(), 0.001);
    }

    @Test
    @DisplayName("Debería calcular ratio correctamente con más humanos que mutantes")
    void testObtenerEstadisticas_DeberiaCalcularRatioConMasHumanos() {
      when(personaRepository.countByEsMutante(true)).thenReturn(25L);
      when(personaRepository.countByEsMutante(false)).thenReturn(100L);

      EstadisticasDTO resultado = mutanteService.obtenerEstadisticas();

      assertNotNull(resultado);
      assertEquals(25L, resultado.getCantidadMutantes());
      assertEquals(100L, resultado.getCantidadHumanos());
      assertEquals(0.25, resultado.getRatio(), 0.001);
    }

    @Test
    @DisplayName("Debería calcular ratio correctamente con igual cantidad")
    void testObtenerEstadisticas_DeberiaCalcularRatioConIgualCantidad() {
      when(personaRepository.countByEsMutante(true)).thenReturn(50L);
      when(personaRepository.countByEsMutante(false)).thenReturn(50L);

      EstadisticasDTO resultado = mutanteService.obtenerEstadisticas();

      assertNotNull(resultado);
      assertEquals(50L, resultado.getCantidadMutantes());
      assertEquals(50L, resultado.getCantidadHumanos());
      assertEquals(1.0, resultado.getRatio(), 0.001);
    }
  }

  @Nested
  @DisplayName("Tests del método isMutant() - Casos de borde")
  class IsMutantTests {

    @Test
    @DisplayName("Debería detectar mutante con exactamente 2 secuencias")
    void testIsMutant_DeberiaDetectarMutanteConDosSecuencias() {
      List<String> adn = Arrays.asList(
          "AAAATG",
          "CAGTGC",
          "TTATGT",
          "AGTTTT",
          "CCTCTA",
          "TCACTG"
      );
      Persona persona = new Persona();
      persona.setAdn(adn);
      when(personaMapper.toEntity(any(PersonaDTO.class))).thenReturn(persona);
      when(personaRepository.save(any(Persona.class))).thenReturn(persona);

      PersonaDTO dto = new PersonaDTO();
      dto.setNombre("Test");
      dto.setApellido("Mutante");
      dto.setAdn(adn);

      boolean resultado = mutanteService.analyze(dto);

      assertTrue(resultado, "Debería detectar mutante con exactamente 2 secuencias");
    }

    @Test
    @DisplayName("No debería detectar mutante con solo 1 secuencia")
    void testIsMutant_NoDeberiaDetectarMutanteConUnaSecuencia() {
      List<String> adn = Arrays.asList(
          "AAAATG",
          "CAGTGC",
          "TTATGT",
          "AGACGG",
          "CCTCTA",
          "TCACTG"
      );
      Persona persona = new Persona();
      persona.setAdn(adn);
      when(personaMapper.toEntity(any(PersonaDTO.class))).thenReturn(persona);
      when(personaRepository.save(any(Persona.class))).thenReturn(persona);

      PersonaDTO dto = new PersonaDTO();
      dto.setNombre("Test");
      dto.setApellido("Humano");
      dto.setAdn(adn);

      boolean resultado = mutanteService.analyze(dto);

      assertFalse(resultado, "No debería detectar mutante con solo 1 secuencia");
    }

    @Test
    @DisplayName("Debería detectar mutante con ADN 4x4 mínimo")
    void testIsMutant_DeberiaDetectarMutanteConAdn4x4() {
      List<String> adn = Arrays.asList(
          "AAAA",
          "CCCC",
          "TAGT",
          "GGGG"
      );
      Persona persona = new Persona();
      persona.setAdn(adn);
      when(personaMapper.toEntity(any(PersonaDTO.class))).thenReturn(persona);
      when(personaRepository.save(any(Persona.class))).thenReturn(persona);

      PersonaDTO dto = new PersonaDTO();
      dto.setNombre("Test");
      dto.setApellido("Mutante4x4");
      dto.setAdn(adn);

      boolean resultado = mutanteService.analyze(dto);

      assertTrue(resultado, "Debería detectar mutante con ADN 4x4");
    }
  }
}

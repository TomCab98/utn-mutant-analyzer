package com.example.mutantes.controllers;

import com.example.mutantes.dtos.EstadisticasDTO;
import com.example.mutantes.dtos.PersonaDTO;
import com.example.mutantes.services.MutanteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MutanteController.class)
@DisplayName("Tests del Controlador de Mutantes")
class MutanteControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private MutanteService mutanteService;

  private PersonaDTO personaMutante;
  private PersonaDTO personaHumano;
  private List<String> adnMutante;
  private List<String> adnHumano;

  @BeforeEach
  void setUp() {
    adnMutante = Arrays.asList(
        "ATGCGA",
        "CAGTGC",
        "TTATGT",
        "AGAAGG",
        "CCCCTA",
        "TCACTG"
    );

    adnHumano = Arrays.asList(
        "ATGCGA",
        "CAGTGC",
        "TTATTT",
        "AGACGG",
        "GCGTCA",
        "TCACTG"
    );

    personaMutante = new PersonaDTO();
    personaMutante.setId(1L);
    personaMutante.setNombre("Charles");
    personaMutante.setApellido("Xavier");
    personaMutante.setEsMutante(true);
    personaMutante.setAdn(adnMutante);

    personaHumano = new PersonaDTO();
    personaHumano.setId(2L);
    personaHumano.setNombre("Peter");
    personaHumano.setApellido("Parker");
    personaHumano.setEsMutante(false);
    personaHumano.setAdn(adnHumano);
  }

  @Test
  @DisplayName("GET /api/mutant - Debería retornar lista de todas las personas")
  void testGetAll_DeberiaRetornarListaDePersonas() throws Exception {
    List<PersonaDTO> personas = Arrays.asList(personaMutante, personaHumano);
    when(mutanteService.findAll()).thenReturn(personas);

    mockMvc.perform(get("/api/mutant")
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$[0].id", is(1)))
        .andExpect(jsonPath("$[0].nombre", is("Charles")))
        .andExpect(jsonPath("$[0].apellido", is("Xavier")))
        .andExpect(jsonPath("$[0].esMutante", is(true)))
        .andExpect(jsonPath("$[1].id", is(2)))
        .andExpect(jsonPath("$[1].nombre", is("Peter")))
        .andExpect(jsonPath("$[1].esMutante", is(false)));

    verify(mutanteService, times(1)).findAll();
  }

  @Test
  @DisplayName("GET /api/mutant - Debería retornar lista vacía cuando no hay personas")
  void testGetAll_DeberiaRetornarListaVacia() throws Exception {
    when(mutanteService.findAll()).thenReturn(List.of());

    mockMvc.perform(get("/api/mutant")
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(0)));

    verify(mutanteService, times(1)).findAll();
  }

  @Test
  @DisplayName("GET /api/mutant/{id} - Debería retornar persona por ID")
  void testGet_DeberiaRetornarPersonaPorId() throws Exception {
    Long id = 1L;
    when(mutanteService.findById(id)).thenReturn(personaMutante);

    mockMvc.perform(get("/api/mutant/{id}", id)
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id", is(1)))
        .andExpect(jsonPath("$.nombre", is("Charles")))
        .andExpect(jsonPath("$.apellido", is("Xavier")))
        .andExpect(jsonPath("$.esMutante", is(true)));

    verify(mutanteService, times(1)).findById(id);
  }

  @Test
  @DisplayName("PUT /api/mutant/{id} - Debería actualizar persona existente")
  void testUpdate_DeberiaActualizarPersona() throws Exception {
    Long id = 1L;
    PersonaDTO personaActualizada = new PersonaDTO();
    personaActualizada.setId(id);
    personaActualizada.setNombre("Erik");
    personaActualizada.setApellido("Lehnsherr");
    personaActualizada.setEsMutante(true);
    personaActualizada.setAdn(adnMutante);

    when(mutanteService.update(eq(id), any(PersonaDTO.class))).thenReturn(personaActualizada);

    mockMvc.perform(put("/api/mutant/{id}", id)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(personaActualizada)))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id", is(1)))
        .andExpect(jsonPath("$.nombre", is("Erik")))
        .andExpect(jsonPath("$.apellido", is("Lehnsherr")))
        .andExpect(jsonPath("$.esMutante", is(true)));

    verify(mutanteService, times(1)).update(eq(id), any(PersonaDTO.class));
  }

  @Test
  @DisplayName("PUT /api/mutant/{id} - Debería fallar con datos inválidos")
  void testUpdate_DeberiaFallarConDatosInvalidos() throws Exception {
    Long id = 1L;
    PersonaDTO personaInvalida = new PersonaDTO();
    personaInvalida.setId(id);
    personaInvalida.setNombre("");
    personaInvalida.setApellido("Test");
    personaInvalida.setAdn(adnMutante);

    mockMvc.perform(put("/api/mutant/{id}", id)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(personaInvalida)))
        .andDo(print())
        .andExpect(status().isBadRequest());

    verify(mutanteService, never()).update(any(), any());
  }

  @Test
  @DisplayName("DELETE /api/mutant/{id} - Debería eliminar persona")
  void testDelete_DeberiaEliminarPersona() throws Exception {
    Long id = 1L;
    doNothing().when(mutanteService).delete(id);

    mockMvc.perform(delete("/api/mutant/{id}", id)
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isNoContent());

    verify(mutanteService, times(1)).delete(id);
  }

  @Test
  @DisplayName("POST /api/mutant/analizar - Debería detectar mutante correctamente")
  void testAnalyze_DeberiaDetectarMutante() throws Exception {
    PersonaDTO personaParaAnalizar = new PersonaDTO();
    personaParaAnalizar.setNombre("Logan");
    personaParaAnalizar.setApellido("Howlett");
    personaParaAnalizar.setAdn(adnMutante);

    when(mutanteService.analyze(any(PersonaDTO.class))).thenReturn(true);

    mockMvc.perform(post("/api/mutant/analizar")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(personaParaAnalizar)))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().string("La persona es un mutante"));

    verify(mutanteService, times(1)).analyze(any(PersonaDTO.class));
  }

  @Test
  @DisplayName("POST /api/mutant/analizar - Debería detectar humano correctamente")
  void testAnalyze_DeberiaDetectarHumano() throws Exception {
    PersonaDTO personaParaAnalizar = new PersonaDTO();
    personaParaAnalizar.setNombre("Steve");
    personaParaAnalizar.setApellido("Rogers");
    personaParaAnalizar.setAdn(adnHumano);

    when(mutanteService.analyze(any(PersonaDTO.class))).thenReturn(false);

    mockMvc.perform(post("/api/mutant/analizar")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(personaParaAnalizar)))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().string("La persona no es un mutante"));

    verify(mutanteService, times(1)).analyze(any(PersonaDTO.class));
  }

  @Test
  @DisplayName("POST /api/mutant/analizar - Debería fallar con ADN inválido (null)")
  void testAnalyze_DeberiaFallarConAdnNull() throws Exception {
    PersonaDTO personaInvalida = new PersonaDTO();
    personaInvalida.setNombre("Tony");
    personaInvalida.setApellido("Stark");
    personaInvalida.setAdn(null);

    mockMvc.perform(post("/api/mutant/analizar")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(personaInvalida)))
        .andDo(print())
        .andExpect(status().isBadRequest());

    verify(mutanteService, never()).analyze(any());
  }

  @Test
  @DisplayName("POST /api/mutant/analizar - Debería fallar con ADN vacío")
  void testAnalyze_DeberiaFallarConAdnVacio() throws Exception {
    PersonaDTO personaInvalida = new PersonaDTO();
    personaInvalida.setNombre("Bruce");
    personaInvalida.setApellido("Banner");
    personaInvalida.setAdn(List.of());

    mockMvc.perform(post("/api/mutant/analizar")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(personaInvalida)))
        .andDo(print())
        .andExpect(status().isBadRequest());

    verify(mutanteService, never()).analyze(any());
  }

  @Test
  @DisplayName("POST /api/mutant/analizar - Debería fallar con ADN con caracteres inválidos")
  void testAnalyze_DeberiaFallarConAdnCaracteresInvalidos() throws Exception {
    PersonaDTO personaInvalida = new PersonaDTO();
    personaInvalida.setNombre("Natasha");
    personaInvalida.setApellido("Romanoff");
    personaInvalida.setAdn(Arrays.asList(
        "ATGCGA",
        "CAGTGC",
        "TTXTGT",
        "AGAAGG"
    ));

    mockMvc.perform(post("/api/mutant/analizar")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(personaInvalida)))
        .andDo(print())
        .andExpect(status().isBadRequest());

    verify(mutanteService, never()).analyze(any());
  }

  @Test
  @DisplayName("POST /api/mutant/analizar - Debería fallar con nombre vacío")
  void testAnalyze_DeberiaFallarConNombreVacio() throws Exception {
    PersonaDTO personaInvalida = new PersonaDTO();
    personaInvalida.setNombre("");
    personaInvalida.setApellido("Strange");
    personaInvalida.setAdn(adnMutante);

    mockMvc.perform(post("/api/mutant/analizar")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(personaInvalida)))
        .andDo(print())
        .andExpect(status().isBadRequest());

    verify(mutanteService, never()).analyze(any());
  }

  @Test
  @DisplayName("POST /api/mutant/analizar - Debería fallar con apellido vacío")
  void testAnalyze_DeberiaFallarConApellidoVacio() throws Exception {
    PersonaDTO personaInvalida = new PersonaDTO();
    personaInvalida.setNombre("Stephen");
    personaInvalida.setApellido("");
    personaInvalida.setAdn(adnMutante);

    mockMvc.perform(post("/api/mutant/analizar")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(personaInvalida)))
        .andDo(print())
        .andExpect(status().isBadRequest());

    verify(mutanteService, never()).analyze(any());
  }

  @Test
  @DisplayName("GET /api/mutant/estadisticas - Debería retornar estadísticas correctamente")
  void testObtenerEstadisticas_DeberiaRetornarEstadisticas() throws Exception {
    EstadisticasDTO estadisticas = new EstadisticasDTO(40L, 100L, 0.4);
    when(mutanteService.obtenerEstadisticas()).thenReturn(estadisticas);

    mockMvc.perform(get("/api/mutant/estadisticas")
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.cantidadMutantes", is(40)))
        .andExpect(jsonPath("$.cantidadHumanos", is(100)))
        .andExpect(jsonPath("$.ratio", is(0.4)));

    verify(mutanteService, times(1)).obtenerEstadisticas();
  }

  @Test
  @DisplayName("GET /api/mutant/estadisticas - Debería retornar estadísticas con ratio 0 cuando no hay humanos")
  void testObtenerEstadisticas_DeberiaRetornarRatioCeroCuandoNoHayHumanos() throws Exception {
    EstadisticasDTO estadisticas = new EstadisticasDTO(10L, 0L, 0.0);
    when(mutanteService.obtenerEstadisticas()).thenReturn(estadisticas);

    mockMvc.perform(get("/api/mutant/estadisticas")
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.cantidadMutantes", is(10)))
        .andExpect(jsonPath("$.cantidadHumanos", is(0)))
        .andExpect(jsonPath("$.ratio", is(0.0)));

    verify(mutanteService, times(1)).obtenerEstadisticas();
  }

  @Test
  @DisplayName("GET /api/mutant/estadisticas - Debería retornar estadísticas vacías")
  void testObtenerEstadisticas_DeberiaRetornarEstadisticasVacias() throws Exception {
    EstadisticasDTO estadisticas = new EstadisticasDTO(0L, 0L, 0.0);
    when(mutanteService.obtenerEstadisticas()).thenReturn(estadisticas);

    mockMvc.perform(get("/api/mutant/estadisticas")
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.cantidadMutantes", is(0)))
        .andExpect(jsonPath("$.cantidadHumanos", is(0)))
        .andExpect(jsonPath("$.ratio", is(0.0)));

    verify(mutanteService, times(1)).obtenerEstadisticas();
  }
}
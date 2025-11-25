package com.example.mutantes.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;

@Entity
@Table(name = "persona")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Audited
public class Mutante {

  @Id
  @GeneratedValue
  private Long id;

  private String nombre;

  private String apellido;

  @Column(name = "es_mutante")
  private boolean esMutante;

  private List<String> adn;
}

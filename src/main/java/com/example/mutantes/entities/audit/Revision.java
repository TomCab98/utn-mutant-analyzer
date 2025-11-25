package com.example.mutantes.entities.audit;

import com.example.mutantes.config.CustomRevisionListener;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import org.hibernate.envers.RevisionEntity;
import org.hibernate.envers.RevisionNumber;
import org.hibernate.envers.RevisionTimestamp;

@Entity
@Table
@RevisionEntity(CustomRevisionListener.class)
@Data
public class Revision implements Serializable {

  private static final Long serialVersionUID = 1L;

  @Id
  @GeneratedValue
  @SequenceGenerator(name = "revision_seq", sequenceName = "rback.seq_revision_id")
  @RevisionNumber
  private int id;

  @Column(name = "REVISION_DATE")
  @Temporal(TemporalType.TIMESTAMP)
  @RevisionTimestamp
  private Date date;

  @Column(name = "operacion")
  private String operacion;
}


package com.silenteight.scb.ingest.adapter.incomming.common.store.rawalert;

import lombok.*;

import com.silenteight.sep.base.common.entity.BaseEntity;

import org.hibernate.annotations.Type;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@Data
@Setter(AccessLevel.NONE)
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "scb_raw_alert")
public class RawAlert extends BaseEntity {

  @Id
  @Column(name = "id", insertable = false, updatable = false, nullable = false)
  @GeneratedValue(strategy = IDENTITY)
  private Long id;

  @NonNull
  private String systemId;

  @NonNull
  private String batchId;

  @NonNull
  private String internalBatchId;

  @NonNull
  @Enumerated(EnumType.STRING)
  private AlertType alertType;

  @NonNull
  @Lob
  @Type(type = "org.hibernate.type.BinaryType")
  private byte[] payload;

  RawAlert(
      @NonNull String systemId,
      @NonNull String batchId,
      @NonNull String internalBatchId,
      @NonNull AlertType alertType,
      @NonNull byte[] payload) {
    this.systemId = systemId;
    this.batchId = batchId;
    this.internalBatchId = internalBatchId;
    this.alertType = alertType;
    this.payload = payload;
  }

  enum AlertType {
    SOLVING, LEARNING
  }

}

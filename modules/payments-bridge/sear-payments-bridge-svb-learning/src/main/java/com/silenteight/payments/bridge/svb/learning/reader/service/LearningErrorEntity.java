package com.silenteight.payments.bridge.svb.learning.reader.service;

import lombok.*;
import lombok.EqualsAndHashCode.Include;

import com.silenteight.payments.bridge.svb.learning.reader.domain.ReadAlertError;
import com.silenteight.sep.base.common.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.*;

@Data
@AllArgsConstructor(access = PRIVATE)
@NoArgsConstructor(access = PROTECTED)
@Setter(NONE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Entity(name = "LearningError")
class LearningErrorEntity extends BaseEntity {

  @Id
  @Column(name = "error_id", insertable = false, updatable = false, nullable = false)
  @GeneratedValue(strategy = IDENTITY)
  @Setter(PUBLIC)
  @Include
  private Long id;

  @Column(updatable = false, nullable = false)
  @NonNull
  private String alertId;

  @Column(updatable = false, nullable = false)
  @NonNull
  private String error;

  @Column(updatable = false, nullable = false)
  @NonNull
  @Setter(PACKAGE)
  private String fileName;

  @Column(updatable = false, nullable = false)
  @NonNull
  @Setter(PACKAGE)
  private String batchStamp;


  public LearningErrorEntity(ReadAlertError error) {
    this.alertId = error.getAlertId();
    this.error = error.getException().getMessage();
  }

}

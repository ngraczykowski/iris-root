package com.silenteight.adjudication.engine.analysis.analysis;

import lombok.*;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Transient;

import static lombok.AccessLevel.NONE;
import static lombok.AccessLevel.PACKAGE;
import static lombok.AccessLevel.PROTECTED;

@Data
@AllArgsConstructor(access = PACKAGE)
@NoArgsConstructor(access = PROTECTED)
@Setter(NONE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
class AnalysisAlertKey implements Serializable {

  private static final long serialVersionUID = 6925160290708786662L;

  @Column(nullable = false)
  @NonNull
  private Long analysisId;

  @Column(nullable = false)
  @NonNull
  private Long alertId;

  @Transient
  String getName() {
    return "analysis/" + getAnalysisId() + "/alerts/" + getAlertId();
  }

  @Transient
  String getAlertName() {
    return "alerts/" + getAlertId();
  }
}

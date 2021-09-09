package com.silenteight.adjudication.engine.analysis.analysis;

import lombok.*;

import java.io.Serializable;
import javax.persistence.Column;

import static lombok.AccessLevel.NONE;
import static lombok.AccessLevel.PACKAGE;
import static lombok.AccessLevel.PROTECTED;

@Data
@AllArgsConstructor(access = PACKAGE)
@NoArgsConstructor(access = PROTECTED)
@Setter(NONE)
@EqualsAndHashCode(callSuper = false)
class AnalysisDatasetKey implements Serializable {

  private static final long serialVersionUID = 6925160290708786662L;

  @Column(nullable = false)
  @NonNull
  private Long analysisId;

  @Column(nullable = false)
  @NonNull
  private Long datasetId;

  String toName() {
    return "analysis/" + analysisId + "/datasets/" + datasetId;
  }
}

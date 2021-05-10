package com.silenteight.adjudication.engine.analysis.analysis;

import lombok.*;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Transient;

import static lombok.AccessLevel.NONE;
import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

@Data
@AllArgsConstructor(access = PRIVATE)
@NoArgsConstructor(access = PROTECTED)
@Setter(NONE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Entity(name = "AnalysisDataset")
class AnalysisDatasetEntity {

  @EmbeddedId
  private AnalysisDatasetKey id;

  AnalysisDatasetEntity(long analysisId, long datasetId) {
    id = new AnalysisDatasetKey(analysisId, datasetId);
  }

  @Transient
  String getName() {
    return "analysis/" + id.getAnalysisId() + "/datasets/" + id.getDatasetId();
  }
}

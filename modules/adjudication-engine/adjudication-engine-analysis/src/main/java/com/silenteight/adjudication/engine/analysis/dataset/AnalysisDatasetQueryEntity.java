package com.silenteight.adjudication.engine.analysis.dataset;

import lombok.*;

import com.silenteight.adjudication.api.v1.AnalysisDataset;

import org.hibernate.annotations.Immutable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

import static lombok.AccessLevel.NONE;
import static lombok.AccessLevel.PACKAGE;
import static lombok.AccessLevel.PROTECTED;

@Data
@AllArgsConstructor(access = PACKAGE)
@NoArgsConstructor(access = PROTECTED)
@Setter(NONE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Immutable
@Entity(name = "AnalysisDatasetQuery")
@Builder(access = PACKAGE)
class AnalysisDatasetQueryEntity {

  @EmbeddedId
  private AnalysisDatasetKey id;

  private Long alertCount;

  AnalysisDataset toAnalysisDataset() {
    return AnalysisDataset
        .newBuilder()
        .setName("analysis/" + id.getAnalysisId() + "/datasets/" + id.getDatasetId())
        .setPendingAlerts(0) // todo tkleszcz: implement pending alerts
        .setAlertCount(alertCount)
        .build();
  }
}

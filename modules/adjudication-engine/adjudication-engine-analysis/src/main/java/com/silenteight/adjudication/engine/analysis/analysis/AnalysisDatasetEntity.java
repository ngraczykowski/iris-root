package com.silenteight.adjudication.engine.analysis.analysis;

import lombok.*;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

import static lombok.AccessLevel.NONE;
import static lombok.AccessLevel.PACKAGE;
import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

@Data
@AllArgsConstructor(access = PRIVATE)
@NoArgsConstructor(access = PROTECTED)
@Setter(NONE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Entity(name = "AnalysisDataset")
@Builder(access = PACKAGE)
class AnalysisDatasetEntity {

  @EmbeddedId
  private AnalysisDatasetKey id;
}

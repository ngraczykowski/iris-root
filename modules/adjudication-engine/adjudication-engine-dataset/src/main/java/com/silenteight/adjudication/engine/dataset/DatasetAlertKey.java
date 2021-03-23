package com.silenteight.adjudication.engine.dataset;

import lombok.*;

import java.io.Serializable;
import javax.persistence.Column;

import static lombok.AccessLevel.NONE;
import static lombok.AccessLevel.PACKAGE;
import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

@Data
@AllArgsConstructor(access = PRIVATE)
@NoArgsConstructor(access = PROTECTED)
@Setter(NONE)
@Builder(access = PACKAGE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
class DatasetAlertKey implements Serializable {

  @Column(nullable = false)
  @NonNull
  private Long datasetId;

  @Column(nullable = false)
  @NonNull
  private Long alertId;

}

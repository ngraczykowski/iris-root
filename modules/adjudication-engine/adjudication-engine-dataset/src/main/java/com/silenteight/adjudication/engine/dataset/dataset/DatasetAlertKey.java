package com.silenteight.adjudication.engine.dataset.dataset;

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

  private static final long serialVersionUID = 6925160290708786662L;

  @Column(nullable = false)
  @NonNull
  private Long datasetId;

  @Column(nullable = false)
  @NonNull
  private Long alertId;
}

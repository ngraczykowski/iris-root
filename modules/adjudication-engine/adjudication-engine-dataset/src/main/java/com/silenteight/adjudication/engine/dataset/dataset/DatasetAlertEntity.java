package com.silenteight.adjudication.engine.dataset.dataset;

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
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Entity(name = "DatasetAlert")
@Setter(NONE)
@Builder(access = PACKAGE)
class DatasetAlertEntity {

  @EmbeddedId
  private DatasetAlertKey id;

}

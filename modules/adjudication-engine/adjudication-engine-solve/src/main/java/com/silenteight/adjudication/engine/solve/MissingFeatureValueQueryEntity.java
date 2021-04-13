package com.silenteight.adjudication.engine.solve;

import lombok.*;

import org.hibernate.annotations.Immutable;

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
@Immutable
@Entity(name = "MissingFeatureValueQuery")
@Builder(access = PACKAGE)
class MissingFeatureValueQueryEntity {

  @EmbeddedId
  private MissingFeatureValueKey id;
}

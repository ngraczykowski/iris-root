package com.silenteight.adjudication.engine.solve;

import lombok.*;

import com.silenteight.sep.base.common.entity.BaseEntity;

import org.hibernate.annotations.Immutable;

import javax.persistence.EmbeddedId;

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
@javax.persistence.Entity(name = "AgentExchangeMatchFeatures")
@Builder(access = PACKAGE)
class AgentExchangeMatchFeaturesEntity extends BaseEntity {

  @EmbeddedId
  private AgentExchangeMatchFeaturesKey id;
}

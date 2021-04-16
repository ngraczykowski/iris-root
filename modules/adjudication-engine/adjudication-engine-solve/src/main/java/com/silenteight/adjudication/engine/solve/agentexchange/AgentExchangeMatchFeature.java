package com.silenteight.adjudication.engine.solve.agentexchange;

import lombok.*;

import com.silenteight.sep.base.common.entity.BaseEntity;

import org.hibernate.annotations.Immutable;

import javax.persistence.Column;
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
@Entity
@Builder(access = PACKAGE)
class AgentExchangeMatchFeature extends BaseEntity {

  @EmbeddedId
  private AgentExchangeMatchFeatureKey id;

  @Column(updatable = false, nullable = false)
  @NonNull
  private String feature;
}

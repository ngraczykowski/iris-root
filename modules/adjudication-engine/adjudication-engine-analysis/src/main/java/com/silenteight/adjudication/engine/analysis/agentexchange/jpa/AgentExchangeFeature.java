package com.silenteight.adjudication.engine.analysis.agentexchange.jpa;

import lombok.*;
import lombok.EqualsAndHashCode.Include;

import com.silenteight.sep.base.common.entity.BaseEntity;

import javax.persistence.*;

import static javax.persistence.GenerationType.SEQUENCE;
import static lombok.AccessLevel.NONE;
import static lombok.AccessLevel.PACKAGE;
import static lombok.AccessLevel.PROTECTED;

@Data
@RequiredArgsConstructor(access = PACKAGE)
@NoArgsConstructor(access = PROTECTED)
@Setter(NONE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Entity
class AgentExchangeFeature extends BaseEntity {

  @Id
  @Column(name = "agent_exchange_feature_id", updatable = false, nullable = false)
  @GeneratedValue(strategy = SEQUENCE, generator = "agentExchangeFeature")
  @SequenceGenerator(name = "agentExchangeFeature", allocationSize = 10)
  @Include
  private Long id;

  @ManyToOne
  @JoinColumn(name = "agentExchangeId")
  @NonNull
  private AgentExchange agentExchange;

  @Column(updatable = false, nullable = false)
  @NonNull
  private String feature;
}

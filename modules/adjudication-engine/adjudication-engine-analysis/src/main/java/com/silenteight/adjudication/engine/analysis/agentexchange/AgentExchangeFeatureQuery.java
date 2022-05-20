package com.silenteight.adjudication.engine.analysis.agentexchange;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.EqualsAndHashCode.Include;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Type;

import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import static lombok.AccessLevel.PACKAGE;

@Data
@NoArgsConstructor(access = PACKAGE)
@Setter(PACKAGE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Immutable
@Entity
class AgentExchangeFeatureQuery {

  @Id
  @Column(name = "agent_exchange_feature_id")
  @Include
  private Long id;

  @Type(type = "pg-uuid")
  private UUID requestId;

  private String feature;

  private Long agentConfigFeatureId;
}

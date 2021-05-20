package com.silenteight.adjudication.engine.analysis.agentexchange.jpa;

import lombok.*;

import com.silenteight.sep.base.common.entity.BaseEntity;

import javax.persistence.*;

import static lombok.AccessLevel.NONE;
import static lombok.AccessLevel.PACKAGE;
import static lombok.AccessLevel.PROTECTED;

@Data
@RequiredArgsConstructor(access = PACKAGE)
@NoArgsConstructor(access = PROTECTED)
@Setter(NONE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Entity
class AgentExchangeMatch extends BaseEntity {

  @EmbeddedId
  @NonNull
  private AgentExchangeMatchKey id;

  @ManyToOne
  @MapsId("agentExchangeId")
  @JoinColumn(name = "agentExchangeId")
  private AgentExchange agentExchange;

  public AgentExchangeMatch(AgentExchange alertExchange, long matchId) {
    this.agentExchange = alertExchange;
    id = new AgentExchangeMatchKey(matchId);
  }
}

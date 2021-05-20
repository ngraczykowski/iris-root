package com.silenteight.adjudication.engine.analysis.agentexchange.jpa;

import lombok.*;

import java.io.Serializable;
import javax.persistence.Column;

import static lombok.AccessLevel.NONE;
import static lombok.AccessLevel.PACKAGE;
import static lombok.AccessLevel.PROTECTED;

@Data
@RequiredArgsConstructor(access = PACKAGE)
@NoArgsConstructor(access = PROTECTED)
@Setter(NONE)
@EqualsAndHashCode
class AgentExchangeMatchKey implements Serializable {

  private static final long serialVersionUID = 5302211907329455680L;

  @Column(updatable = false, nullable = false)
  private Long agentExchangeId;

  @Column(updatable = false, nullable = false)
  @NonNull
  private Long matchId;
}

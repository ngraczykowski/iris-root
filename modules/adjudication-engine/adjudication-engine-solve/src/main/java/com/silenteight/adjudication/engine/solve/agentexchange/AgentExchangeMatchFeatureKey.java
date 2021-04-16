package com.silenteight.adjudication.engine.solve.agentexchange;

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
@EqualsAndHashCode
class AgentExchangeMatchFeatureKey implements Serializable {

  private static final long serialVersionUID = 5302211907329455680L;

  @Column(updatable = false, nullable = false)
  @NonNull
  private Long agentExchangeId;

  @Column(updatable = false, nullable = false)
  @NonNull
  private Long matchId;
}

package com.silenteight.adjudication.engine.solve;

import lombok.*;

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
class AgentExchangeMatchFeaturesKey {

  @Column(updatable = false, nullable = false)
  @NonNull
  private Long agentExchangeId;

  @Column(updatable = false, nullable = false)
  @NonNull
  private Long matchId;
}

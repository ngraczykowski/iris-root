package com.silenteight.adjudication.engine.analysis.agentexchange;

import lombok.*;
import lombok.EqualsAndHashCode.Include;

import com.silenteight.sep.base.common.entity.BaseAggregateRoot;

import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Type;

import java.util.UUID;
import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;
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
class AgentExchange extends BaseAggregateRoot {

  @Id
  @Column(name = "agent_exchange_id", insertable = false, updatable = false, nullable = false)
  @GeneratedValue(strategy = IDENTITY)
  @Include
  private Long id;

  @Column(updatable = false, nullable = false)
  @Type(type = "pg-uuid")
  private UUID requestId;

  @Column(updatable = false, nullable = false)
  @NonNull
  private Integer requestPriority;

  @Column(updatable = false, nullable = false)
  @NonNull
  private String agentConfig;

  @Column(nullable = false)
  @NonNull
  @Enumerated(EnumType.STRING)
  private State state;

  enum State {
    NEW,
    IN_FLIGHT
  }
}

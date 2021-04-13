package com.silenteight.adjudication.engine.solve;

import lombok.*;
import lombok.EqualsAndHashCode.Include;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Type;

import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

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
@javax.persistence.Entity(name = "AgentExchange")
@Builder(access = PACKAGE)
class AgentExchangeEntity {

  @Id
  @Column(name = "agent_exchange_id", updatable = false, nullable = false)
  @Include
  @GenericGenerator(name = "uuid-gen", strategy = "uuid2")
  @GeneratedValue(strategy = IDENTITY, generator = "uuid-gen")
  @Type(type = "pg-uuid")
  private UUID id;

  @Column(updatable = false, nullable = false)
  @NonNull
  private String feature;

  @Column(updatable = false, nullable = false)
  @NonNull
  private String agentConfig;

  @Column(updatable = false, nullable = false)
  @NonNull
  private Integer priority;
}

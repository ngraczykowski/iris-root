package com.silenteight.adjudication.engine.analysis.agentexchange.jpa;

import lombok.*;
import lombok.EqualsAndHashCode.Include;

import com.silenteight.adjudication.engine.analysis.agentexchange.domain.AgentExchangeRequestMessage;
import com.silenteight.sep.base.common.entity.BaseEntity;
import com.silenteight.sep.base.common.entity.IdentifiableEntity;

import org.hibernate.annotations.Type;

import java.util.UUID;
import javax.persistence.*;

import static javax.persistence.GenerationType.SEQUENCE;
import static lombok.AccessLevel.*;

@Data
@AllArgsConstructor(access = PRIVATE)
@NoArgsConstructor(access = PROTECTED)
@Setter(NONE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Entity
@Builder(access = PACKAGE)
class AgentExchange extends BaseEntity implements IdentifiableEntity {

  @Id
  @Column(name = "agent_exchange_id", updatable = false, nullable = false)
  @GeneratedValue(strategy = SEQUENCE, generator = "agentExchange")
  @SequenceGenerator(name = "agentExchange", allocationSize = 10)
  @Include
  @Setter(PUBLIC)
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

  AgentExchange(AgentExchangeRequestMessage message) {
    requestId = message.getRequestId();
    requestPriority = message.getPriority();
    agentConfig = message.getAgentConfig();
  }
}

package com.silenteight.sens.webapp.audit.domain;

import lombok.*;

import com.silenteight.sens.webapp.audit.api.dto.AuditLogDto;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
@Setter(AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString(onlyExplicitlyIncluded = true)
class AuditLog implements Serializable {

  private static final long serialVersionUID = -4924620114138542353L;

  @Id
  @Column(updatable = false, nullable = false)
  @ToString.Include
  @EqualsAndHashCode.Include
  private UUID eventId;

  @ToString.Include
  @Column(nullable = false)
  private UUID correlationId;

  @ToString.Include
  @Column(nullable = false)
  private OffsetDateTime timestamp;

  @ToString.Include
  @Column(nullable = false, length = 64)
  private String type;

  @ToString.Include
  @Column(length = 64)
  private String principal;

  @ToString.Include
  @Column
  private String entityId;

  @ToString.Include
  @Column
  private String entityClass;

  @ToString.Include
  @Column(length = 64)
  private String entityAction;

  @ToString.Include
  @Column
  private String details;

  AuditLogDto toDto() {
    return AuditLogDto.builder()
        .eventId(getEventId())
        .correlationId(getCorrelationId())
        .timestamp(getTimestamp())
        .type(getType())
        .principal(getPrincipal())
        .entityId(getEntityId())
        .entityClass(getEntityClass())
        .entityAction(getEntityAction())
        .details(getDetails())
        .build();
  }
}

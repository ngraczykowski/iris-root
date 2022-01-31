package com.silenteight.sens.webapp.endpoint.domain;

import lombok.*;

import com.silenteight.sep.base.common.entity.BaseModifiableEntity;

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
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@ToString(onlyExplicitlyIncluded = true)
class Endpoint extends BaseModifiableEntity {

  @Id
  @Column(name = "endpoint_id", updatable = false, nullable = false)
  @ToString.Include
  @EqualsAndHashCode.Include
  private UUID endpointId;

  @ToString.Include
  @Column(nullable = false)
  private String name;

  @ToString.Include
  private String description;

  @ToString.Include
  @Column(name = "service_name", nullable = false)
  private String serviceName;
}

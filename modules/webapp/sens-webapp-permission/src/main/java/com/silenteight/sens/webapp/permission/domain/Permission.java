package com.silenteight.sens.webapp.permission.domain;

import lombok.*;

import com.silenteight.sens.webapp.permission.list.dto.PermissionDto;
import com.silenteight.sep.base.common.entity.BaseModifiableEntity;

import java.util.Set;
import java.util.UUID;
import javax.persistence.*;

@Entity
@Data
@Setter(AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@ToString(onlyExplicitlyIncluded = true)
class Permission extends BaseModifiableEntity {

  @Id
  @Column(name = "permission_id", updatable = false, nullable = false)
  @ToString.Include
  @EqualsAndHashCode.Include
  private UUID permissionId;

  @ToString.Include
  @Column(nullable = false)
  private String name;

  @ToString.Include
  private String description;

  @ToString.Include
  @Column(name = "created_by", nullable = false)
  private String createdBy;

  @ToString.Include
  @Column(name = "updated_by", nullable = false)
  private String updatedBy;

  @NonNull
  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(
      name = "webapp_permission_endpoint_id",
      joinColumns = @JoinColumn(name = "permission_id", referencedColumnName = "permission_id"))
  @Column(name = "endpoint_id", nullable = false)
  private Set<UUID> endpointIds;

  PermissionDto toDto() {
    return PermissionDto.builder()
        .id(getPermissionId())
        .name(getName())
        .description(getDescription())
        .build();
  }
}

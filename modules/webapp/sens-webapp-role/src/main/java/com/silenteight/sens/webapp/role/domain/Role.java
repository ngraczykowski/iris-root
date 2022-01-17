package com.silenteight.sens.webapp.role.domain;

import lombok.*;

import com.silenteight.sens.webapp.role.details.dto.RoleDetailsDto;
import com.silenteight.sens.webapp.role.list.dto.RoleDto;
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
class Role extends BaseModifiableEntity {

  @Id
  @Column(name = "role_id", updatable = false, nullable = false)
  @ToString.Include
  @EqualsAndHashCode.Include
  private UUID roleId;

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
      name = "webapp_role_permission_id",
      joinColumns = @JoinColumn(name = "role_id", referencedColumnName = "role_id"))
  @Column(name = "permission_id", nullable = false)
  private Set<UUID> permissionIds;

  RoleDto toDto() {
    return RoleDto.builder()
        .id(getRoleId())
        .name(getName())
        .description(getDescription())
        .build();
  }

  RoleDetailsDto toDetailsDto() {
    return RoleDetailsDto.builder()
        .id(getRoleId())
        .name(getName())
        .description(getDescription())
        .permissions(getPermissionIds())
        .createdAt(getCreatedAt())
        .createdBy(getCreatedBy())
        .updatedAt(getUpdatedAt())
        .updatedBy(getUpdatedBy())
        .build();
  }
}

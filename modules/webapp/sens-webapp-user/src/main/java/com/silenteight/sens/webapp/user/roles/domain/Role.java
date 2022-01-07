package com.silenteight.sens.webapp.user.roles.domain;

import lombok.*;

import com.silenteight.sens.webapp.user.roles.list.dto.RoleDto;
import com.silenteight.sep.base.common.entity.BaseModifiableEntity;
import com.silenteight.sep.base.common.entity.IdentifiableEntity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;
import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@ToString(onlyExplicitlyIncluded = true)
class Role extends BaseModifiableEntity implements IdentifiableEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @EqualsAndHashCode.Include
  @Column(updatable = false)
  private Long id;

  @ToString.Include
  @Column(nullable = false)
  private UUID roleId;

  @ToString.Include
  @Column(nullable = false)
  private String name;

  @ToString.Include
  private String description;

  @ToString.Include
  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private RoleState state;

  @ToString.Include
  @Column(name = "created_by", nullable = false)
  private String createdBy;

  @ManyToMany
  @JoinTable(
      name = "role_permission",
      joinColumns = @JoinColumn(name = "roleId"),
      inverseJoinColumns = @JoinColumn(name = "permissionId"))
  private Collection<Permission> permissions = new ArrayList<>();

  RoleDto toDto() {
    return RoleDto.builder()
        .id(getRoleId())
        .name(getName())
        .description(getDescription())
        .state(getState())
        .updatedAt(getUpdatedAt())
        .createdAt(getCreatedAt())
        .createdBy(getCreatedBy())
        .build();
  }
}

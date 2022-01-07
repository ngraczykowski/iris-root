package com.silenteight.sens.webapp.user.roles.domain;

import lombok.*;

import com.silenteight.sens.webapp.user.roles.list.dto.PermissionDto;
import com.silenteight.sep.base.common.entity.BaseModifiableEntity;
import com.silenteight.sep.base.common.entity.IdentifiableEntity;

import java.util.UUID;
import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@ToString(onlyExplicitlyIncluded = true)
class Permission extends BaseModifiableEntity implements IdentifiableEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @EqualsAndHashCode.Include
  @Column(updatable = false)
  private Long id;

  @ToString.Include
  @Column(nullable = false)
  private UUID permissionId;

  @ToString.Include
  @Column(nullable = false)
  private String name;

  @ToString.Include
  private String description;

  PermissionDto toDto() {
    return PermissionDto.builder()
        .id(getPermissionId())
        .name(getName())
        .description(getDescription())
        .build();
  }
}

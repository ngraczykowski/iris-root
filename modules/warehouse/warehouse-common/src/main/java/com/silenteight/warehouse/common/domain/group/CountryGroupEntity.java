package com.silenteight.warehouse.common.domain.group;

import lombok.*;

import com.silenteight.sep.base.common.entity.BaseModifiableEntity;
import com.silenteight.sep.base.common.entity.IdentifiableEntity;

import java.util.UUID;
import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@Data
@Setter(AccessLevel.PRIVATE)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "warehouse_country_group")
public class CountryGroupEntity extends BaseModifiableEntity implements IdentifiableEntity {

  private static final long serialVersionUID = -4592869339522050083L;

  @Id
  @GeneratedValue(strategy = IDENTITY)
  @Column(name = "id", updatable = false)
  @ToString.Include
  @Setter(AccessLevel.PUBLIC)
  private Long id;

  @ToString.Include
  @Column(name = "country_group_id", nullable = false)
  private UUID countryGroupId;

  @ToString.Include
  @Column(name = "country_group_name", nullable = false)
  private String name;

  @Column(name = "migrated")
  private boolean migrated;

  public void updateName(String name) {
    setName(name);
  }

  public void markMigrated() {
    setMigrated(true);
  }
}

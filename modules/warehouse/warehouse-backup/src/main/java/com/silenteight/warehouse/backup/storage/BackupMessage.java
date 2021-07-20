package com.silenteight.warehouse.backup.storage;

import lombok.*;

import com.silenteight.sep.base.common.entity.BaseEntity;
import com.silenteight.sep.base.common.entity.IdentifiableEntity;

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
@Table(name = "warehouse_message_backup")
class BackupMessage extends BaseEntity implements IdentifiableEntity {

  @Id
  @GeneratedValue(strategy = IDENTITY)
  @Setter(AccessLevel.PUBLIC)
  @Column(name = "id", updatable = false)
  @ToString.Include
  private Long id;

  @ToString.Include
  @Setter(AccessLevel.PUBLIC)
  @Column(length = 40)
  private String requestId;

  @ToString.Include
  @Setter(AccessLevel.PUBLIC)
  @Column(length = 64)
  private String analysisName;

  @NonNull
  @Setter(AccessLevel.PUBLIC)
  byte[] data;
}

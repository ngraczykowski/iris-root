package com.silenteight.warehouse.backup.storage;

import lombok.*;

import com.silenteight.sep.base.common.entity.BaseEntity;
import com.silenteight.sep.base.common.entity.IdentifiableEntity;

import com.vladmihalcea.hibernate.type.json.JsonType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

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
@TypeDef(name = "json", typeClass = JsonType.class)
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

  @Setter(AccessLevel.PUBLIC)
  byte[] data;

  @Setter(AccessLevel.PUBLIC)
  @Type(type = "json")
  String diagnostic;
}

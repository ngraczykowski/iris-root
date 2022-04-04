package com.silenteight.connector.ftcc.request.domain;

import lombok.*;

import com.silenteight.sep.base.common.entity.BaseEntity;

import java.io.Serializable;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Setter(AccessLevel.PRIVATE)
@Builder
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = false)
@ToString(onlyExplicitlyIncluded = true)
@Entity
@Table
class RequestEntity extends BaseEntity implements Serializable {

  @Id
  @Column(name = "batch_id", updatable = false, nullable = false)
  @ToString.Include
  @EqualsAndHashCode.Include
  private UUID batchId;
}

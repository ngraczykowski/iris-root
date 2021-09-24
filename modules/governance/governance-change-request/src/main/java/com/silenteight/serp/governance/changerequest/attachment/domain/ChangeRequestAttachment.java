package com.silenteight.serp.governance.changerequest.attachment.domain;

import lombok.*;

import com.silenteight.sep.base.common.entity.BaseEntity;
import com.silenteight.sep.base.common.entity.IdentifiableEntity;

import java.util.UUID;
import javax.persistence.*;

@Entity
@Data
@Table(name = "governance_change_request_attachment")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@ToString(onlyExplicitlyIncluded = true)
class ChangeRequestAttachment extends BaseEntity implements IdentifiableEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @EqualsAndHashCode.Include
  @Column(updatable = false)
  private Long id;

  @ToString.Include
  @Column(name = "change_request_id", nullable = false)
  private UUID changeRequestId;

  @ToString.Include
  @Column(name = "file_name", nullable = false)
  private String fileName;
}

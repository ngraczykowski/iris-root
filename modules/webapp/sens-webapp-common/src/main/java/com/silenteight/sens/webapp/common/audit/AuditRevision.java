package com.silenteight.sens.webapp.common.audit;

import lombok.*;

import org.hibernate.envers.RevisionEntity;
import org.hibernate.envers.RevisionNumber;
import org.hibernate.envers.RevisionTimestamp;

import javax.persistence.*;

@Entity
@RevisionEntity(AuditRevisionListener.class)
@EqualsAndHashCode(of = { "id" }, callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter(AccessLevel.PROTECTED)
public class AuditRevision {

  @Id
  @RevisionNumber
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "audit_revision_id")
  private Long id;

  @RevisionTimestamp
  private long timestamp;

  @Setter(AccessLevel.PUBLIC)
  private String userName;
}

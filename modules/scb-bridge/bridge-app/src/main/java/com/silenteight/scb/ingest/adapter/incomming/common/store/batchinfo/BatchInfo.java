package com.silenteight.scb.ingest.adapter.incomming.common.store.batchinfo;

import lombok.*;

import com.silenteight.scb.ingest.domain.model.BatchSource;
import com.silenteight.scb.ingest.domain.model.BatchStatus;
import com.silenteight.sep.base.common.entity.BaseEntity;

import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.envers.Audited;

import java.time.OffsetDateTime;
import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "scb_batch_info")
public class BatchInfo extends BaseEntity {

  @Id
  @Column(name = "id", insertable = false, updatable = false, nullable = false)
  @GeneratedValue(strategy = IDENTITY)
  private Long id;

  @NonNull
  private String internalBatchId;

  @NonNull
  @Enumerated(EnumType.STRING)
  private BatchSource batchSource;

  @NonNull
  @Enumerated(EnumType.STRING)
  private BatchStatus batchStatus;

  @NonNull
  private Integer alertCount;

  @UpdateTimestamp
  @Audited
  private OffsetDateTime modifiedAt;
}

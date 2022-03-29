package com.silenteight.scb.ingest.adapter.incomming.common.store.batchinfo;

import lombok.*;

import com.silenteight.scb.ingest.domain.model.BatchSource;
import com.silenteight.sep.base.common.entity.BaseEntity;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@Data
@Setter(AccessLevel.NONE)
@NoArgsConstructor
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

  BatchInfo(
      @NonNull String internalBatchId,
      @NonNull BatchSource batchSource) {
    this.internalBatchId = internalBatchId;
    this.batchSource = batchSource;
  }
}

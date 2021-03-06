/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.store.batchinfo;

import lombok.*;

import com.silenteight.iris.bridge.scb.ingest.domain.model.BatchSource;
import com.silenteight.iris.bridge.scb.ingest.domain.model.BatchStatus;
import com.silenteight.sep.base.common.entity.BaseEntity;

import org.hibernate.annotations.UpdateTimestamp;

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
  private OffsetDateTime modifiedAt;

  // TODO: it would be quite useful to have String error here, like its in AlertUnderProcessing
}

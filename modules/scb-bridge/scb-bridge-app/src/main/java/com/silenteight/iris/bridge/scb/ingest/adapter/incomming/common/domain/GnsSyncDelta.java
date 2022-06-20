/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Setter(AccessLevel.NONE)
@Entity
@Table(name = "ScbGnsSyncDelta")
class GnsSyncDelta {

  @Id
  private String alertExternalId;

  private Integer decisionsCount;

  private String watchlistId;

  private String deltaJobName;
}

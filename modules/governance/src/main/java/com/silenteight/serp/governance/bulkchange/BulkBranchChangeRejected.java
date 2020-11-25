package com.silenteight.serp.governance.bulkchange;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import com.silenteight.sep.base.common.entity.BaseEvent;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Builder
@EqualsAndHashCode(callSuper = true, doNotUseGetters = true)
@ToString(callSuper = true, doNotUseGetters = true)
class BulkBranchChangeRejected extends BaseEvent {

  private static final long serialVersionUID = 3762134920104278607L;

  private final UUID bulkBranchChangeId;

  private final OffsetDateTime rejectedAt;
}

package com.silenteight.serp.governance.bulkchange;

import lombok.RequiredArgsConstructor;

import com.silenteight.proto.serp.v1.governance.BulkBranchChangeCreatedEvent;
import com.silenteight.proto.serp.v1.governance.CreateBulkBranchChangeCommand;

import javax.transaction.Transactional;

import static com.silenteight.protocol.utils.MoreTimestamps.toTimestamp;
import static java.time.OffsetDateTime.now;

@RequiredArgsConstructor
class CreateBulkBranchChangeHandler {

  private final BulkBranchChangeMapper mapper;
  private final BulkBranchChangeRepository repository;

  @Transactional
  BulkBranchChangeCreatedEvent create(CreateBulkBranchChangeCommand command) {
    var change = mapper.map(command);

    repository.save(change);

    return BulkBranchChangeCreatedEvent
        .newBuilder()
        .setId(command.getId())
        .setCorrelationId(command.getCorrelationId())
        .setCreatedAt(toTimestamp(now()))
        .build();
  }
}

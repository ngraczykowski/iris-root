package com.silenteight.serp.governance.bulkchange;

import lombok.RequiredArgsConstructor;

import com.silenteight.proto.serp.v1.governance.ApplyBulkBranchChangeCommand;
import com.silenteight.proto.serp.v1.governance.BulkBranchChangeAppliedEvent;
import com.silenteight.protocol.utils.Uuids;

import org.springframework.context.ApplicationEventPublisher;

import java.util.UUID;
import javax.transaction.Transactional;

import static com.silenteight.protocol.utils.MoreTimestamps.toTimestamp;
import static com.silenteight.protocol.utils.Uuids.fromJavaUuid;
import static java.time.OffsetDateTime.now;

@RequiredArgsConstructor
class ApplyBulkBranchChangeHandler {

  private final BulkBranchChangeRepository repository;
  private final ApplicationEventPublisher eventPublisher;

  @Transactional
  BulkBranchChangeAppliedEvent apply(ApplyBulkBranchChangeCommand command) {
    BulkBranchChange bulkChange = findBulkChange(command);

    bulkChange.apply();
    repository.save(bulkChange);

    eventPublisher.publishEvent(bulkChange.getDomainEvents());

    return BulkBranchChangeAppliedEvent.newBuilder()
        .setId(fromJavaUuid(bulkChange.getBulkBranchChangeId()))
        .setCorrelationId(fromJavaUuid(bulkChange.getCorrelationId()))
        .setAppliedAt(toTimestamp(now()))
        .build();
  }

  private BulkBranchChange findBulkChange(ApplyBulkBranchChangeCommand command) {
    UUID bulkChangeId = Uuids.toJavaUuid(command.getId());
    var change = repository.findByBulkBranchChangeId(bulkChangeId)
        .orElseThrow(() -> new BulkChangeNotFoundException(bulkChangeId));
    // BS
    change.setCorrelationId(Uuids.toJavaUuid(command.getCorrelationId()));
    return change;
  }
}

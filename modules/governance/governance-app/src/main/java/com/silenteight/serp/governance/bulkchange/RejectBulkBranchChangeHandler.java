package com.silenteight.serp.governance.bulkchange;

import lombok.RequiredArgsConstructor;

import com.silenteight.proto.serp.v1.governance.BulkBranchChangeRejectedEvent;
import com.silenteight.proto.serp.v1.governance.RejectBulkBranchChangeCommand;
import com.silenteight.protocol.utils.Uuids;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;

import static com.silenteight.protocol.utils.MoreTimestamps.toTimestamp;
import static java.time.OffsetDateTime.now;

@RequiredArgsConstructor
class RejectBulkBranchChangeHandler {

  private final BulkBranchChangeRepository repository;
  private final ApplicationEventPublisher eventPublisher;

  @Transactional
  BulkBranchChangeRejectedEvent reject(RejectBulkBranchChangeCommand command) {
    BulkBranchChange bulkChange = findBulkChange(command);

    bulkChange.reject();
    repository.save(bulkChange);

    eventPublisher.publishEvent(bulkChange.getDomainEvents());

    return BulkBranchChangeRejectedEvent.newBuilder()
        .setCorrelationId(command.getCorrelationId())
        .setId(command.getId())
        .setRejectedAt(toTimestamp(now()))
        .build();
  }

  private BulkBranchChange findBulkChange(RejectBulkBranchChangeCommand command) {
    var id = Uuids.toJavaUuid(command.getId());
    var change = repository.findByBulkBranchChangeId(id)
        .orElseThrow(() -> new BulkChangeNotFoundException(id));
    // BS
    change.setCorrelationId(Uuids.toJavaUuid(command.getCorrelationId()));
    return change;
  }
}

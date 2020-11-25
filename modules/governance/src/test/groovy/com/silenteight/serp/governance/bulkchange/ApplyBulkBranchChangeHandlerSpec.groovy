package com.silenteight.serp.governance.bulkchange

import com.silenteight.proto.protobuf.Uuid
import com.silenteight.proto.serp.v1.governance.ApplyBulkBranchChangeCommand
import com.silenteight.proto.serp.v1.governance.BulkBranchChangeAppliedEvent
import com.silenteight.protocol.utils.Uuids
import com.silenteight.sep.base.common.entity.BaseEvent
import com.silenteight.serp.governance.bulkchange.BulkBranchChange.State

import org.springframework.context.ApplicationEventPublisher
import spock.lang.Specification

import static com.silenteight.protocol.utils.Uuids.fromJavaUuid
import static com.silenteight.protocol.utils.Uuids.toJavaUuid
import static java.util.UUID.randomUUID

class ApplyBulkBranchChangeHandlerSpec extends Specification {

  BulkBranchChangeRepository repository = Mock(BulkBranchChangeRepository)
  ApplicationEventPublisher eventPublisher = Mock(ApplicationEventPublisher)

  ApplyBulkBranchChangeHandler handler =
      new ApplyBulkBranchChangeHandler(repository, eventPublisher)

  Uuid someUuid = Uuids.random()
  UUID correlationId = randomUUID()
  UUID bulkBranchChangeId = randomUUID()
  BulkBranchChange someBulkBranchChange = new BulkBranchChange(bulkBranchChangeId, [] as Set)

  def 'should apply bulk branch change'() {
    given:
    def command = ApplyBulkBranchChangeCommand.newBuilder()
        .setId(someUuid)
        .setCorrelationId(fromJavaUuid(correlationId))
        .build()

    when:
    BulkBranchChangeAppliedEvent appliedEvent = handler.apply(command)

    then:
    someBulkBranchChange.completedAt
    someBulkBranchChange.state == State.APPLIED

    and:
    appliedEvent.correlationId == fromJavaUuid(correlationId)
    appliedEvent.id == fromJavaUuid(bulkBranchChangeId)
    appliedEvent.appliedAt

    and:
    1 * repository.findByBulkBranchChangeId(toJavaUuid(someUuid)) >>
        Optional.of(someBulkBranchChange)
    1 * repository.save(someBulkBranchChange)
    1 * eventPublisher.publishEvent {
      List<BaseEvent> events ->
        events.first().class.isAssignableFrom BulkBranchChangeApplied
    }
  }

  def 'should throw BulkChangeNotFoundException when bulk change cannot be found'() {
    given:
    def command = ApplyBulkBranchChangeCommand.newBuilder()
        .setId(someUuid)
        .setCorrelationId(fromJavaUuid(correlationId))
        .build()

    when:
    handler.apply(command)

    then:
    1 * repository.findByBulkBranchChangeId(toJavaUuid(someUuid)) >> Optional.empty()
    thrown(BulkChangeNotFoundException)
  }
}

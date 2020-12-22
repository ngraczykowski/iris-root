package com.silenteight.serp.governance.bulkchange

import com.silenteight.proto.serp.v1.governance.RejectBulkBranchChangeCommand
import com.silenteight.sep.base.common.entity.BaseEvent
import com.silenteight.serp.governance.bulkchange.BulkBranchChange.State

import org.springframework.context.ApplicationEventPublisher
import spock.lang.Specification

import static com.silenteight.protocol.utils.Uuids.fromJavaUuid
import static java.util.Optional.empty
import static java.util.Optional.of
import static java.util.UUID.randomUUID

class RejectBulkBranchChangeHandlerSpec extends Specification {

  def eventPublisher = Mock(ApplicationEventPublisher)
  def repository = Mock(BulkBranchChangeRepository)
  def underTest = new RejectBulkBranchChangeHandler(repository, eventPublisher)

  def bulkBranchChangeId = randomUUID()
  def someBulkBranchChange = new BulkBranchChange(bulkBranchChangeId, [] as Set)

  def 'should throw BulkChangeNotFoundException when uuid not found'() {
    given:
    def command = RejectBulkBranchChangeCommand.newBuilder()
        .setId(fromJavaUuid(bulkBranchChangeId))
        .setCorrelationId(fromJavaUuid(randomUUID()))
        .build()

    when:
    underTest.reject(command)

    then:
    1 * repository.findByBulkBranchChangeId(bulkBranchChangeId) >> empty()
    thrown(BulkChangeNotFoundException)
  }

  def 'should reject bulk branch change'() {
    given:
    def id = fromJavaUuid(bulkBranchChangeId)
    def correlationId = fromJavaUuid(randomUUID())
    def command = RejectBulkBranchChangeCommand.newBuilder()
        .setId(id)
        .setCorrelationId(correlationId)
        .build()

    when:
    def result = underTest.reject(command)

    then:
    someBulkBranchChange.completedAt
    someBulkBranchChange.state == State.REJECTED

    result.id == id
    result.correlationId == correlationId
    result.hasRejectedAt()

    1 * repository.findByBulkBranchChangeId(bulkBranchChangeId) >> of(someBulkBranchChange)
    1 * repository.save(someBulkBranchChange)
    1 * eventPublisher.publishEvent({List<BaseEvent> events -> events.size() == 1})
  }
}

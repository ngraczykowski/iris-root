package com.silenteight.serp.governance.bulkchange

import com.silenteight.auditing.bs.AuditDataDto
import com.silenteight.auditing.bs.AuditingLogger
import com.silenteight.proto.serp.v1.governance.BulkBranchChangeCreatedEvent
import com.silenteight.protocol.utils.Uuids
import com.silenteight.serp.governance.bulkchange.audit.AuditBulkChangeCreationHandler

import spock.lang.Specification

import static com.silenteight.protocol.utils.Uuids.toJavaUuid
import static java.util.Objects.nonNull

class AuditBulkChangeCreationHandlerSpec extends Specification {

  def auditingLogger = Mock(AuditingLogger)
  def underTest = new AuditBulkChangeCreationHandler(auditingLogger)

  def 'should handle bulk change creation event'() {
    given:
    def someUuid = Uuids.random()
    def event = BulkBranchChangeCreatedEvent.newBuilder()
        .setId(someUuid)
        .setCorrelationId(someUuid)
        .build()

    when:
    underTest.handle(event)

    then:
    1 * auditingLogger.log({AuditDataDto dto -> nonNull(dto.timestamp) &&
        dto.entityClass == 'com.silenteight.serp.governance.bulkchange.BulkBranchChange' &&
        dto.type == 'CreateBulkBranchChange' &&
        dto.correlationId == toJavaUuid(event.correlationId)})
  }
}

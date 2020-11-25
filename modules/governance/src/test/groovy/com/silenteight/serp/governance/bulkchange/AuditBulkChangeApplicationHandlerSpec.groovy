package com.silenteight.serp.governance.bulkchange

import com.silenteight.auditing.bs.AuditDataDto
import com.silenteight.auditing.bs.AuditingLogger
import com.silenteight.proto.serp.v1.governance.BulkBranchChangeAppliedEvent
import com.silenteight.protocol.utils.Uuids
import com.silenteight.serp.governance.bulkchange.audit.AuditBulkChangeApplicationHandler

import spock.lang.Specification

import static com.silenteight.protocol.utils.Uuids.toJavaUuid
import static java.util.Objects.nonNull

class AuditBulkChangeApplicationHandlerSpec extends Specification {

  def auditingLogger = Mock(AuditingLogger)
  def underTest = new AuditBulkChangeApplicationHandler(auditingLogger)

  def 'should handle bulk change application event'() {
    given:
    def someUuid = Uuids.random()
    def event = BulkBranchChangeAppliedEvent.newBuilder()
        .setId(someUuid)
        .setCorrelationId(someUuid)
        .build()

    when:
    underTest.handle(event)

    then:
    1 * auditingLogger.log(
        {AuditDataDto dto ->
          nonNull(dto.timestamp) &&
              dto.entityClass == 'com.silenteight.serp.governance.bulkchange.BulkBranchChange' &&
              dto.type == 'ApplyBulkBranchChange' &&
              dto.correlationId == toJavaUuid(event.correlationId)
        })
  }
}

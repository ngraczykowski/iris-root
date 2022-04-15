package com.silenteight.fab.dataprep.domain

import com.silenteight.fab.dataprep.domain.model.WarehouseEvent
import com.silenteight.fab.dataprep.domain.model.WarehouseEvent.Alert
import com.silenteight.fab.dataprep.domain.outgoing.LearningEventPublisher

import spock.lang.Specification
import spock.lang.Subject

import static Fixtures.ALERT_NAME
import static Fixtures.LEARNING_DATA
import static com.silenteight.fab.dataprep.domain.Fixtures.ACCESS_PERMISSION_TAG
import static com.silenteight.fab.dataprep.domain.Fixtures.ANALYST_DATE_TIME
import static com.silenteight.fab.dataprep.domain.Fixtures.ANALYST_DECISION
import static com.silenteight.fab.dataprep.domain.Fixtures.ANALYST_REASON
import static com.silenteight.fab.dataprep.domain.Fixtures.DISCRIMINATOR
import static com.silenteight.fab.dataprep.domain.Fixtures.ORIGINAL_ANALYST_DECISION

class LearningServiceTest extends Specification {

  LearningEventPublisher learningEventPublisher = Mock()

  @Subject
  LearningService underTest = new LearningService(learningEventPublisher)

  def 'message to Warehouse should be sent'() {
    when:
    underTest.feedWarehouse(LEARNING_DATA)

    then:
    1 * learningEventPublisher.publish(_) >> {WarehouseEvent event ->
      assert event.getRequestId() != null
      assert event.getAlerts() == [
          Alert.builder()
              .alertName(ALERT_NAME)
              .discriminator(DISCRIMINATOR)
              .accessPermissionTag(ACCESS_PERMISSION_TAG)
              .payload(
                  ['originalAnalystDecision'        : ORIGINAL_ANALYST_DECISION,
                   'analystDecision'                : ANALYST_DECISION,
                   'analystDecisionModifiedDateTime': ANALYST_DATE_TIME,
                   'analystReason'                  : ANALYST_REASON])
              .matches([])
              .build()
      ]
    }
  }
}

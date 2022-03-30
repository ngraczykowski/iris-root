package com.silenteight.fab.dataprep.domain

import com.silenteight.fab.dataprep.domain.model.WarehouseEvent
import com.silenteight.fab.dataprep.domain.model.WarehouseEvent.Alert
import com.silenteight.fab.dataprep.domain.model.WarehouseEvent.Match
import com.silenteight.fab.dataprep.domain.outgoing.LearningEventPublisher

import spock.lang.Specification
import spock.lang.Subject

import static com.silenteight.fab.dataprep.domain.Fixtures.ALERT_NAME
import static com.silenteight.fab.dataprep.domain.Fixtures.MATCH_NAME
import static com.silenteight.fab.dataprep.domain.Fixtures.REGISTERED_ALERT

class LearningServiceTest extends Specification {

  LearningEventPublisher learningEventPublisher = Mock()

  @Subject
  LearningService underTest = new LearningService(learningEventPublisher)

  def 'message to Warehouse should be sent'() {
    when:
    underTest.feedWarehouse(REGISTERED_ALERT)

    then:
    1 * learningEventPublisher.publish(_) >> {WarehouseEvent event ->
      assert event.getRequestId() != null
      assert event.getAlerts() == [Alert.builder()
                                       .alertName(ALERT_NAME)
                                       .matches(
                                           [Match.builder()
                                                .matchName(MATCH_NAME)
                                                .build()])
                                       .build()]
    }
  }
}

package com.silenteight.fab.dataprep.domain

import com.silenteight.fab.dataprep.BaseSpecificationIT
import com.silenteight.fab.dataprep.domain.model.RegisteredAlert

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import spock.lang.Subject
import spock.lang.Unroll

import static com.silenteight.fab.dataprep.domain.Fixtures.*

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@ActiveProfiles("dev")
@Transactional
class AlertServiceTest extends BaseSpecificationIT {

  @Autowired
  @Subject
  AlertService underTest

  @Autowired
  AlertRepository alertRepository

  def 'alert should be stored in DB'() {
    when:
    underTest.save(REGISTERED_ALERT)

    then:
    alertRepository.findAll() == [AlertEntity.builder()
                                      .alertName(ALERT_NAME)
                                      .discriminator("$SYSTEM_ID|$MESSAGE_ID")
                                      .build()]
  }

  @Unroll
  def 'learning alert should be found in DB #alerName #systemId #messageId'() {
    given:
    RegisteredAlert storedAegisteredAlert = RegisteredAlert.builder()
        .batchName(BATCH_NAME)
        .messageName(MESSAGE_NAME)
        .alertName(ALERT_NAME)
        .systemId(SYSTEM_ID)
        .messageId(MESSAGE_ID)
        .build()

    underTest.save(storedAegisteredAlert)

    RegisteredAlert registeredAlert = RegisteredAlert.builder()
        .batchName(BATCH_NAME)
        .messageName(MESSAGE_NAME)
        .alertName(alerName)
        .systemId(systemId)
        .messageId(messageId)
        .build()

    when:
    boolean result = underTest.isLearningAlert(registeredAlert)

    then:
    result == expected

    where:
    expected | alerName   | systemId  | messageId
    true     | ALERT_NAME | SYSTEM_ID | MESSAGE_ID
    true     | ""         | SYSTEM_ID | MESSAGE_ID
    false    | ALERT_NAME | ""        | MESSAGE_ID
    false    | ALERT_NAME | SYSTEM_ID | ""
  }
}

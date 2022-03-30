package com.silenteight.fab.dataprep.domain

import com.silenteight.fab.dataprep.BaseSpecificationIT
import com.silenteight.fab.dataprep.domain.model.LearningData

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestPropertySource
import org.springframework.transaction.annotation.Transactional
import spock.lang.Subject
import spock.lang.Unroll

import static com.silenteight.fab.dataprep.domain.Fixtures.*

@ContextConfiguration(classes = AlertService,
    initializers = ConfigDataApplicationContextInitializer)
@TestPropertySource("classpath:/data-test.properties")
@EnableAutoConfiguration
@Transactional
class AlertServiceTest extends BaseSpecificationIT {

  @Autowired
  @Subject
  AlertService underTest

  @Autowired
  AlertRepository alertRepository

  def 'alert should be stored in DB'() {
    when:
    underTest.save(LEARNING_DATA)

    then:
    alertRepository.findAll() == [AlertEntity.builder()
                                      .alertName(ALERT_NAME)
                                      .discriminator("$SYSTEM_ID|$MESSAGE_ID")
                                      .build()]
  }

  @Unroll
  def 'learning alert should be found in DB #alerName #systemId #messageId'() {
    given:
    underTest.save(LEARNING_DATA)

    LearningData learningData = LearningData.builder()
        .alertName(alerName)
        .systemId(systemId)
        .messageId(messageId)
        .build()

    when:
    boolean result = underTest.isLearningAlert(learningData)

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

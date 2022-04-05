package com.silenteight.fab.dataprep.domain

import com.silenteight.fab.dataprep.BaseSpecificationIT

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestPropertySource
import org.springframework.transaction.annotation.Transactional
import spock.lang.Subject
import spock.lang.Unroll

import static com.silenteight.fab.dataprep.domain.Fixtures.ALERT_NAME
import static com.silenteight.fab.dataprep.domain.Fixtures.DISCRIMINATOR

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
    underTest.save(DISCRIMINATOR, ALERT_NAME)

    then:
    alertRepository.findAll() == [AlertEntity.builder()
                                      .alertName(ALERT_NAME)
                                      .discriminator(DISCRIMINATOR)
                                      .build()]
  }

  @Unroll
  def 'learning alert should be found in DB #alerName #discriminator'() {
    given:
    underTest.save(discriminator, alerName)

    when:
    def result = underTest.getAlertName(discriminator).get()

    then:
    result == alerName

    where:
    alerName   | discriminator
    ALERT_NAME | DISCRIMINATOR
    ""         | DISCRIMINATOR
    ALERT_NAME | ""
  }
}

package com.silenteight.fab.dataprep.domain


import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification
import spock.lang.Subject

import static com.silenteight.fab.dataprep.domain.Fixtures.EMPTY_FEATURE_INPUTS_COMMAND

@ContextConfiguration(classes = ServiceTestConfig,
    initializers = ConfigDataApplicationContextInitializer)
@ActiveProfiles("dev")
class FeedingServiceIT extends Specification {

  @Subject
  @Autowired
  FeedingService feedingService

  def "json fields are optional"() {
    when:
    feedingService.createFeatureInputs(EMPTY_FEATURE_INPUTS_COMMAND)

    then:
    noExceptionThrown()
  }
}

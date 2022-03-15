package com.silenteight.fab.dataprep.domain

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification
import spock.lang.Subject

import static com.silenteight.fab.dataprep.domain.Fixtures.EMPTY_FEATURE_INPUTS_COMMAND

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
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

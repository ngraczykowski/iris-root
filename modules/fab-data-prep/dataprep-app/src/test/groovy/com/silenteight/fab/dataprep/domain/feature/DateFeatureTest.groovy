package com.silenteight.fab.dataprep.domain.feature

import com.silenteight.fab.dataprep.domain.ServiceTestConfig
import com.silenteight.universaldatasource.api.library.date.v1.DateFeatureInputOut
import com.silenteight.universaldatasource.api.library.date.v1.EntityTypeOut
import com.silenteight.universaldatasource.api.library.date.v1.SeverityModeOut

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification
import spock.lang.Subject

import static com.silenteight.fab.dataprep.domain.Fixtures.BUILD_FEATURE_COMMAND
import static com.silenteight.fab.dataprep.domain.Fixtures.EMPTY_BUILD_FEATURE_COMMAND

@ContextConfiguration(classes = ServiceTestConfig,
    initializers = ConfigDataApplicationContextInitializer)
@ActiveProfiles("dev")
class DateFeatureTest extends Specification {

  @Subject
  @Autowired
  DateFeature underTest

  def 'featureInput should be created'() {
    when:
    def result = underTest.buildFeature(command)

    then:
    result == DateFeatureInputOut.builder()
        .feature(DateFeature.FEATURE_NAME)
        .alertedPartyDates(alertedParty)
        .watchlistDates(watchList)
        .alertedPartyType(EntityTypeOut.INDIVIDUAL)
        .mode(SeverityModeOut.STRICT)
        .build()

    where:
    command                     | alertedParty  | watchList
    EMPTY_BUILD_FEATURE_COMMAND | ['30/8/1965'] | []
    BUILD_FEATURE_COMMAND       | ['30/8/1965'] | ['10/04/60\nNATIONALITY: \nnone']
  }
}

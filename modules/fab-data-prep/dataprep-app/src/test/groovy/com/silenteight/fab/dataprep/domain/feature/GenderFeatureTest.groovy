package com.silenteight.fab.dataprep.domain.feature

import com.silenteight.fab.dataprep.domain.ServiceTestConfig
import com.silenteight.universaldatasource.api.library.gender.v1.GenderFeatureInputOut

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
class GenderFeatureTest extends Specification {

  @Subject
  @Autowired
  private GenderFeature underTest

  def 'featureInput should be created'() {
    when:
    def result = underTest.buildFeature(command)

    then:
    result == GenderFeatureInputOut.builder()
        .feature(GenderFeature.FEATURE_NAME)
        .alertedPartyGenders(alertedParty)
        .watchlistGenders(watchList)
        .build()

    where:
    command                     | alertedParty | watchList
    EMPTY_BUILD_FEATURE_COMMAND | ['M']        | []
    BUILD_FEATURE_COMMAND       | ['M']        | ['none']
  }
}

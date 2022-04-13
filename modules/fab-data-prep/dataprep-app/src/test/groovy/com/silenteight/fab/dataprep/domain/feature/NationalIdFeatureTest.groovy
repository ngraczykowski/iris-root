package com.silenteight.fab.dataprep.domain.feature

import com.silenteight.fab.dataprep.domain.ServiceTestConfig
import com.silenteight.universaldatasource.api.library.document.v1.DocumentFeatureInputOut

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
class NationalIdFeatureTest extends Specification {

  @Subject
  @Autowired
  NationalIdFeature underTest

  def 'featureInput should be created'() {
    when:
    def result = underTest.buildFeature(command)

    then:
    result == DocumentFeatureInputOut.builder()
        .feature(NationalIdFeature.FEATURE_NAME)
        .alertedPartyDocuments(alertedParty)
        .watchlistDocuments(watchList)
        .build()

    where:
    command                     | alertedParty   | watchList
    EMPTY_BUILD_FEATURE_COMMAND | ['S93849384A'] | []
    BUILD_FEATURE_COMMAND       | ['S93849384A'] | ['210225511433']
  }
}

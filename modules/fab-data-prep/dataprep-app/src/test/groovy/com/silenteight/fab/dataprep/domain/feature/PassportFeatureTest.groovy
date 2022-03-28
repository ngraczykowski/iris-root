package com.silenteight.fab.dataprep.domain.feature


import com.silenteight.fab.dataprep.domain.ServiceTestConfig
import com.silenteight.universaldatasource.api.library.document.v1.DocumentFeatureInputOut

import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import spock.lang.Specification
import spock.lang.Subject

import static com.silenteight.fab.dataprep.domain.Fixtures.BUILD_FEATURE_COMMAND
import static com.silenteight.fab.dataprep.domain.Fixtures.EMPTY_BUILD_FEATURE_COMMAND

@ContextConfiguration(classes = ServiceTestConfig,
    initializers = ConfigDataApplicationContextInitializer)
@ActiveProfiles("dev")
class PassportFeatureTest extends Specification {

  @Subject
  @Autowired
  PassportFeature underTest

  def 'featureInput should be created'() {
    when:
    def result = underTest.buildFeature(command)

    then:
    result == DocumentFeatureInputOut.builder()
        .feature(PassportFeature.FEATURE_NAME)
        .alertedPartyDocuments(alertedParty)
        .watchlistDocuments(watchList)
        .build()

    where:
    command                     | alertedParty   | watchList
    EMPTY_BUILD_FEATURE_COMMAND | ['AVB2833444'] | []
    BUILD_FEATURE_COMMAND       | ['AVB2833444'] | []
  }
}

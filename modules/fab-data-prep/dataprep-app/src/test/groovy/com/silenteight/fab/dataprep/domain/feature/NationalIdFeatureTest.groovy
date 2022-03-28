package com.silenteight.fab.dataprep.domain.feature

import com.silenteight.fab.dataprep.domain.ServiceTestConfig
import com.silenteight.universaldatasource.api.library.nationalid.v1.NationalIdFeatureInputOut

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
    result == NationalIdFeatureInputOut.builder()
        .feature(NationalIdFeature.FEATURE_NAME)
        .alertedPartyCountries(apCountries)
        .watchlistCountries(wlCountries)
        .alertedPartyDocumentNumbers(apDocumentNumbers)
        .watchlistDocumentNumbers(wlDocumentNumbers)
        .build()

    where:
    command                     | apCountries | wlCountries | apDocumentNumbers | wlDocumentNumbers
    EMPTY_BUILD_FEATURE_COMMAND | ['IR']      | []          | ['S93849384A']    | []
    BUILD_FEATURE_COMMAND       | ['IR']      | ['none']    | ['S93849384A']    | []
  }
}

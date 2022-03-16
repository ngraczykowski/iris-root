package com.silenteight.fab.dataprep.domain.feature

import com.silenteight.universaldatasource.api.library.nationalid.v1.NationalIdFeatureInputOut

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification
import spock.lang.Subject

import static com.silenteight.fab.dataprep.domain.Fixtures.BUILD_FEATURE_COMMAND

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@ActiveProfiles("dev")
class NationalIdFeatureTest extends Specification {

  @Subject
  @Autowired
  NationalIdFeature underTest

  def 'featureInput should be created'() {
    when:
    def result = underTest.buildFeature(BUILD_FEATURE_COMMAND)

    then:
    result == NationalIdFeatureInputOut.builder()
        .feature(NationalIdFeature.FEATURE_NAME)
        .alertedPartyCountries(['IR'])
        .watchlistCountries(['none'])
        .alertedPartyDocumentNumbers(['S93849384A'])
        .watchlistDocumentNumbers([])
        .build()
  }
}

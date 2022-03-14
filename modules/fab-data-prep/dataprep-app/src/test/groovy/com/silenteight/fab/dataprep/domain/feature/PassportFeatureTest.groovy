package com.silenteight.fab.dataprep.domain.feature

import com.silenteight.universaldatasource.api.library.document.v1.DocumentFeatureInputOut

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import spock.lang.Specification
import spock.lang.Subject

import static com.silenteight.fab.dataprep.domain.Fixtures.BUILD_FEATURE_COMMAND

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
class PassportFeatureTest extends Specification {

  @Subject
  @Autowired
  PassportFeature underTest

  def 'featureInput should be created'() {
    when:
    def result = underTest.buildFeature(BUILD_FEATURE_COMMAND)

    then:
    result == DocumentFeatureInputOut.builder()
        .feature(PassportFeature.FEATURE_NAME)
        .alertedPartyDocuments(['AVB2833444'])
        .watchlistDocuments([])
        .build()
  }
}

package com.silenteight.fab.dataprep.domain.feature

import com.silenteight.universaldatasource.api.library.document.v1.DocumentFeatureInputOut

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification
import spock.lang.Subject

import static com.silenteight.fab.dataprep.domain.Fixtures.BUILD_FEATURE_COMMAND

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@ActiveProfiles("dev")
class DocumentNumberFeatureTest extends Specification {

  @Subject
  @Autowired
  DocumentNumberFeature underTest

  def 'featureInput should be created'() {
    when:
    def result = underTest.buildFeature(BUILD_FEATURE_COMMAND)

    then:
    result == DocumentFeatureInputOut.builder()
        .feature(DocumentNumberFeature.FEATURE_NAME)
        .alertedPartyDocuments(['', 'AVB2833444', 'S93849384A'])
        .watchlistDocuments([])
        .build()
  }
}
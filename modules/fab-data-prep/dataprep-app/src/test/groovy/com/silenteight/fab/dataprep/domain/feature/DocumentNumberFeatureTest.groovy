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
class DocumentNumberFeatureTest extends Specification {

  @Subject
  @Autowired
  DocumentNumberFeature underTest

  def 'featureInput should be created'() {
    when:
    def result = underTest.buildFeature(command)

    then:
    result == DocumentFeatureInputOut.builder()
        .feature(DocumentNumberFeature.FEATURE_NAME)
        .alertedPartyDocuments(alertedParty)
        .watchlistDocuments(watchList)
        .build()

    where:
    command                     | alertedParty                     | watchList
    EMPTY_BUILD_FEATURE_COMMAND | ['', 'AVB2833444', 'S93849384A'] | []
    BUILD_FEATURE_COMMAND       | ['', 'AVB2833444', 'S93849384A'] |
        ['E2548653', 'RDBAIQBB', '01TG031000', '210225511433']
  }
}

package com.silenteight.fab.dataprep.domain.feature

import com.silenteight.universaldatasource.api.library.agentinput.v1.AgentInputIn
import com.silenteight.universaldatasource.api.library.document.v1.DocumentFeatureInputOut

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import spock.lang.Specification
import spock.lang.Subject

import static com.silenteight.fab.dataprep.domain.Fixtures.*

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
class DocumentNumberFeatureTest extends Specification {

  @Subject
  @Autowired
  DocumentNumberFeature underTest

  def 'featureInput should be created'() {
    when:
    def result = underTest.createFeatureInput(FEATURE_INPUTS_COMMAND)

    then:
    result == [AgentInputIn.builder()
                   .match(MATCH_NAME)
                   .alert(ALERT_NAME)
                   .featureInputs(
                       [DocumentFeatureInputOut.builder()
                            .feature(DocumentNumberFeature.FEATURE_NAME)
                            .alertedPartyDocuments([''])
                            .watchlistDocuments([])
                            .build()])
                   .build()]
  }
}

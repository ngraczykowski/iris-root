package com.silenteight.fab.dataprep.domain.feature

import com.silenteight.universaldatasource.api.library.agentinput.v1.AgentInputIn
import com.silenteight.universaldatasource.api.library.gender.v1.GenderFeatureInputOut

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import spock.lang.Specification
import spock.lang.Subject

import static com.silenteight.fab.dataprep.domain.Fixtures.*

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
class GenderFeatureTest extends Specification {

  @Subject
  @Autowired
  private GenderFeature underTest

  def 'featureInput should be created'() {
    when:
    def result = underTest.createFeatureInput(command)

    then:
    result == [AgentInputIn.builder()
                   .match(MATCH_NAME)
                   .alert(ALERT_NAME)
                   .featureInputs(
                       [GenderFeatureInputOut.builder()
                            .feature(GenderFeature.FEATURE_NAME)
                            .alertedPartyGenders(alertedParty)
                            .watchlistGenders(watchList)
                            .build()])
                   .build()]
    where:
    command                      | alertedParty | watchList
    EMPTY_FEATURE_INPUTS_COMMAND | ['M']        | []
    FEATURE_INPUTS_COMMAND       | ['M']        | []
  }
}

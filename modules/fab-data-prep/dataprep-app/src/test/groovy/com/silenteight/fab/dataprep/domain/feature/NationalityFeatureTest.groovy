package com.silenteight.fab.dataprep.domain.feature

import com.silenteight.universaldatasource.api.library.agentinput.v1.AgentInputIn
import com.silenteight.universaldatasource.api.library.country.v1.CountryFeatureInputOut

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import spock.lang.Specification
import spock.lang.Subject

import static com.silenteight.fab.dataprep.domain.Fixtures.FEATURE_INPUTS_COMMAND
import static com.silenteight.fab.dataprep.domain.Fixtures.ALERT_NAME
import static com.silenteight.fab.dataprep.domain.Fixtures.MAPPER
import static com.silenteight.fab.dataprep.domain.Fixtures.MATCH_NAME

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
class NationalityFeatureTest extends Specification {

  @Subject
  @Autowired
  NationalityFeature underTest

  def 'featureInput should be created'() {
    when:
    def result = underTest.createFeatureInput(FEATURE_INPUTS_COMMAND)

    then:
    result == [AgentInputIn.builder()
                   .match(MATCH_NAME)
                   .alert(ALERT_NAME)
                   .featureInputs(
                       [CountryFeatureInputOut.builder()
                            .feature(NationalityFeature.FEATURE_NAME)
                            .alertedPartyCountries(['IR'])
                            .watchlistCountries([''])
                            .build()])
                   .build()]
  }

  def 'nationality should be extracted correctly'() {
    given:
    def json = MAPPER.readTree(
        '''{
  "HittedEntity": {
    "Nationality": "IR"
  }
}''')

    when:
    def result = underTest.getWatchlistPart(json)

    then:
    result == ["IR"]
  }
}

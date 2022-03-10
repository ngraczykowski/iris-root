package com.silenteight.fab.dataprep.domain.feature

import com.silenteight.universaldatasource.api.library.agentinput.v1.AgentInputIn
import com.silenteight.universaldatasource.api.library.name.v1.AlertedPartyNameOut
import com.silenteight.universaldatasource.api.library.name.v1.EntityTypeOut
import com.silenteight.universaldatasource.api.library.name.v1.NameFeatureInputOut
import com.silenteight.universaldatasource.api.library.name.v1.NameTypeOut
import com.silenteight.universaldatasource.api.library.name.v1.WatchlistNameOut

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

import static com.silenteight.fab.dataprep.domain.Fixtures.*
import static java.util.stream.Collectors.toList

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
class NameFeatureTest extends Specification {

  @Subject
  @Autowired
  NameFeature underTest

  def 'featureInput should be created'() {
    given:
    def alertedPart =
        ['OSAMA BIN LADIN', 'BIN LADIN'].stream()
            .map {AlertedPartyNameOut.builder().name(it).build()}
            .collect(toList())
    def watchlistPart =
        ["YASIN, ABDUL RAHMAN", "YASIN, ABOUD", "TAHER, ABDUL RAHMAN SAID", "TAHA, ABDUL RAHMAN"]
            .stream()
            .map {WatchlistNameOut.builder().name(it).type(NameTypeOut.REGULAR).build()}
            .collect(toList())

    when:
    def result = underTest.createFeatureInput(FEATURE_INPUTS_COMMAND)

    then:
    result == [AgentInputIn.builder()
                   .match(MATCH_NAME)
                   .alert(ALERT_NAME)
                   .featureInputs(
                       [NameFeatureInputOut.builder()
                            .feature(NameFeature.FEATURE_NAME)
                            .alertedPartyNames(alertedPart)
                            .watchlistNames(watchlistPart)
                            .alertedPartyType(EntityTypeOut.INDIVIDUAL)
                            .matchingTexts(["YASIN RAHMAN"])
                            .build()])
                   .build()]
  }

  @Unroll
  def 'type should be extracted correctly: #expected'() {
    given:
    def json = MAPPER.readTree(
        """{
"HittedEntity": {
  "Type": "$type"
  }
}""")

    when:
    def result = underTest.getPartyType(json)

    then:
    result == expected

    where:
    type | expected
    'I'  | EntityTypeOut.INDIVIDUAL
    'i'  | EntityTypeOut.INDIVIDUAL
    'C'  | EntityTypeOut.ORGANIZATION
    'o'  | EntityTypeOut.ORGANIZATION
    ''   | EntityTypeOut.ENTITY_TYPE_UNSPECIFIED
  }
}

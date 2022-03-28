package com.silenteight.fab.dataprep.domain.feature

import com.silenteight.fab.dataprep.domain.ServiceTestConfig
import com.silenteight.fab.dataprep.domain.model.RegisteredAlert.Match
import com.silenteight.universaldatasource.api.library.bankidentificationcodes.v1.BankIdentificationCodesFeatureInputOut

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification
import spock.lang.Subject

import static com.silenteight.fab.dataprep.domain.Fixtures.*
import static com.silenteight.sep.base.common.support.jackson.JsonConversionHelper.INSTANCE

@ContextConfiguration(classes = ServiceTestConfig,
    initializers = ConfigDataApplicationContextInitializer)
@ActiveProfiles("dev")
class BicFeatureTest extends Specification {

  @Subject
  @Autowired
  BicFeature underTest

  def 'featureInput should be created'() {
    when:
    def result = underTest.buildFeature(command)

    then:
    result == BankIdentificationCodesFeatureInputOut.builder()
        .feature(BicFeature.FEATURE_NAME)
        .alertedPartyMatchingField(alertedParty)
        .watchListMatchingText(wlMatchingTest)
        .watchlistType(wlType)
        .watchlistBicCodes(wlBicCodes)
        .watchlistSearchCodes(wlSearchCodes)
        .build()

    where:
    command                     | alertedParty | wlMatchingTest | wlType | wlBicCodes |
        wlSearchCodes
    EMPTY_BUILD_FEATURE_COMMAND | ''           | ''             | ''     | []         | []
    BUILD_FEATURE_COMMAND       | ''           | 'YASIN RAHMAN' | 'I'    | []         | []
  }

  def 'should be used first hit with bic code'() {
    given:
    def hit1 = INSTANCE.objectMapper().readTree(
        '''{
  "MatchingText": "hit-1",
  "HittedEntity": {
    "Codes": [
    ],
  "Type": "I"
  }
}''')
    def hit2 = INSTANCE.objectMapper().readTree(
        '''{
  "MatchingText": "hit-2",
  "HittedEntity": {
    "Codes": [
      "123456"
    ],
  "Type": "C"
  }
}''')
    def match = Match.builder()
        .hitName(UUID.randomUUID().toString())
        .matchName(MATCH_NAME)
        .payloads([hit1, hit2])
        .build()

    def command = BuildFeatureCommand.builder()
        .parsedMessageData(PARSED_PAYLOAD)
        .match(match)
        .build()

    when:
    def result = underTest.buildFeature(command)

    then:
    result == BankIdentificationCodesFeatureInputOut.builder()
        .feature(BicFeature.FEATURE_NAME)
        .alertedPartyMatchingField('')
        .watchListMatchingText('hit-2')
        .watchlistType('C')
        .watchlistBicCodes(['123456'])
        .watchlistSearchCodes(['123456'])
        .build()
  }
}

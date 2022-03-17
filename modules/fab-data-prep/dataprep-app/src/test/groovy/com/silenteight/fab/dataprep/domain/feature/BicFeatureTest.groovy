package com.silenteight.fab.dataprep.domain.feature

import com.silenteight.universaldatasource.api.library.bankidentificationcodes.v1.BankIdentificationCodesFeatureInputOut

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification
import spock.lang.Subject

import static com.silenteight.fab.dataprep.domain.Fixtures.BUILD_FEATURE_COMMAND
import static com.silenteight.fab.dataprep.domain.Fixtures.EMPTY_BUILD_FEATURE_COMMAND

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
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
    command                     | alertedParty | wlMatchingTest   | wlType | wlBicCodes | wlSearchCodes
    EMPTY_BUILD_FEATURE_COMMAND | ''           | ''               | ''     | []         | []
    BUILD_FEATURE_COMMAND       | ''           | 'YASIN RAHMAN'   | 'I'    | []         | []
  }
}

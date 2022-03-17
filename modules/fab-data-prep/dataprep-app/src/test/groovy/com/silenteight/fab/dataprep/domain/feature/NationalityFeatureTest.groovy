package com.silenteight.fab.dataprep.domain.feature

import com.silenteight.universaldatasource.api.library.country.v1.CountryFeatureInputOut

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification
import spock.lang.Subject

import static com.silenteight.fab.dataprep.domain.Fixtures.*
import static com.silenteight.sep.base.common.support.jackson.JsonConversionHelper.INSTANCE

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@ActiveProfiles("dev")
class NationalityFeatureTest extends Specification {

  @Subject
  @Autowired
  NationalityFeature underTest

  def 'featureInput should be created'() {
    when:
    def result = underTest.buildFeature(command)

    then:
    result == CountryFeatureInputOut.builder()
        .feature(NationalityFeature.FEATURE_NAME)
        .alertedPartyCountries(alertedParty)
        .watchlistCountries(watchList)
        .build()

    where:
    command                     | alertedParty | watchList
    EMPTY_BUILD_FEATURE_COMMAND | ['IR']       | []
    BUILD_FEATURE_COMMAND       | ['IR']       | ['none']
  }

  def 'nationality should be extracted correctly'() {
    given:
    def json = INSTANCE.objectMapper().readTree(
        '''{
  "HittedEntity": {
    "AdditionalInfo": "IR"
  }
}''')

    when:
    def result = underTest.getWatchlistPart(json)

    then:
    result == ["IR"]
  }
}

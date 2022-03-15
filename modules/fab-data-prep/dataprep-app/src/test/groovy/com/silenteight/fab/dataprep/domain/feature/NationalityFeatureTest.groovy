package com.silenteight.fab.dataprep.domain.feature

import com.silenteight.universaldatasource.api.library.country.v1.CountryFeatureInputOut

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification
import spock.lang.Subject

import static com.silenteight.fab.dataprep.domain.Fixtures.BUILD_FEATURE_COMMAND
import static com.silenteight.fab.dataprep.domain.Fixtures.MAPPER

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@ActiveProfiles("dev")
class NationalityFeatureTest extends Specification {

  @Subject
  @Autowired
  NationalityFeature underTest

  def 'featureInput should be created'() {
    when:
    def result = underTest.buildFeature(BUILD_FEATURE_COMMAND)

    then:
    result == CountryFeatureInputOut.builder()
        .feature(NationalityFeature.FEATURE_NAME)
        .alertedPartyCountries(['IR'])
        .watchlistCountries([''])
        .build()
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

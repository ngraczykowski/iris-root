package com.silenteight.fab.dataprep.domain.feature

import com.silenteight.universaldatasource.api.library.country.v1.CountryFeatureInputOut

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import spock.lang.Specification
import spock.lang.Subject

import static com.silenteight.fab.dataprep.domain.Fixtures.FEATURE_INPUTS_COMMAND

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
class CountryFeatureTest extends Specification {

  @Subject
  @Autowired
  CountryFeature underTest

  def 'country should be extracted'() {
    when:
    def result = underTest.createFeatureInput(FEATURE_INPUTS_COMMAND)

    then:
    result.size() == 1
    result.get(0).getFeatureInputs() == [CountryFeatureInputOut.builder()
        .feature('features/country')
        .alertedPartyCountries(['IR'])
        .watchlistCountries(['UEA'])
        .build()]
  }
}

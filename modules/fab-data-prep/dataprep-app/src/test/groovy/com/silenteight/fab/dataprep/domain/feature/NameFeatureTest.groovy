package com.silenteight.fab.dataprep.domain.feature

import com.silenteight.universaldatasource.api.library.name.v1.*

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification
import spock.lang.Subject

import static com.silenteight.fab.dataprep.domain.Fixtures.BUILD_FEATURE_COMMAND
import static java.util.stream.Collectors.toList

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@ActiveProfiles("dev")
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
    def result = underTest.buildFeature(BUILD_FEATURE_COMMAND)

    then:
    result == NameFeatureInputOut.builder()
        .feature(NameFeature.FEATURE_NAME)
        .alertedPartyNames(alertedPart)
        .watchlistNames(watchlistPart)
        .alertedPartyType(EntityTypeOut.INDIVIDUAL)
        .matchingTexts(["YASIN RAHMAN"])
        .build()
  }
}

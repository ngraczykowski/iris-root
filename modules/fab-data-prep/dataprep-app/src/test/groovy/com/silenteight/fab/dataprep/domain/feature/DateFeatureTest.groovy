package com.silenteight.fab.dataprep.domain.feature

import com.silenteight.universaldatasource.api.library.date.v1.DateFeatureInputOut
import com.silenteight.universaldatasource.api.library.date.v1.EntityTypeOut
import com.silenteight.universaldatasource.api.library.date.v1.SeverityModeOut

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification
import spock.lang.Subject

import static com.silenteight.fab.dataprep.domain.Fixtures.BUILD_FEATURE_COMMAND

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@ActiveProfiles("dev")
class DateFeatureTest extends Specification {

  @Subject
  @Autowired
  DateFeature underTest

  def 'featureInput should be created'() {
    when:
    def result = underTest.buildFeature(BUILD_FEATURE_COMMAND)

    then:
    result == DateFeatureInputOut.builder()
        .feature(DateFeature.FEATURE_NAME)
        .alertedPartyDates(['30/8/1965'])
        .watchlistDates(['10/04/60\nNATIONALITY: \nnone'])
        .alertedPartyType(EntityTypeOut.INDIVIDUAL)
        .mode(SeverityModeOut.NORMAL)
        .build()
  }
}

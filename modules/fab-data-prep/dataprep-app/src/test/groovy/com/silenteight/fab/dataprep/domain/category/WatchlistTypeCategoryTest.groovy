package com.silenteight.fab.dataprep.domain.category

import com.silenteight.fab.dataprep.domain.ServiceTestConfig
import com.silenteight.fab.dataprep.domain.model.RegisteredAlert.Match

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

import static com.silenteight.fab.dataprep.domain.Fixtures.BUILD_CATEGORY_COMMAND
import static com.silenteight.fab.dataprep.domain.Fixtures.MATCH_NAME
import static com.silenteight.sep.base.common.support.jackson.JsonConversionHelper.INSTANCE

@ContextConfiguration(classes = ServiceTestConfig,
    initializers = ConfigDataApplicationContextInitializer)
@ActiveProfiles("dev")
class WatchlistTypeCategoryTest extends Specification {

  @Subject
  @Autowired
  WatchlistTypeCategory underTest

  def 'featureInput should be created'() {
    when:
    def result = underTest.buildCategory(BUILD_CATEGORY_COMMAND)

    then:
    result.getName().startsWith('categories/watchlistType/values/')
    result.getMatch() == MATCH_NAME
    result.getSingleValue() == 'INDIVIDUAL'
  }

  @Unroll
  def '#type type should be returned'() {
    given:
    def hit = INSTANCE.objectMapper().readTree(
        """{
  "HittedEntity": {
    "Type": "$wlType"
  }
}""")
    def categoryCommand = BuildCategoryCommand.builder()
        .match(
            Match.builder()
                .matchName(MATCH_NAME)
                .payloads([hit])
                .build())
        .build()
    when:
    def result = underTest.buildCategory(categoryCommand)

    then:
    result.getSingleValue() == type

    where:
    wlType | type
    'I'    | 'INDIVIDUAL'
    'C'    | 'COMPANY'
    'O'    | 'ADDRESS'
    'V'    | 'VESSEL'
    ''     | 'OTHER'
  }
}

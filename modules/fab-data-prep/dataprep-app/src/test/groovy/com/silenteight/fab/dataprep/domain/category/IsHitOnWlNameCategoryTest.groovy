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
import static com.silenteight.fab.dataprep.domain.Fixtures.ALERT_NAME
import static com.silenteight.sep.base.common.support.jackson.JsonConversionHelper.INSTANCE

@ContextConfiguration(classes = ServiceTestConfig,
    initializers = ConfigDataApplicationContextInitializer)
@ActiveProfiles("dev")
class IsHitOnWlNameCategoryTest extends Specification {

  @Subject
  @Autowired
  IsHitOnWlNameCategory underTest

  def 'featureInput should be created'() {
    when:
    def result = underTest.buildCategory(BUILD_CATEGORY_COMMAND)

    then:
    result.getAlert() == ALERT_NAME
    result.getMatch() == MATCH_NAME
    result.getSingleValue() == 'NO_DATA'
  }

  @Unroll
  def '#type type should be returned'() {
    given:
    def hit = INSTANCE.objectMapper().readTree(
        """{
  "HittedEntity": {
    "Names": [
      {
        "Name": "$name"
      }
    ]
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
    name  | type
    '*'   | 'NO'
    ''    | 'NO_DATA'
    'abc' | 'NO_DATA'
  }
}

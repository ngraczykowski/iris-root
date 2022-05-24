package com.silenteight.fab.dataprep.domain.category

import com.silenteight.fab.dataprep.domain.ServiceTestConfig
import com.silenteight.fab.dataprep.domain.model.ParsedMessageData

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

import static com.silenteight.fab.dataprep.domain.Fixtures.BUILD_CATEGORY_COMMAND
import static com.silenteight.fab.dataprep.domain.Fixtures.MATCH_NAME
import static com.silenteight.fab.dataprep.domain.Fixtures.MATCH

@ContextConfiguration(classes = ServiceTestConfig,
    initializers = ConfigDataApplicationContextInitializer)
@ActiveProfiles("dev")
class RecordSourceTypeCategoryTest extends Specification {
  @Subject
  @Autowired
  RecordSourceTypeCategory underTest

  def 'featureInput should be created'() {
    when:
    def result = underTest.buildCategory(BUILD_CATEGORY_COMMAND)

    then:
    result.getName().startsWith('categories/recordSourceType/values/')
    result.getMatch() == MATCH_NAME
    result.getSingleValue() == 'OTHER'
  }

  @Unroll
  def '#type type should be returned'() {
    given:
    def messageData = ParsedMessageData.builder()
        .source(source)

        .build()
    def categoryCommand = BuildCategoryCommand.builder()
        .parsedMessageData(messageData)
        .match(MATCH)
        .build()
    when:
    def result = underTest.buildCategory(categoryCommand)

    then:
    result.getSingleValue() == type

    where:
    source    | type
    'T24'     | 'T24'
    'T241234' | 'T24'
    ''        | 'NO_DATA'
    'abc'     | 'OTHER'
  }
}

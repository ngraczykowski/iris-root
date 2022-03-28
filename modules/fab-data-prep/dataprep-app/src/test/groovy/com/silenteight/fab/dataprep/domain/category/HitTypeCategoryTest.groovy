package com.silenteight.fab.dataprep.domain.category

import com.silenteight.fab.dataprep.domain.ServiceTestConfig

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

import static com.silenteight.fab.dataprep.domain.Fixtures.BUILD_CATEGORY_COMMAND
import static com.silenteight.fab.dataprep.domain.Fixtures.MATCH_NAME

@ContextConfiguration(classes = ServiceTestConfig,
    initializers = ConfigDataApplicationContextInitializer)
@ActiveProfiles("dev")
class HitTypeCategoryTest extends Specification {

  @Subject
  @Autowired
  HitTypeCategory underTest

  def 'featureInput should be created'() {
    when:
    def result = underTest.buildCategory(BUILD_CATEGORY_COMMAND)

    then:
    result.getName().startsWith('categories/hitType/values/')
    result.getMatch() == MATCH_NAME
    result.getSingleValue() == 'OTHER'
  }

  @Unroll
  def 'sanction should be extracted correctly #systemId'() {
    when:
    def result = underTest.buildCategory(
        BuildCategoryCommand.builder()
            .matchName(MATCH_NAME)
            .systemId(systemId)
            .build())

    then:
    result.getSingleValue() == expected

    where:
    expected  | systemId
    'SAN'     | 'SAN!'
    'SAN'     | '123SAN!123'
    'OTHER'   | 'SAN'
    'OTHER'   | 'SAN123!'
    'NO_DATA' | ''
  }
}
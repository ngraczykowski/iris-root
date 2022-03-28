package com.silenteight.fab.dataprep.domain.category

import com.silenteight.fab.dataprep.domain.ServiceTestConfig

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification
import spock.lang.Subject

import static com.silenteight.fab.dataprep.domain.Fixtures.BUILD_CATEGORY_COMMAND
import static com.silenteight.fab.dataprep.domain.Fixtures.MATCH_NAME

@ContextConfiguration(classes = ServiceTestConfig,
    initializers = ConfigDataApplicationContextInitializer)
@ActiveProfiles("dev")
class CustomerTypeCategoryTest extends Specification {

  @Subject
  @Autowired
  CustomerTypeCategory underTest

  def 'featureInput should be created'() {
    when:
    def result = underTest.buildCategory(BUILD_CATEGORY_COMMAND)

    then:
    result.getName().startsWith('categories/customerType/values/')
    result.getMatch() == MATCH_NAME
    result.getSingleValue() == 'I'
  }
}

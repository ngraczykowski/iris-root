package com.silenteight.fab.dataprep.domain.category


import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

import static com.silenteight.fab.dataprep.domain.Fixtures.BUILD_CATEGORY_COMMAND
import static com.silenteight.fab.dataprep.domain.Fixtures.MATCH_NAME

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@ActiveProfiles("dev")
class SanctionCategoryTest extends Specification {

  @Subject
  @Autowired
  SanctionCategory underTest

  def 'featureInput should be created'() {
    when:
    def result = underTest.buildCategory(BUILD_CATEGORY_COMMAND)

    then:
    result.getName().contains('categories/is_san/values/')
    result.getMatch() == MATCH_NAME
    result.getSingleValue() == 'False'
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
    expected | systemId
    'True'   | 'SAN!'
    'True'   | '123SAN!123'
    'False'  | 'SAN'
    'False'  | 'SAN123!'
  }
}

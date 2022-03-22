package com.silenteight.fab.dataprep.domain

import com.silenteight.fab.dataprep.domain.category.FabCategory
import com.silenteight.fab.dataprep.domain.feature.FeatureInputsCommand
import com.silenteight.fab.dataprep.domain.model.RegisteredAlert
import com.silenteight.fab.dataprep.domain.model.RegisteredAlert.Match
import com.silenteight.fab.dataprep.infrastructure.grpc.CategoriesConfigurationProperties.CategoryDefinition
import com.silenteight.universaldatasource.api.library.category.v2.*

import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@ActiveProfiles("dev")
class CategoryServiceTest extends Specification {

  @Autowired
  CategoryService underTest

  @SpringBean
  CategoryServiceClient categoryServiceClient = Mock()

  @SpringBean
  CategoryValuesServiceClient categoryValuesServiceClient = Mock()

  def "categories should be created"() {
    given:
    FabCategory isSanFabCategory = Mock() {
      getCategoryName() >> "categories/is_san"
      getCategoryDefinition() >> CategoryDefinition.builder()
          .enabled(true)
          .displayName("isSanction")
          .categoryType("ANY_STRING")
          .allowedValues([])
          .multiValue(true)
          .build()
    }

    List<FabCategory> categories = [isSanFabCategory]

    when:
    underTest.createCategories(categories)

    then:
    1 * categoryServiceClient.createCategories(
        BatchCreateCategoriesIn.builder()
            .categories(
                [CategoryShared.builder()
                     .name("categories/is_san")
                     .displayName("isSanction")
                     .categoryType(CategoryTypeShared.ANY_STRING)
                     .allowedValues([])
                     .multiValue(true)
                     .build()])
            .build())
  }

  def "should call all categories"() {
    given:
    def categoryDefinition = CategoryDefinition.builder()
        .categoryType("ANY_STRING")
        .build()
    def categories = [
        Mock(FabCategory) {
          getCategoryDefinition() >> categoryDefinition
          getCategoryName() >> "categories/category-1"
        },
        Mock(FabCategory) {
          getCategoryDefinition() >> categoryDefinition
          getCategoryName() >> "categories/category-2"
        }
    ]

    def categoryService = new CategoryService(
        categoryServiceClient,
        categoryValuesServiceClient,
        categories
    )

    def command = FeatureInputsCommand.builder()
        .registeredAlert(
            RegisteredAlert.builder()
                .batchName(Fixtures.BATCH_NAME)
                .messageName(Fixtures.MESSAGE_NAME)
                .alertName(Fixtures.ALERT_NAME)
                .matches(
                    [Match.builder()
                         .hitName(Fixtures.HIT_NAME)
                         .matchName(Fixtures.MATCH_NAME)
                         .build()
                    ])
                .build())
        .build()

    when:
    categoryService.createCategoryInputs(command)

    then:
    categories.each {1 * it.buildCategory(_)}
    1 * categoryValuesServiceClient.createCategoriesValues(_) >> []
  }
}

package com.silenteight.fab.dataprep.domain

import com.silenteight.fab.dataprep.BaseSpecificationIT
import com.silenteight.fab.dataprep.domain.category.FabCategory
import com.silenteight.fab.dataprep.domain.feature.FeatureInputsCommand
import com.silenteight.fab.dataprep.infrastructure.grpc.CategoriesConfigurationProperties.CategoryDefinition
import com.silenteight.universaldatasource.api.library.category.v2.*

import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration

import static com.silenteight.fab.dataprep.domain.Fixtures.REGISTERED_ALERT

@ContextConfiguration(classes = ServiceTestConfig,
    initializers = ConfigDataApplicationContextInitializer)
@ActiveProfiles("dev")
class CategoryServiceTest extends BaseSpecificationIT {

  @Autowired
  CategoryService underTest

  @SpringBean
  CategoryServiceClient categoryServiceClient = Mock()

  @SpringBean
  CategoryValuesServiceClient categoryValuesServiceClient = Mock()

  def "categories should be created"() {
    when:
    underTest.createCategories()

    then:
    1 * categoryServiceClient.createCategories(
        BatchCreateCategoriesIn.builder()
            .categories(
                [CategoryShared.builder()
                     .name("categories/hitType")
                     .displayName("Risk Type")
                     .categoryType(CategoryTypeShared.ENUMERATED)
                     .allowedValues(['SAN', 'OTHER', 'NO_DATA'])
                     .multiValue(false)
                     .build(),
                 CategoryShared.builder()
                     .name("categories/customerType")
                     .displayName("Customer Type")
                     .categoryType(CategoryTypeShared.ENUMERATED)
                     .allowedValues(['I', 'C', 'NO_DATA', 'DATA_SOURCE_ERROR'])
                     .multiValue(false)
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
        .registeredAlert(REGISTERED_ALERT)
        .build()

    when:
    categoryService.createCategoryInputs(command)

    then:
    categories.each {1 * it.buildCategory(_)}
    1 * categoryValuesServiceClient.createCategoriesValues(_) >> []
  }
}

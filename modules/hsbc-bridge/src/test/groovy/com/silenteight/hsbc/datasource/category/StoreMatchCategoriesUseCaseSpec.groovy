package com.silenteight.hsbc.datasource.category

import com.silenteight.hsbc.bridge.match.MatchComposite
import com.silenteight.hsbc.datasource.category.command.StoreMatchCategoriesCommand
import com.silenteight.hsbc.datasource.datamodel.CaseInformation
import com.silenteight.hsbc.datasource.datamodel.MatchData

import spock.lang.Specification

class StoreMatchCategoriesUseCaseSpec extends Specification {

  def fixtures = new Fixtures()
  def categoryRepository = Mock(CategoryRepository)
  def matchCategoryRepository = Mock(MatchCategoryRepository)
  def categoryModelHolder = Mock(CategoryModelHolder)
  def underTest = new StoreMatchCategoriesUseCase(
      categoryRepository, matchCategoryRepository, categoryModelHolder)

  def 'should store match categories'() {
    given:
    def command = new StoreMatchCategoriesCommand([fixtures.matchComposite])
    categoryModelHolder.getCategories() >> fixtures.categories

    when:
    underTest.storeMatchCategories(command)

    then:
    fixtures.numberOfCategories * categoryRepository.findByName(_ as String) >> new CategoryEntity()
    (fixtures.numberOfCategories * command.getMatchComposites().size()) *
        matchCategoryRepository.save(_ as MatchCategoryEntity)
  }

  class Fixtures {

    def categories = [
        CategoryModel.builder()
            .name('categories/sourceSystem')
            .displayName('Source System')
            .multiValue(false)
            .type(CategoryType.ANY_STRING)
            .allowedValues([])
            .valueRetriever(
                new CategoryValueRetriever() {

                  @Override
                  List<String> retrieve(MatchData matchData) {
                    return List.of("dummy1", "dummy2")
                  }
                })
            .build()
    ] as List

    def numberOfCategories = categories.size()

    def matchComposite = MatchComposite.builder()
        .id(1L)
        .name('matchName')
        .matchData(matchData)
        .build()
  }

  def matchData = [
      getCaseInformation: {
        [
            getExtendedAttribute5: {'AM'},
            getSourceName        : {'sourceName'}
        ] as CaseInformation
      },
      isIndividual      : {true}
  ] as MatchData
}

package com.silenteight.hsbc.datasource.category

import com.silenteight.hsbc.bridge.match.MatchComposite
import com.silenteight.hsbc.datasource.category.command.StoreMatchCategoriesCommand
import com.silenteight.hsbc.datasource.datamodel.CaseInformation
import com.silenteight.hsbc.datasource.datamodel.MatchData

import spock.lang.Specification

class StoreMatchCategoriesUseCaseSpec extends Specification {

  def categoryRepository = Mock(CategoryRepository)
  def matchCategoryRepository = Mock(MatchCategoryRepository)
  def underTest = new StoreMatchCategoriesUseCase(categoryRepository, matchCategoryRepository)

  def numberOfCategories = CategoryModelHolder.getCategories().size()

  def 'should store match categories'() {
    given:
    def matchData = [
        getCaseInformation: {
          [
              getExtendedAttribute5: {'AM'},
              getSourceName        : {'sourceName'}
          ] as CaseInformation
        },
        isIndividual      : {true}
    ] as MatchData
    def matchComposite = MatchComposite.builder()
        .id(1L)
        .name('matchName')
        .matchData(matchData)
        .build()
    def command = new StoreMatchCategoriesCommand([matchComposite])

    when:
    underTest.storeMatchCategories(command)

    then:
    numberOfCategories * categoryRepository.findByName(_ as String) >> new CategoryEntity()
    (numberOfCategories * command.getMatchComposites().size()) *
        matchCategoryRepository.save(_ as MatchCategoryEntity)
  }
}

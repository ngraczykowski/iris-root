package com.silenteight.hsbc.datasource.category

import com.silenteight.hsbc.bridge.domain.CasesWithAlertURL
import com.silenteight.hsbc.bridge.match.MatchComposite
import com.silenteight.hsbc.bridge.match.MatchRawData
import com.silenteight.hsbc.datasource.category.command.StoreMatchCategoriesCommand

import spock.lang.Specification

class StoreMatchCategoriesUseCaseSpec extends Specification {

  def categoryRepository = Mock(CategoryRepository)
  def matchCategoryRepository = Mock(MatchCategoryRepository)
  def underTest = new StoreMatchCategoriesUseCase(categoryRepository, matchCategoryRepository)

  def numberOfCategories = CategoryModelHolder.getCategories().size()

  def 'should store match categories'() {
    given:
    def matchRawData = new MatchRawData(
        caseWithAlertURL: new CasesWithAlertURL(
            extendedAttribute3: 'AM',
            sourceName: 'sourceName'
        )
    )
    def matchComposite = MatchComposite.builder()
        .name('matchName')
        .rawData(matchRawData)
        .build()
    def command = StoreMatchCategoriesCommand.builder()
        .matchComposites([matchComposite])
        .build()

    when:
    underTest.storeMatchCategories(command)

    then:
    numberOfCategories * categoryRepository.findByName(_ as String) >> new CategoryEntity()
    (numberOfCategories * command.getMatchComposites().size()) *
        matchCategoryRepository.save(_ as MatchCategoryEntity)
  }
}

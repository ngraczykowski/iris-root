package com.silenteight.hsbc.datasource.category;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.datasource.category.command.StoreMatchCategoriesCommand;

import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Slf4j
class StoreMatchCategoriesUseCase {

  private final CategoryRepository categoryRepository;
  private final MatchCategoryRepository matchCategoryRepository;
  private final CategoryModelHolder categoryModelHolder;

  @Transactional
  void storeMatchCategories(@NonNull StoreMatchCategoriesCommand command) {
    var matchComposites = command.getMatchComposites();

    categoryModelHolder.getCategories()
        .forEach(categoryModel -> matchComposites.forEach(match -> {
          var matchId = match.getId();
          var category = categoryRepository.findByName(categoryModel.getName());
          var values = categoryModel.getValueRetriever().retrieve(match.getMatchData());

          var matchCategory = new MatchCategoryEntity(matchId, category, values);
          matchCategoryRepository.save(matchCategory);
        }));

    log.trace("Match categories has been stored");
  }
}

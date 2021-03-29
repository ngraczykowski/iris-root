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

  @Transactional
  void storeMatchCategories(@NonNull StoreMatchCategoriesCommand command) {
    var matchComposites = command.getMatchComposites();

    CategoryModelHolder.getCategories()
        .forEach(categoryModel -> matchComposites.stream().forEach(match -> {
          var matchValue = match.getName();
          var category = categoryRepository.findByName(categoryModel.getName());
          var values = categoryModel.getValueRetriever().retrieve(match.getRawData());

          var matchCategory = new MatchCategoryEntity(matchValue, category, values);
          matchCategoryRepository.save(matchCategory);
        }));

    if (log.isDebugEnabled()) {
      log.info("Match categories has been stored");
    }
  }
}

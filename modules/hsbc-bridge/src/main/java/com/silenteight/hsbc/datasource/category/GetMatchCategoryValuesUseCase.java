package com.silenteight.hsbc.datasource.category;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.match.MatchFacade;
import com.silenteight.hsbc.datasource.category.command.GetMatchCategoryValuesCommand;
import com.silenteight.hsbc.datasource.category.dto.CategoryValueDto;

import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class GetMatchCategoryValuesUseCase {

  private final MatchFacade matchFacade;
  private final MatchCategoryRepository matchCategoryRepository;

  @Transactional(readOnly = true)
  public List<CategoryValueDto> activate(@NonNull GetMatchCategoryValuesCommand command) {

    var matchIds = findMatchIds(command.getMatchValues());
    var categoryValues =
        matchCategoryRepository.findByMatchIdIn(matchIds);

    return categoryValues.stream()
        .map(this::mapMatchCategory)
        .collect(Collectors.toList());
  }

  private Collection<Long> findMatchIds(List<String> matchValues) {
    return matchFacade.getMatchIdsByNames(matchValues);
  }

  private CategoryValueDto mapMatchCategory(MatchCategoryEntity matchCategory) {
    return CategoryValueDto.builder()
        .multiValue(matchCategory.getCategory().isMultiValue())
        .name(matchCategory.getCategoryName())
        .values(new ArrayList<>(matchCategory.getValues()))
        .build();
  }
}

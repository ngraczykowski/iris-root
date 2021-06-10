package com.silenteight.hsbc.datasource.category;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.category.command.GetMatchCategoryValuesCommand;
import com.silenteight.hsbc.datasource.category.dto.CategoryValueDto;

import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class GetMatchCategoryValuesUseCase {

  private final MatchCategoryViewRepository matchCategoryViewRepository;

  @Transactional(readOnly = true)
  public List<CategoryValueDto> activate(@NonNull GetMatchCategoryValuesCommand command) {
    var matchCategoryValues = findMatchCategoryValues(command.getMatchValues());

    return matchCategoryValues.stream()
        .map(this::mapMatchCategoryValue)
        .collect(Collectors.toList());
  }

  private List<MatchCategoryView> findMatchCategoryValues(List<String> matchValues) {
    return matchCategoryViewRepository.findByNameIn(matchValues);
  }

  private CategoryValueDto mapMatchCategoryValue(MatchCategoryView matchCategory) {
    return CategoryValueDto.builder()
        .multiValue(matchCategory.isMultiValue())
        .name(matchCategory.getName())
        .values(new ArrayList<>(matchCategory.getValues()))
        .build();
  }
}

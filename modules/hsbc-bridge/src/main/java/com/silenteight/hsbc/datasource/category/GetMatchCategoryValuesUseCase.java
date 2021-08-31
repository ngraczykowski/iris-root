package com.silenteight.hsbc.datasource.category;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.datasource.category.command.GetMatchCategoryValuesCommand;
import com.silenteight.hsbc.datasource.category.dto.CategoryValueDto;

import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@Slf4j
public class GetMatchCategoryValuesUseCase {

  private final MatchCategoryViewRepository matchCategoryViewRepository;

  @Transactional(readOnly = true)
  public List<CategoryValueDto> activate(@NonNull GetMatchCategoryValuesCommand command) {
    var matchValues = command.getMatchValues();
    var matchCategoryValues = findMatchCategoryValues(matchValues)
        .stream()
        .map(this::mapMatchCategoryValue)
        .collect(toList());

    var result = new ArrayList<>(matchCategoryValues);
    if (result.size() < matchValues.size()) {
      addMissingValues(matchValues, result);
    }

    return result;
  }

  private void addMissingValues(List<String> matchValues, List<CategoryValueDto> result) {
    var returnedNames = result.stream().map(CategoryValueDto::getName).collect(toList());
    var missingNames =
        matchValues.stream().filter(a -> !returnedNames.contains(a)).collect(toList());
    log.warn("Not all match categories are present, missing category values={}", missingNames);

    result.addAll(addMissingValues(missingNames));
  }

  private List<CategoryValueDto> addMissingValues(List<String> names) {
    return names.stream().map(n ->
            CategoryValueDto.builder()
                .name(n)
                .values(List.of("NO_DATA"))
                .build())
        .collect(toList());
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

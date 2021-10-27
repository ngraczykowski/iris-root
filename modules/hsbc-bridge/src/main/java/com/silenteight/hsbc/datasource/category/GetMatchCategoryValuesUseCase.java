package com.silenteight.hsbc.datasource.category;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.datasource.category.command.GetMatchCategoryValuesCommand;
import com.silenteight.hsbc.datasource.category.dto.CategoryValueDto;

import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static java.util.List.of;
import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@Slf4j
class GetMatchCategoryValuesUseCase {

  private final MatchCategoryRepository matchCategoryRepository;

  @Transactional(readOnly = true)
  public List<CategoryValueDto> activate(@NonNull GetMatchCategoryValuesCommand command) {
    var matchValues = mapToMatchValues(command);
    var matchCategoryValues = findMatchCategoryValues(matchValues)
        .map(this::mapMatchCategoryEntity)
        .collect(toList());

    if (matchCategoryValues.size() < matchValues.size()) {
      return addMissingValues(matchValues, matchCategoryValues);
    }

    return matchCategoryValues;
  }

  private List<String> mapToMatchValues(GetMatchCategoryValuesCommand command) {
    return command.getCategoryMatches().stream()
        .flatMap(categoryMatch -> categoryMatch.getMatches().stream()
            .map(match -> categoryMatch.getCategory() + '/' + match)
        )
        .collect(toList());
  }

  private Stream<MatchCategoryEntity> findMatchCategoryValues(List<String> matchValues) {
    return matchCategoryRepository.findByNameIn(matchValues);
  }

  private CategoryValueDto mapMatchCategoryEntity(MatchCategoryEntity matchCategoryEntity) {
    var names = separateCategoryAndMatchName(matchCategoryEntity.getName());
    return CategoryValueDto.builder()
        .multiValue(matchCategoryEntity.getMultiValue())
        .name(names.get(0))
        .match(names.get(1))
        .values(new ArrayList<>(matchCategoryEntity.getValues()))
        .build();
  }

  private static List<String> separateCategoryAndMatchName(String fullName) {
    var index = fullName.indexOf('/', fullName.indexOf('/') + 1);
    var name = fullName.substring(0, index);
    var match = fullName.substring(index + 1);
    return of(name, match);
  }

  private List<CategoryValueDto> addMissingValues(
      List<String> matchValues, List<CategoryValueDto> matchCategoryValues) {
    var result = new ArrayList<>(matchCategoryValues);
    var returnedNames = result.stream().map(CategoryValueDto::getName).collect(toList());
    var missingNames =
        matchValues.stream().filter(a -> !returnedNames.contains(a)).collect(toList());
    log.warn("Not all match categories are present, missing category values={}", missingNames);

    result.addAll(addMissingValues(missingNames));
    return result;
  }

  private List<CategoryValueDto> addMissingValues(List<String> names) {
    return names.stream().map(n ->
            CategoryValueDto.builder()
                .name(n)
                .values(of("NO_DATA"))
                .build())
        .collect(toList());
  }
}

package com.silenteight.hsbc.datasource.category.command;

import lombok.Builder;
import lombok.Value;

import com.silenteight.hsbc.datasource.category.dto.CategoryMatchesDto;

import java.util.List;

@Builder
@Value
public class GetMatchCategoryValuesCommand {

  List<CategoryMatchesDto> categoryMatches;
}

package com.silenteight.hsbc.datasource.category.command;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Builder
@Value
public class GetMatchCategoryValuesCommand {

  List<String> matchValues;
}

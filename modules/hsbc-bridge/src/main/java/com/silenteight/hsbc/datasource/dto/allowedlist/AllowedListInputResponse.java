package com.silenteight.hsbc.datasource.dto.allowedlist;

import lombok.Builder;
import lombok.Value;

import java.util.List;

import static java.util.Collections.emptyList;

@Value
@Builder
public class AllowedListInputResponse {

  @Builder.Default
  List<AllowListInputDto> inputs = emptyList();
}

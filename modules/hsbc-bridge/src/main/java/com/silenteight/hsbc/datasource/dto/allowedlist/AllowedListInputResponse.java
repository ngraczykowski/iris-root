package com.silenteight.hsbc.datasource.dto.allowedlist;

import lombok.Builder;
import lombok.Value;

import java.util.Collections;
import java.util.List;

@Value
@Builder
public class AllowedListInputResponse {

  @Builder.Default
  List<AllowListInputDto> inputs = Collections.emptyList();
}

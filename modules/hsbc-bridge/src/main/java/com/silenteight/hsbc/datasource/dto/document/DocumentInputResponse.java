package com.silenteight.hsbc.datasource.dto.document;

import lombok.Builder;
import lombok.Value;

import java.util.List;

import static java.util.Collections.emptyList;

@Builder
@Value
public class DocumentInputResponse {

  @Builder.Default
  List<DocumentInputDto> inputs = emptyList();
}

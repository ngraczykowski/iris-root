package com.silenteight.hsbc.datasource.dto.document;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Builder
@Value
public class DocumentInputResponse {
  List<DocumentInputDto> inputs;
}

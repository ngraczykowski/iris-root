package com.silenteight.hsbc.datasource.dto.ispep;


import lombok.Builder;
import lombok.Value;

import java.util.Collections;
import java.util.List;

@Builder
@Value
public class WatchListItemDto {

  String id;
  String type;
  @Builder.Default
  List<String> countries = Collections.emptyList();
  String furtherInformation;
  @Builder.Default
  List<String> linkedPepsUids = Collections.emptyList();
}

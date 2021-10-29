package com.silenteight.hsbc.datasource.dto.ispep;


import lombok.Builder;
import lombok.Value;

import java.util.List;

import static java.util.Collections.emptyList;

@Builder
@Value
public class WatchListItemDto {

  String id;
  String type;
  String furtherInformation;
  String country;
  @Builder.Default
  List<String> linkedPepsUids = emptyList();
}

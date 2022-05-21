package com.silenteight.hsbc.datasource.dto.name;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class WatchlistNameDto {

  String name;
  NameType type;

  public enum NameType {
    REGULAR,
    ALIAS
  }
}

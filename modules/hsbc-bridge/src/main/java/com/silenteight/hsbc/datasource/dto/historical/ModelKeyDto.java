package com.silenteight.hsbc.datasource.dto.historical;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ModelKeyDto {

  ModelKeyType modelKeyType;
  ModelKeyValue modelKeyValue;

  public enum ModelKeyType {
    ALERTED_PARTY,
    WATCHLIST_PARTY,
    MATCH
  }

  public interface ModelKeyValue {

  }
}

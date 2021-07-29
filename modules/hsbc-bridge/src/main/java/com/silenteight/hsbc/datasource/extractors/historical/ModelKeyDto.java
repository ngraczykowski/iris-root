package com.silenteight.hsbc.datasource.extractors.historical;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
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

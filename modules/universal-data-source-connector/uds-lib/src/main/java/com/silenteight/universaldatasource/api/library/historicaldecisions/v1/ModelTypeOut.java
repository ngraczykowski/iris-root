package com.silenteight.universaldatasource.api.library.historicaldecisions.v1;

import com.silenteight.datasource.api.historicaldecisions.v1.HistoricalDecisionsInput.ModelType;

public enum ModelTypeOut {
  UNSPECIFIED,
  ALERTED_PARTY,
  WATCHLIST_PARTY,
  MATCH;

  static ModelTypeOut createFrom(ModelType type) {
    switch (type) {
      case MATCH:
        return ModelTypeOut.MATCH;
      case ALERTED_PARTY:
        return ModelTypeOut.ALERTED_PARTY;
      case WATCHLIST_PARTY:
        return ModelTypeOut.WATCHLIST_PARTY;
      default:
        return ModelTypeOut.UNSPECIFIED;
    }
  }
}

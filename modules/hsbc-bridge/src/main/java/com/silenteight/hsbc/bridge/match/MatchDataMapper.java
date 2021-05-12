package com.silenteight.hsbc.bridge.match;

import com.silenteight.hsbc.bridge.json.external.model.HsbcMatch;

interface MatchDataMapper {

  MatchRawData map(HsbcMatch hsbcMatch);
}

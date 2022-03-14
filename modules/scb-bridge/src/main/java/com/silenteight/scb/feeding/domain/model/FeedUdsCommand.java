package com.silenteight.scb.feeding.domain.model;

import com.silenteight.scb.ingest.adapter.incomming.common.model.alert.Alert;
import com.silenteight.scb.ingest.adapter.incomming.common.model.match.Match;

import java.util.List;

public record FeedUdsCommand(
    Alert alert,
    List<Match> matches) {
}

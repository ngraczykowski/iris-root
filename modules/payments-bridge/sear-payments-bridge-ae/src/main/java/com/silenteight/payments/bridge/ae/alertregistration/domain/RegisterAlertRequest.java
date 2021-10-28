package com.silenteight.payments.bridge.ae.alertregistration.domain;

import lombok.Builder;
import lombok.Builder.Default;
import lombok.NonNull;
import lombok.Singular;
import lombok.Value;

import com.silenteight.adjudication.api.v1.Alert;
import com.silenteight.adjudication.api.v1.BatchCreateAlertMatchesRequest;
import com.silenteight.adjudication.api.v1.CreateAlertRequest;
import com.silenteight.adjudication.api.v1.Match;

import com.google.common.base.Preconditions;
import com.google.protobuf.Timestamp;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Locale;
import javax.annotation.Nonnull;

import static java.util.stream.Collectors.toList;

@Value
@Builder
public class RegisterAlertRequest {

  @NonNull
  String alertId;

  @Default
  int priority = 5;

  @NonNull
  Timestamp alertTime;

  @NonNull
  List<String> matchIds;

  @Singular
  List<Label> labels;

  @Nonnull
  public BatchCreateAlertMatchesRequest toCreateMatchesRequest(String alertName) {
    if (matchIds.isEmpty())
      throw new IllegalStateException("Match ids must not be empty");

    return BatchCreateAlertMatchesRequest
        .newBuilder()
        .addAllMatches(createMatches())
        .setAlert(alertName)
        .build();
  }

  @Nonnull
  private List<Match> createMatches() {
    return matchIds.stream().map(RegisterAlertRequest::createMatch).collect(toList());
  }

  @Nonnull
  private static Match createMatch(String matchId) {
    return Match.newBuilder().setMatchId(matchId).build();
  }

  @Nonnull
  public CreateAlertRequest toCreateAlertRequest() {
    Preconditions.checkState(!matchIds.isEmpty(), "At least one match is required.");
    return CreateAlertRequest.newBuilder().setAlert(toAlert()).build();
  }

  public Alert toAlert() {
    var alert = Alert
        .newBuilder()
        .setAlertId(getAlertId())
        .setPriority(getPriority())
        .setAlertTime(alertTime);

    var matchQuantity = matchIds.size() > 1 ? MatchQuantity.MANY : MatchQuantity.SINGLE;
    alert.putLabels("matchQuantity", matchQuantity.name().toLowerCase(Locale.ROOT));

    if (!CollectionUtils.isEmpty(labels)) {
      labels.forEach(l -> alert.putLabels(l.getName(), l.getValue()));
    }

    return alert.build();
  }
}

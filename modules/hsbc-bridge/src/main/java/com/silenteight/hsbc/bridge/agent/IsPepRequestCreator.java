package com.silenteight.hsbc.bridge.agent;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.json.external.model.AlertData;
import com.silenteight.hsbc.bridge.json.external.model.CaseComment;
import com.silenteight.hsbc.bridge.json.external.model.CustomerIndividual;
import com.silenteight.hsbc.bridge.json.external.model.WorldCheckIndividual;
import com.silenteight.proto.learningstore.ispep.v1.api.Alert;
import com.silenteight.proto.learningstore.ispep.v1.api.Comment;
import com.silenteight.proto.learningstore.ispep.v1.api.IsPepLearningStoreExchangeRequest;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
class IsPepRequestCreator {

  private final AgentTimestampMapper timestampMapper;

  public IsPepLearningStoreExchangeRequest create(Collection<AlertData> alerts) {
    return IsPepLearningStoreExchangeRequest.newBuilder()
        .addAllAlerts(mapAlert(alerts))
        .build();
  }

  private List<Alert> mapAlert(Collection<AlertData> alerts) {
    return alerts.stream()
        .filter(e -> !e.getWorldCheckIndividuals().isEmpty())
        .map(this::toAlert)
        .collect(toList());
  }

  private Alert toAlert(AlertData alert) {
    var builder = Alert.newBuilder();
    findApCountry(alert.getCustomerIndividuals()).ifPresent(builder::setAlertedPartyCountry);
    findWatchlistId(alert.getWorldCheckIndividuals()).ifPresent(builder::setWatchlistId);

    return builder
        .setAlertId(alert.getId())
        .setMatchId(alert.getCaseId())
        .addAllComments(mapComments(alert.getCaseComments()))
        .build();
  }

  private List<Comment> mapComments(List<CaseComment> caseComments) {
    return caseComments.stream()
        .map(e -> Comment.newBuilder()
            .setId(e.getCommentId())
            .setValue(e.getCaseComment())
            .setCreatedAt(timestampMapper.toUnixTimestamp(e.getCommentDateTime()))
            .build())
        .collect(toList());
  }

  private Optional<String> findApCountry(List<CustomerIndividual> customers) {
    return customers.stream()
        .findFirst()
        .map(CustomerIndividual::getEdqLobCountryCode);
  }

  private Optional<String> findWatchlistId(List<WorldCheckIndividual> worldchecks) {
    return worldchecks.stream()
        .findFirst()
        .map(WorldCheckIndividual::getListRecordId);
  }
}

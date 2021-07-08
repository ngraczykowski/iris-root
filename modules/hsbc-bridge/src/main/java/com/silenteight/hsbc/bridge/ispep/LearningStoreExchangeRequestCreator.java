package com.silenteight.hsbc.bridge.ispep;

import lombok.experimental.UtilityClass;

import com.silenteight.hsbc.bridge.json.external.model.AlertData;
import com.silenteight.hsbc.bridge.json.external.model.CaseComment;
import com.silenteight.hsbc.bridge.json.external.model.CustomerIndividual;
import com.silenteight.hsbc.bridge.json.external.model.WorldCheckIndividual;
import com.silenteight.proto.learningstore.ispep.v1.api.Alert;
import com.silenteight.proto.learningstore.ispep.v1.api.Comment;
import com.silenteight.proto.learningstore.ispep.v1.api.IsPepLearningStoreExchangeRequest;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@UtilityClass
class LearningStoreExchangeRequestCreator {

  private static final DateTimeFormatter DATE_FORMATTER = new DateTimeFormatterBuilder()
      .parseCaseInsensitive().appendPattern("dd-MMM-yy")
      .toFormatter(Locale.ENGLISH);

  public static IsPepLearningStoreExchangeRequest create(Collection<AlertData> alerts) {
    return IsPepLearningStoreExchangeRequest.newBuilder()
        .addAllAlerts(mapAlert(alerts))
        .build();
  }

  private static List<com.silenteight.proto.learningstore.ispep.v1.api.Alert> mapAlert(
      Collection<AlertData> alerts) {
    return alerts.stream().map(LearningStoreExchangeRequestCreator::toAlert).collect(toList());
  }

  private static com.silenteight.proto.learningstore.ispep.v1.api.Alert toAlert(AlertData alert) {
    var builder = Alert.newBuilder();
    findApCountry(alert.getCustomerIndividuals()).ifPresent(builder::setAlertedPartyCountry);
    findWatchlistId(alert.getWorldCheckIndividuals()).ifPresent(builder::setWatchlistId);

    return builder
        .setAlertId(alert.getId())
        .setMatchId(alert.getCaseId())
        .addAllComments(mapComments(alert.getCaseComments()))
        .build();
  }

  private static List<Comment> mapComments(List<CaseComment> caseComments) {
    return caseComments.stream()
        .map(e -> Comment.newBuilder()
            .setId(e.getCommentId())
            .setValue(e.getCaseComment())
            .setCreatedAt(parseCommentDate(e.getCommentDateTime()))
            .build())
        .collect(Collectors.toList());
  }

  private static long parseCommentDate(String rawDate) {
    try {
      var date = LocalDate.parse(rawDate, DATE_FORMATTER);
      return date.atStartOfDay(ZoneId.of("UTC")).toEpochSecond();
    } catch (DateTimeParseException e) {
      throw new DateParsingException(e);
    }
  }

  private static Optional<String> findApCountry(List<CustomerIndividual> customers) {
    return customers.stream()
        .findFirst()
        .map(CustomerIndividual::getEdqLobCountryCode);
  }

  private static Optional<String> findWatchlistId(List<WorldCheckIndividual> worldchecks) {
    return worldchecks.stream()
        .findFirst()
        .map(WorldCheckIndividual::getListRecordId);
  }
}

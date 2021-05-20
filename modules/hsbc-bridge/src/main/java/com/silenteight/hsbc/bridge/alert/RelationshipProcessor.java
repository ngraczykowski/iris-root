package com.silenteight.hsbc.bridge.alert;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.bridge.alert.ProcessingResult.ProcessedAlert;
import com.silenteight.hsbc.bridge.json.external.model.*;
import com.silenteight.hsbc.bridge.match.Match;

import io.jsonwebtoken.lang.Collections;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.Optional.empty;
import static java.util.stream.Collectors.toList;

@Slf4j
class RelationshipProcessor {

  ProcessingResult process(@NonNull List<AlertData> alertsData) {
    var processedAlerts = alertsData.stream()
        .map(a -> new CaseIdAlertProcessor(a).process())
        .collect(toList());

    return new ProcessingResult(processedAlerts);
  }

  @AllArgsConstructor
  static class CaseIdAlertProcessor {

    final AlertData alertData;

    ProcessedAlert process() {
      var processedAlert = ProcessedAlert.builder()
          .discriminator(alertData.getFlagKey())
          .externalId(alertData.getId());

      var validationError = validateRelationships();
      if (validationError.isPresent()) {
        processedAlert.errorMessage(validationError.get());
        processedAlert.matches(List.of());
      } else {
        processedAlert.matches(getMatches());
      }

      return processedAlert.build();
    }

    private List<Match> getMatches() {
      return List.of(createMatch());
    }

    private Match createMatch() {
      return new Match(alertData.getCaseId(), createHsbcMatch());
    }

    private HsbcMatch createHsbcMatch() {
      var hsbcMatch = new HsbcMatch();
      hsbcMatch.setCaseInformation(alertData.getCaseInformation());
      hsbcMatch.setCtrpScreeningEntities(alertData.getCtrpScreeningEntities());
      hsbcMatch.setCtrpScreeningIndividuals(alertData.getCtrpScreeningIndividuals());
      hsbcMatch.setPrivateListEntities(alertData.getPrivateListEntities());
      hsbcMatch.setPrivateListIndividuals(alertData.getPrivateListIndividuals());
      hsbcMatch.setWorldCheckEntities(alertData.getWorldCheckEntities());
      hsbcMatch.setWorldCheckIndividuals(alertData.getWorldCheckIndividuals());

      getCustomerEntity().ifPresent(hsbcMatch::setCustomerEntity);
      getCustomerIndividual().ifPresent(hsbcMatch::setCustomerIndividual);
      return hsbcMatch;
    }

    private Optional<CustomerIndividual> getCustomerIndividual() {
      if (Collections.isEmpty(alertData.getCustomerIndividuals())) {
        return empty();
      }

      return alertData.getCustomerIndividuals().stream().findFirst();
    }

    private Optional<CustomerEntity> getCustomerEntity() {
      if (Collections.isEmpty(alertData.getCustomerEntities())) {
        return empty();
      }

      return alertData.getCustomerEntities().stream().findFirst();
    }

    private Optional<String> validateRelationships() {
      if (getRelationships().isEmpty()) {
        return Optional.of("No relationships defined!");
      }

      if (existCustomerEntityOrIndividualWithoutRelationship()) {
        return Optional.of("Customer data without relationships!");
      }

      if (existWatchlistDataWithoutRelationship()) {
        return Optional.of("Watchlist data without relationships!");
      }

      return Optional.empty();
    }

    private boolean existWatchlistDataWithoutRelationship() {
      var watchlistRecordIds = Stream.of(
          alertData.getCtrpScreeningEntities().stream().map(CtrpScreeningIndividual::getRecordId),
          alertData.getCtrpScreeningIndividuals().stream().map(CtrpScreeningIndividual::getRecordId),
          alertData.getPrivateListEntities().stream().map(PrivateListEntity::getRecordId),
          alertData.getPrivateListIndividuals().stream().map(PrivateListIndividual::getRecordId),
          alertData.getWorldCheckEntities().stream().map(WorldCheckEntity::getRecordId),
          alertData.getWorldCheckIndividuals().stream().map(WorldCheckIndividual::getRecordId)
      ).flatMap(id -> id);

      return watchlistRecordIds.anyMatch(this::relationshipsDoesNotContainRelatedRecordId);
    }

    private boolean existCustomerEntityOrIndividualWithoutRelationship() {
      var customerRecordIds = Stream.concat(
          alertData.getCustomerEntities().stream().map(CustomerEntity::getRecordId),
          alertData.getCustomerIndividuals().stream().map(CustomerIndividual::getRecordId)
      );

      return customerRecordIds.anyMatch(this::relationshipsDoesNotContainRecordId);
    }

    private boolean relationshipsDoesNotContainRecordId(String recordId) {
      return getRelationships().stream()
          .map(Relationship::getRecordId)
          .noneMatch(m -> m.equals(recordId));
    }

    private boolean relationshipsDoesNotContainRelatedRecordId(String recordId) {
      return getRelationships().stream()
          .map(Relationship::getRelatedRecordId)
          .noneMatch(m -> m.equals(recordId));
    }

    private List<Relationship> getRelationships() {
      return alertData.getRelationships();
    }
  }
}

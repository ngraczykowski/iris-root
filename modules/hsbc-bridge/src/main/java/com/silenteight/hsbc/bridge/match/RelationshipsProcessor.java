package com.silenteight.hsbc.bridge.match;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.alert.AlertRawData;
import com.silenteight.hsbc.bridge.domain.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
class RelationshipsProcessor {

  private final AlertRawData alertRawData;

  List<MatchRawData> process() {
    var relatedPairs = alertRawData.getRelationships().stream()
        .flatMap(this::processIndividualAndEntity)
        .collect(toList());

    if (relatedPairs.isEmpty()) {
      throw new NoRelatedPairException();
    }

    return relatedPairs;
  }

  private Stream<MatchRawData> processIndividualAndEntity(Relationships relationship) {
    return Stream
        .concat(
            processEntityRelatedPairs(relationship).stream(),
            processIndividualRelatedPairs(relationship).stream());
  }

  private Optional<MatchRawData> processIndividualRelatedPairs(Relationships relationship) {
    var caseId = relationship.getCaseId();
    var individualsResolver = new IndividualsResolver(relationship, alertRawData);

    return individualsResolver.getCustomerIndividuals().stream()
        .map(customerIndividual -> createIndividualComposite(
            customerIndividual, individualsResolver))
        .map(individualComposite -> createIndividualPair(caseId, individualComposite))
        .findFirst();
  }

  private Optional<MatchRawData> processEntityRelatedPairs(Relationships relationship) {

    var caseId = relationship.getCaseId();
    var entitiesResolver = new EntitiesResolver(relationship, alertRawData);

    return entitiesResolver.getCustomerEntities().stream()
        .map(customerEntity -> createEntityComposite(
            customerEntity, entitiesResolver))
        .map(entityComposite -> createEntitiesPair(caseId, entityComposite))
        .findFirst();
  }

  private static class IndividualsResolver {

    final Relationships relationship;
    final AlertRawData alertRawData;

    IndividualsResolver(Relationships relationship, AlertRawData alertRawData) {
      this.relationship = relationship;
      this.alertRawData = alertRawData;
    }

    private List<CustomerIndividuals> getCustomerIndividuals() {
      return alertRawData.getCustomerIndividuals().stream()
          .filter(customerIndividual -> customerIndividual
              .getRecordId().equals(relationship.getRecordId()))
          .collect(toList());
    }

    private List<WorldCheckIndividuals> getWorkCheckIndividuals() {
      return alertRawData.getWorldCheckIndividuals().stream()
          .filter(worldCheckIndividual -> worldCheckIndividual
              .getRecordId().equals(relationship.getRelatedRecordId()))
          .collect(toList());
    }

    private List<PrivateListIndividuals> getPrivateListIndividuals() {
      return alertRawData.getPrivateListIndividuals().stream()
          .filter(privateListIndividual -> privateListIndividual
              .getRecordId().equals(relationship.getRelatedRecordId()))
          .collect(toList());
    }

    private List<CountryCtrpScreening> getCtrpScreeningIndividuals() {
      return alertRawData.getCountryCtrpScreeningIndividuals().stream()
          .filter(countryCtrpScreeningIndividual -> countryCtrpScreeningIndividual
              .getRecordId().equals(relationship.getRelatedRecordId()))
          .collect(toList());
    }
  }

  private static class EntitiesResolver {

    final Relationships relationship;
    final AlertRawData alertRawData;

    EntitiesResolver(Relationships relationship, AlertRawData alertRawData) {
      this.relationship = relationship;
      this.alertRawData = alertRawData;
    }

    private List<CustomerEntities> getCustomerEntities() {
      return alertRawData.getCustomerEntities().stream()
          .filter(customerEntity -> customerEntity
              .getRecordId().equals(relationship.getRecordId()))
          .collect(toList());
    }

    private List<WorldCheckEntities> getWorkCheckEntities() {
      return alertRawData.getWorldCheckEntities().stream()
          .filter(worldCheckEntity -> worldCheckEntity
              .getRecordId().equals(relationship.getRelatedRecordId()))
          .collect(toList());
    }

    private List<PrivateListEntities> getPrivateListEntities() {
      return alertRawData.getPrivateListEntities().stream()
          .filter(privateListEntity -> privateListEntity
              .getRecordId().equals(relationship.getRelatedRecordId()))
          .collect(toList());
    }

    private List<CountryCtrpScreening> getCtrpScreeningEntities() {
      return alertRawData.getCountryCtrpScreeningEntities().stream()
          .filter(countryCtrpScreeningEntities -> countryCtrpScreeningEntities
              .getRecordId().equals(relationship.getRelatedRecordId()))
          .collect(toList());
    }
  }

  private MatchRawData createIndividualPair(int caseId, IndividualComposite individualComposite) {
    return new MatchRawData(caseId, findCaseWithAlertUrl(caseId), individualComposite);
  }

  private CasesWithAlertURL findCaseWithAlertUrl(int caseId) {
    return alertRawData
        .getCasesWithAlertURL()
        .stream()
        .filter(c -> c.getId() == caseId)
        .findFirst()
        .orElseThrow(() -> new MissingCaseWithAlertUrlException(caseId));
  }

  private IndividualComposite createIndividualComposite(
      CustomerIndividuals relatedCustomerIndividual, IndividualsResolver individualsResolver) {
    return new IndividualComposite(
        relatedCustomerIndividual,
        individualsResolver.getWorkCheckIndividuals(),
        individualsResolver.getPrivateListIndividuals(),
        individualsResolver.getCtrpScreeningIndividuals());
  }

  private MatchRawData createEntitiesPair(int caseId, EntityComposite entityComposite) {
    return new MatchRawData(caseId, findCaseWithAlertUrl(caseId), entityComposite);
  }

  private EntityComposite createEntityComposite(
      CustomerEntities relatedCustomerEntity, EntitiesResolver entitiesResolver) {
    return new EntityComposite(
        relatedCustomerEntity,
        entitiesResolver.getWorkCheckEntities(),
        entitiesResolver.getPrivateListEntities(),
        entitiesResolver.getCtrpScreeningEntities());
  }

  static class NoRelatedPairException extends RuntimeException {

    public NoRelatedPairException() {
      super("No related objects pairs was found !");
    }
  }

  static class MissingCaseWithAlertUrlException extends RuntimeException {

    public MissingCaseWithAlertUrlException(int caseId) {
      super("Entry with caseId: " + caseId + " not found");
    }
  }
}

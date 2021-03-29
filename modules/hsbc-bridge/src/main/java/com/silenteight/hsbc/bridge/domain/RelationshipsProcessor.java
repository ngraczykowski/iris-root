package com.silenteight.hsbc.bridge.domain;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class RelationshipsProcessor {

  public RelationshipsComposite processRelations(AlertSystemInformation alertSystemInformation) {

    var relatedPairs = alertSystemInformation.getRelationships().stream()
        .flatMap(relationship -> processIndividualAndEntity(alertSystemInformation, relationship))
        .collect(toList());

    if (relatedPairs.isEmpty()) {
      throw new NoRelatedPairException();
    }

    return RelationshipsComposite.builder().relatedPairs(relatedPairs).build();
  }

  private Stream<RelatedPair> processIndividualAndEntity(
      AlertSystemInformation alertSystemInformation,
      Relationships relationship) {

    return Stream
        .concat(
            processEntityRelatedPairs(alertSystemInformation, relationship).stream(),
            processIndividualRelatedPairs(alertSystemInformation, relationship).stream());
  }

  private Optional<RelatedPair> processIndividualRelatedPairs(
      AlertSystemInformation alertSystemInformation,
      Relationships relationship) {
    var individualsResolver = new IndividualsResolver(relationship, alertSystemInformation);

    return individualsResolver.getCustomerIndividuals().stream()
        .map(customerIndividual -> createIndividualComposite(
            customerIndividual, individualsResolver))
        .map(individualComposite -> createIndividualPair(relationship, individualComposite))
        .findFirst();
  }

  private Optional<RelatedPair> processEntityRelatedPairs(
      AlertSystemInformation alertSystemInformation,
      Relationships relationship) {

    var entitiesResolver = new EntitiesResolver(relationship, alertSystemInformation);

    return entitiesResolver.getCustomerEntities().stream()
        .map(customerEntity -> createEntityComposite(
            customerEntity, entitiesResolver))
        .map(entityComposite -> createEntitiesPair(relationship, entityComposite))
        .findFirst();
  }

  private static class IndividualsResolver {

    Relationships relationship;
    AlertSystemInformation alertSystemInformation;

    IndividualsResolver(Relationships relationship, AlertSystemInformation alertSystemInformation) {
      this.relationship = relationship;
      this.alertSystemInformation = alertSystemInformation;
    }

    private List<CustomerIndividuals> getCustomerIndividuals() {
      return alertSystemInformation.getCustomerIndividuals().stream()
          .filter(customerIndividual -> customerIndividual
              .getRecordId().equals(relationship.getRecordId()))
          .collect(toList());
    }

    private List<WorldCheckIndividuals> getWorkCheckIndividuals() {
      return alertSystemInformation.getWorldCheckIndividuals().stream()
          .filter(worldCheckIndividual -> worldCheckIndividual
              .getRecordId().equals(relationship.getRelatedRecordId()))
          .collect(toList());
    }

    private List<PrivateListIndividuals> getPrivateListIndividuals() {
      return alertSystemInformation.getPrivateListIndividuals().stream()
          .filter(privateListIndividual -> privateListIndividual
              .getRecordId().equals(relationship.getRelatedRecordId()))
          .collect(toList());
    }

    private List<CountryCtrpScreening> getCtrpScreeningIndividuals() {
      return alertSystemInformation.getCountryCtrpScreeningIndividuals().stream()
          .filter(countryCtrpScreeningIndividual -> countryCtrpScreeningIndividual
              .getRecordId().equals(relationship.getRelatedRecordId()))
          .collect(toList());
    }
  }

  private static class EntitiesResolver {

    Relationships relationship;
    AlertSystemInformation alertSystemInformation;

    EntitiesResolver(Relationships relationship, AlertSystemInformation alertSystemInformation) {
      this.relationship = relationship;
      this.alertSystemInformation = alertSystemInformation;
    }

    private List<CustomerEntities> getCustomerEntities() {
      return alertSystemInformation.getCustomerEntities().stream()
          .filter(customerEntity -> customerEntity
              .getRecordId().equals(relationship.getRecordId()))
          .collect(toList());
    }

    private List<WorldCheckEntities> getWorkCheckEntities() {
      return alertSystemInformation.getWorldCheckEntities().stream()
          .filter(worldCheckEntity -> worldCheckEntity
              .getRecordId().equals(relationship.getRelatedRecordId()))
          .collect(toList());
    }

    private List<PrivateListEntities> getPrivateListEntities() {
      return alertSystemInformation.getPrivateListEntities().stream()
          .filter(privateListEntity -> privateListEntity
              .getRecordId().equals(relationship.getRelatedRecordId()))
          .collect(toList());
    }

    private List<CountryCtrpScreening> getCtrpScreeningEntities() {
      return alertSystemInformation.getCountryCtrpScreeningEntities().stream()
          .filter(countryCtrpScreeningEntities -> countryCtrpScreeningEntities
              .getRecordId().equals(relationship.getRelatedRecordId()))
          .collect(toList());
    }
  }

  private RelatedPair createIndividualPair(
      Relationships relationship, IndividualComposite individualComposite) {
    return RelatedPair.builder()
        .caseId(relationship.getCaseId())
        .relatedCustomerIndividualsPair(individualComposite)
        .build();
  }

  private IndividualComposite createIndividualComposite(
      CustomerIndividuals relatedCustomerIndividual, IndividualsResolver individualsResolver) {
    return IndividualComposite.builder()
        .customerIndividuals(relatedCustomerIndividual)
        .worldCheckIndividuals(individualsResolver.getWorkCheckIndividuals())
        .privateListIndividuals(individualsResolver.getPrivateListIndividuals())
        .countryCtrpScreeningIndividuals(individualsResolver.getCtrpScreeningIndividuals())
        .build();
  }

  private RelatedPair createEntitiesPair(
      Relationships relationship, EntityComposite entityComposite) {
    return RelatedPair.builder()
        .caseId(relationship.getCaseId())
        .relatedCustomerEntitiesPair(entityComposite)
        .build();
  }

  private EntityComposite createEntityComposite(
      CustomerEntities relatedCustomerEntity, EntitiesResolver entitiesResolver) {
    return EntityComposite.builder()
        .customerEntities(relatedCustomerEntity)
        .worldCheckEntities(entitiesResolver.getWorkCheckEntities())
        .privateListEntities(entitiesResolver.getPrivateListEntities())
        .countryCtrpScreeningEntities(entitiesResolver.getCtrpScreeningEntities())
        .build();
  }

  static class NoRelatedPairException extends RuntimeException {

    public NoRelatedPairException() {
      super("No related objects pairs was found !");
    }
  }
}

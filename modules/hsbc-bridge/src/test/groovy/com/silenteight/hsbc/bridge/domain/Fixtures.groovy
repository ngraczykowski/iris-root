package com.silenteight.hsbc.bridge.domain

class Fixtures {

  def individualCaseId = 1
  def individualRecordId = 2
  def individualRelatedRecordId = 3
  def entitiesCaseId = 4
  def entitiesRecordId = 5
  def entitiesRelatedRecordId = 6

  def individualRelationship = new Relationships(
      caseId: individualCaseId,
      recordId: individualRecordId,
      relatedRecordId: individualRelatedRecordId
  )

  def customerIndividuals = new CustomerIndividuals(
      caseId: individualCaseId,
      recordId: individualRecordId
  )

  def worldCheckIndividual = new WorldCheckIndividuals(
      caseId: individualCaseId,
      recordId: individualRelatedRecordId
  )

  def privateListIndividuals = new PrivateListIndividuals(
      caseId: individualCaseId,
      recordId: individualRelatedRecordId
  )

  def countryCtrpScreeningIndividuals = new CountryCtrpScreening(
      caseId: individualCaseId,
      recordId: individualRelatedRecordId
  )

  def entitiesRelationship = new Relationships(
      caseId: entitiesCaseId,
      recordId: entitiesRecordId,
      relatedRecordId: entitiesRelatedRecordId
  )

  def customerEntities = new CustomerEntities(
      caseId: entitiesCaseId,
      recordId: entitiesRecordId
  )

  def worldCheckEntities = new WorldCheckEntities(
      caseId: entitiesCaseId,
      recordId: entitiesRelatedRecordId
  )

  def privateListEntities = new PrivateListEntities(
      caseId: entitiesCaseId,
      recordId: entitiesRelatedRecordId
  )

  def countryCtrpScreeningEntities = new CountryCtrpScreening(
      caseId: entitiesCaseId,
      recordId: entitiesRelatedRecordId
  )

  def alertSystemInformation = new AlertSystemInformation(
      relationships: [individualRelationship, entitiesRelationship],
      customerEntities: [customerEntities],
      worldCheckEntities: [worldCheckEntities],
      privateListEntities: [privateListEntities],
      countryCtrpScreeningEntities: [countryCtrpScreeningEntities],
      customerIndividuals: [customerIndividuals],
      worldCheckIndividuals: [worldCheckIndividual],
      privateListIndividuals: [privateListIndividuals],
      countryCtrpScreeningIndividuals: [countryCtrpScreeningIndividuals]
  )

  def alertSystemInformationWithoutCustomer = new AlertSystemInformation(
      relationships: [individualRelationship, entitiesRelationship],
      customerEntities: [],
      worldCheckEntities: [worldCheckEntities],
      privateListEntities: [privateListEntities],
      countryCtrpScreeningEntities: [countryCtrpScreeningEntities],
      customerIndividuals: [],
      worldCheckIndividuals: [worldCheckIndividual],
      privateListIndividuals: [privateListIndividuals],
      countryCtrpScreeningIndividuals: [countryCtrpScreeningIndividuals]
  )
}

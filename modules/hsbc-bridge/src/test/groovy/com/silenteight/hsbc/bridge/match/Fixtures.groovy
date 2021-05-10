package com.silenteight.hsbc.bridge.match

import com.silenteight.hsbc.bridge.domain.CasesWithAlertURL
import com.silenteight.hsbc.bridge.domain.CountryCtrpScreening
import com.silenteight.hsbc.bridge.domain.CustomerEntities
import com.silenteight.hsbc.bridge.domain.CustomerIndividuals
import com.silenteight.hsbc.bridge.domain.PrivateListEntities
import com.silenteight.hsbc.bridge.domain.PrivateListIndividuals
import com.silenteight.hsbc.bridge.domain.Relationships
import com.silenteight.hsbc.bridge.domain.WorldCheckEntitiesOld
import com.silenteight.hsbc.bridge.domain.WorldCheckIndividualsOld

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

  def worldCheckIndividual = new WorldCheckIndividualsOld(
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

  def worldCheckEntities = new WorldCheckEntitiesOld(
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

  def caseWithAlertUrlEntity = new CasesWithAlertURL(
      id: entitiesCaseId
  )

  def caseWithAlertUrlIndividual = new CasesWithAlertURL(
      id: individualCaseId
  )
}

package com.silenteight.hsbc.bridge.match

import com.silenteight.hsbc.bridge.bulk.rest.input.AlertSystemInformation
import com.silenteight.hsbc.bridge.bulk.rest.input.CasesWithAlertURL
import com.silenteight.hsbc.bridge.bulk.rest.input.CountryCtrpScreeningEntities
import com.silenteight.hsbc.bridge.bulk.rest.input.CountryCtrpScreeningIndividuals
import com.silenteight.hsbc.bridge.bulk.rest.input.CustomerEntities
import com.silenteight.hsbc.bridge.bulk.rest.input.CustomerIndividuals
import com.silenteight.hsbc.bridge.bulk.rest.input.WorldCheckEntities
import com.silenteight.hsbc.bridge.bulk.rest.input.WorldCheckIndividuals

import spock.lang.Specification

class MatchRawMapperSpec extends Specification {

  def underTest = new MatchRawMapper()
  def fixtures = new Fixtures()

  def 'should map alert to raw match data'() {
    when:
    def result = underTest.map(fixtures.systemInformation)

    then:
    with(result.countryCtrpScreeningEntities.first()) {
      caseId == fixtures.countryCtrpScreeningEntities.caseId
    }
    with(result.countryCtrpScreeningIndividuals.first()) {
      caseId == fixtures.countryCtrpScreeningIndividuals.caseId
    }
    with(result.worldCheckEntities.first()) {
      caseId == fixtures.worldCheckEntities.caseId
    }
    with(result.worldCheckIndividuals.first()) {
      caseId == fixtures.worldCheckIndividual.caseId
    }
    with(result.customerEntities.first()) {
      caseId == fixtures.customerEntities.caseId
    }
    with(result.customerIndividuals.first()) {
      caseId == fixtures.customerIndividuals.caseId
    }
  }

  class Fixtures {

    def caseId = 1

    def worldCheckIndividual = new WorldCheckIndividuals(
        caseId: caseId
    )

    def worldCheckEntities = new WorldCheckEntities(
        caseId: caseId
    )

    def customerIndividuals = new CustomerIndividuals(
        caseId: caseId
    )

    def customerEntities = new CustomerEntities(
        caseId: caseId
    )

    def countryCtrpScreeningIndividuals = new CountryCtrpScreeningIndividuals(
        caseId: caseId
    )

    def countryCtrpScreeningEntities = new CountryCtrpScreeningEntities(
        caseId: caseId
    )

    def systemInformation = new AlertSystemInformation(
        casesWithAlertURL: [new CasesWithAlertURL()],
        countryCtrpScreeningEntities: [countryCtrpScreeningEntities],
        countryCtrpScreeningIndividuals: [countryCtrpScreeningIndividuals],
        customerEntities: [customerEntities],
        customerIndividuals: [customerIndividuals],
        worldCheckEntities: [worldCheckEntities],
        worldCheckIndividuals: [worldCheckIndividual]
    )
  }
}

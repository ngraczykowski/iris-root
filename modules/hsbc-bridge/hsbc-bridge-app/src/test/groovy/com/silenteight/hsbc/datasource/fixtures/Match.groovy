package com.silenteight.hsbc.datasource.fixtures

import com.silenteight.hsbc.bridge.json.internal.model.CaseInformation
import com.silenteight.hsbc.bridge.json.internal.model.CustomerIndividual
import com.silenteight.hsbc.bridge.json.internal.model.PrivateListIndividual
import com.silenteight.hsbc.bridge.json.internal.model.WorldCheckIndividual
import com.silenteight.hsbc.bridge.match.MatchRawData
import com.silenteight.hsbc.datasource.datamodel.MatchData

trait Match {

  static MatchData MATCH_WITHOUT_AP_GENDER_DERIVED_FLAG_1 = new MatchRawData(
      caseInformation: new CaseInformation(id: 1),
      customerEntities: [null],
      customerIndividuals: [new CustomerIndividual(
          gender: "M",
      )],
      worldCheckIndividuals: [new WorldCheckIndividual(
          gender: "M",
          genderDerivedFlag: "N"
      )],
      privateListIndividuals: [new PrivateListIndividual(
          gender: "M",
          genderDerivedFlag: "N"
      )],
      negativeNewsScreeningIndividuals: [])

  static MatchData MATCH_WITHOUT_WP_GENDER_DERIVED_FLAG_1 = new MatchRawData(
      caseInformation: new CaseInformation(id: 1),
      customerEntities: [null],
      customerIndividuals: [new CustomerIndividual(
          gender: "M",
          genderDerivedFlag: "N",
      )],
      worldCheckIndividuals: [new WorldCheckIndividual(
          gender: "M",
      )],
      privateListIndividuals: [new PrivateListIndividual(
          gender: "M",
      )],
      negativeNewsScreeningIndividuals: [])
}

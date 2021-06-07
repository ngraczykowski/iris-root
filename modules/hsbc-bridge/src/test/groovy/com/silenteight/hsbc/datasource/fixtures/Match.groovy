package com.silenteight.hsbc.datasource.fixtures

import com.silenteight.hsbc.bridge.json.internal.model.CaseInformation
import com.silenteight.hsbc.bridge.json.internal.model.CustomerIndividual
import com.silenteight.hsbc.bridge.json.internal.model.PrivateListIndividual
import com.silenteight.hsbc.bridge.json.internal.model.WorldCheckIndividual
import com.silenteight.hsbc.datasource.datamodel.MatchData

trait Match {

  static MatchData MATCH_WITHOUT_AP_GENDER_DERIVED_FLAG_1 = [
      getCaseInformation       : {new CaseInformation(id: 1)},
      getCustomerIndividual    : {
        new CustomerIndividual(
            gender: "M",
        )
      },
      getWorldCheckIndividuals : {
        [new WorldCheckIndividual(
            gender: "M",
            genderDerivedFlag: "N"
        )]
      },
      getPrivateListIndividuals: {
        [new PrivateListIndividual(
            gender: "M",
            genderDerivedFlag: "N"
        )]
      }
  ] as MatchData

  static MatchData MATCH_WITHOUT_WP_GENDER_DERIVED_FLAG_1 = [
      getCaseInformation       : {new CaseInformation(id: 1)},
      getCustomerIndividual    : {
        new CustomerIndividual(
            gender: "M",
            genderDerivedFlag: "N",

        )
      },
      getWorldCheckIndividuals : {
        [new WorldCheckIndividual(
            gender: "M",
        )]
      },
      getPrivateListIndividuals: {
        [new PrivateListIndividual(
            gender: "M",
        )]
      }
  ] as MatchData
}

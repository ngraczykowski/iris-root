package com.silenteight.payments.bridge.svb.newlearning.step.etl.feature.service;

import com.silenteight.payments.bridge.svb.newlearning.domain.EtlHit;
import com.silenteight.payments.bridge.svb.newlearning.domain.HitComposite;
import com.silenteight.payments.bridge.svb.oldetl.response.AlertedPartyData;

class FeatureFixture {

  public static EtlHit createEtlHit() {
    return EtlHit.builder().alertedPartyData(
            AlertedPartyData
                .builder()
                .accountNumber("account")
                .address("address")
                .nameAddress("nameAddress")
                .ctryTown("ctryTown")
                .build())
        .hitComposite(
            HitComposite
                .builder()
                .fkcoVListCountry("listCountry")
                .fkcoVListState("state")
                .fkcoVListCity("city")
                .build())
        .build();
  }
}

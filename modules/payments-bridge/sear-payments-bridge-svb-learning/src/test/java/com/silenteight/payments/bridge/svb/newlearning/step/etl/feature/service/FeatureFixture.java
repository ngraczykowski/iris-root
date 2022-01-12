package com.silenteight.payments.bridge.svb.newlearning.step.etl.feature.service;

import com.silenteight.payments.bridge.ae.alertregistration.domain.RegisterAlertResponse;
import com.silenteight.payments.bridge.ae.alertregistration.domain.RegisterMatchResponse;
import com.silenteight.payments.bridge.svb.newlearning.domain.EtlHit;
import com.silenteight.payments.bridge.svb.newlearning.domain.HitComposite;
import com.silenteight.payments.bridge.svb.oldetl.response.AlertedPartyData;

import java.util.List;

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
                .fkcoVListFmmId("fmmId")
                .fkcoVMatchedTag("tag")
                .fkcoISequence("1")
                .fkcoVListCountry("listCountry")
                .fkcoVListState("state")
                .fkcoVListCity("city")
                .fkcoVMatchedTagContent("content")
                .fkcoVCityMatchedText("city")
                .fkcoVStateMatchedText("state")
                .fkcoVCountryMatchedText("country")
                .fkcoVAddressMatchedText("address")
                .fkcoVListMatchedName("listmatchedname")
                .fkcoVHitType("type")
                .fkcoVListType("INDIVIDUAL")
                .fkcoVListName("name,name")
                .fkcoVNameMatchedText("name matched text")
                .build())
        .build();
  }

  static RegisterAlertResponse createRegisterAlert() {
    return RegisterAlertResponse
        .builder()
        .alertName("alerts/1")
        .systemId("SystemId")
        .matchResponses(
            List.of(RegisterMatchResponse
                .builder()
                .matchId("fmmId(tag, #1)")
                .matchName("matches/1")
                .build()))
        .build();
  }
}

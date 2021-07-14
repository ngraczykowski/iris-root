package com.silenteight.searpayments.scb.mapper;


import com.silenteight.searpayments.scb.etl.response.HitData;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class CreateNameAddressCrossmatchAgentRequestFactory {

    @NonNull
    private final CreateAlertPartyEntitiesFactory createAlertPartyEntitiesFactory;

    public CreateNameAddressCrossmatchAgentRequest create(HitData requestHitDto) {
        return new CreateNameAddressCrossmatchAgentRequest(
                requestHitDto, createAlertPartyEntitiesFactory);
    }
}

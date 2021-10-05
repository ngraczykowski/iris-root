package com.silenteight.payments.bridge.firco.datasource.service.process.category;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.categories.api.v2.CategoryValue;
import com.silenteight.payments.bridge.agents.port.SanctionedNationalityUseCase;
import com.silenteight.payments.bridge.common.dto.input.AlertMessageDto;
import com.silenteight.payments.bridge.event.AlertRegisteredEvent;
import com.silenteight.payments.bridge.svb.etl.response.HitData;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;

@Service
@Qualifier("sanctionedNationality")
@RequiredArgsConstructor
class SanctionedNationalityProcess implements CategoryValueProcess {

  private final SanctionedNationalityUseCase sanctionedNationalityUseCase;

  @Override
  public CategoryValue extract(AlertRegisteredEvent data, HitData hitData, String matchValue) {
    var value = sanctionedNationalityUseCase.invoke(createRequest(data));
    return CategoryValue
        .newBuilder()
        .setName("categories/sanctionedNationality")
        .setMatch(matchValue)
        .setSingleValue(value.toString())
        .build();
  }

  @Nonnull
  private String createRequest(AlertRegisteredEvent data) {
    AlertMessageDto alertMessageDto = data.getData(AlertMessageDto.class);
    return alertMessageDto.getMessageData();
  }
}

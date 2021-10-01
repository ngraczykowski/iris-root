package com.silenteight.payments.bridge.firco.datasource.service.process;

import com.silenteight.payments.bridge.common.dto.input.AlertMessageDto;
import com.silenteight.payments.bridge.common.model.AlertData;
import com.silenteight.payments.bridge.event.AlertRegisteredEvent;
import com.silenteight.payments.bridge.firco.datasource.model.EtlProcess;
import com.silenteight.payments.bridge.svb.etl.response.AlertEtlResponse;

import org.springframework.stereotype.Component;

@Component
class CategoryEtlProcess implements EtlProcess {

  @Override
  public void extractAndLoad(AlertRegisteredEvent data, AlertEtlResponse alertEtlResponse) {
    // an example how to retrieve data from event
    var alertData = data.getData(AlertData.class);
    var alertMessageDto = data.getData(AlertMessageDto.class);
    // send data to service
  }

  @Override
  public boolean supports(AlertRegisteredEvent data) {
    // check whether etl-process should handle the received data
    return false;
  }
}

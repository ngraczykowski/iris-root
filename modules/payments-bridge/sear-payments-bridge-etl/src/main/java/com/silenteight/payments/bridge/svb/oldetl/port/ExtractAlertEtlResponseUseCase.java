package com.silenteight.payments.bridge.svb.oldetl.port;

import com.silenteight.payments.bridge.common.dto.input.AlertMessageDto;
import com.silenteight.payments.bridge.svb.oldetl.response.AlertEtlResponse;
import com.silenteight.payments.bridge.svb.oldetl.response.HitAndWatchlistPartyData;

import java.util.Map;

public interface ExtractAlertEtlResponseUseCase {

  AlertEtlResponse createAlertEtlResponse(AlertMessageDto alertMessageDto);

  Map<String, HitAndWatchlistPartyData> getWatchlistDataForMatch(AlertMessageDto alertMessageDto);

}

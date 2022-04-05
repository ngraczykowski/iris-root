package com.silenteight.scb.outputrecommendation.adapter.outgoing;

import lombok.NonNull;

import com.silenteight.scb.outputrecommendation.domain.model.CbsAlertRecommendation;

public interface RecomFunctionExecutorService {

  String execute(@NonNull CbsAlertRecommendation alertRecommendation);
}

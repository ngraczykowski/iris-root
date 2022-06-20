/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.outputrecommendation.adapter.outgoing;

import lombok.NonNull;

import com.silenteight.iris.bridge.scb.outputrecommendation.domain.model.CbsAlertRecommendation;

public interface RecomFunctionExecutorService {

  String execute(@NonNull CbsAlertRecommendation alertRecommendation);
}

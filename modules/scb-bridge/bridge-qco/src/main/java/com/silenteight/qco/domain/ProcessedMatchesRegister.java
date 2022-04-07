package com.silenteight.qco.domain;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.qco.domain.model.QcoRecommendationMatch;

import org.springframework.stereotype.Component;

@Slf4j
@Component
class ProcessedMatchesRegister {

  void register(QcoRecommendationMatch match) {
    //TODO: implement
    log.info("The processed match has been registered");
  }
}
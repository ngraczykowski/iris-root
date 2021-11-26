package com.silenteight.adjudication.engine.analysis.pii;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PiiFacade {

  private final RemovePiiDataUseCase removePiiDataUseCase;

  public void removePii(List<String> alertsNames) {
    removePiiDataUseCase.removePii(alertsNames);
  }
}

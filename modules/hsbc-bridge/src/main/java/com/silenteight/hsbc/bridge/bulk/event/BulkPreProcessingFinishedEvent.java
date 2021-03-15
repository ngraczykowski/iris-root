package com.silenteight.hsbc.bridge.bulk.event;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.domain.AlertMatchIdComposite;

import java.util.Collection;

@RequiredArgsConstructor
public class BulkPreProcessingFinishedEvent {

  private final Collection<AlertMatchIdComposite> alertMatchIdComposites;
}

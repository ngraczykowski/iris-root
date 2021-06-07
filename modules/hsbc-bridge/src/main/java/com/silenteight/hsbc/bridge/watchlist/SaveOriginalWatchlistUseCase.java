package com.silenteight.hsbc.bridge.watchlist;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.file.SaveResourceUseCase;
import com.silenteight.hsbc.bridge.watchlist.event.OriginalWatchlistSavedEvent;

import org.springframework.context.ApplicationEventPublisher;

@RequiredArgsConstructor
class SaveOriginalWatchlistUseCase {

  private final SaveResourceUseCase saveFileUseCase;
  private final ApplicationEventPublisher eventPublisher;

  void save(RawWatchlistData core, RawWatchlistData aliases, RawWatchlistData keywords) {
    var savedCore = saveFileUseCase.save(core.getInputStream(), core.getName());
    var savedAliases = saveFileUseCase.save(aliases.getInputStream(), aliases.getName());
    var savedKeywords = saveFileUseCase.save(keywords.getInputStream(), keywords.getName());

    eventPublisher.publishEvent(OriginalWatchlistSavedEvent.builder()
        .coreWatchlistUri(savedCore.getUri())
        .aliasesWatchlistUri(savedAliases.getUri())
        .keywordsWatchlistUri(savedKeywords.getUri())
        .build());
  }
}

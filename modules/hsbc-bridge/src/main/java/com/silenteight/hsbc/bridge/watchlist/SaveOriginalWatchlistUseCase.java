package com.silenteight.hsbc.bridge.watchlist;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.watchlist.event.OriginalWatchlistSavedEvent;

import org.springframework.context.ApplicationEventPublisher;

@RequiredArgsConstructor
class SaveOriginalWatchlistUseCase {

  private final WatchlistSaver watchlistSaver;
  private final ApplicationEventPublisher eventPublisher;

  void save(RawWatchlistData core, RawWatchlistData aliases, RawWatchlistData keywords) {
    var savedCore = watchlistSaver.save(core.getInputStream(), core.getName());
    var savedAliases = watchlistSaver.save(aliases.getInputStream(), aliases.getName());
    var savedKeywords = watchlistSaver.save(keywords.getInputStream(), keywords.getName());

    eventPublisher.publishEvent(OriginalWatchlistSavedEvent.builder()
        .coreWatchlistUri(savedCore.toString())
        .aliasesWatchlistUri(savedAliases.toString())
        .keywordsWatchlistUri(savedKeywords.toString())
        .build());
  }
}

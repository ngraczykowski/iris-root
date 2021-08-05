package com.silenteight.hsbc.bridge.watchlist;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.watchlist.event.OriginalWatchlistSavedEvent;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;

import java.net.URI;

@RequiredArgsConstructor
class WatchlistEventListener {

  private final WatchlistLoader watchlistLoader;
  private final ProcessWatchlistArchiveUseCase processWatchlist;

  @Async
  @EventListener
  public void onOriginalWatchlistSaved(OriginalWatchlistSavedEvent event) {
    var coreUri = URI.create(event.getCoreWatchlistUri());
    var aliasesUri = URI.create(event.getAliasesWatchlistUri());

    processWatchlist.process(RetrievedWatchlist.builder()
        .core(watchlistLoader.loadWatchlist(coreUri))
        .aliases(watchlistLoader.loadWatchlist(aliasesUri))
        .keywordsUri(event.getKeywordsWatchlistUri())
        .build()
    );
  }
}

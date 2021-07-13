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
    var core = watchlistLoader.loadWatchlist(
        URI.create(event.getCoreWatchlistUri()));
    var aliases = watchlistLoader.loadWatchlist(
        URI.create(event.getAliasesWatchlistUri()));

    processWatchlist.process(RetrievedWatchlist.builder()
        .core(core)
        .aliases(aliases)
        .keywordsUri(event.getKeywordsWatchlistUri())
        .build()
    );
  }

}

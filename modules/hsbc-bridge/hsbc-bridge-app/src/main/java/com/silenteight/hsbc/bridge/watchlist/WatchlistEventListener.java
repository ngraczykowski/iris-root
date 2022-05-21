package com.silenteight.hsbc.bridge.watchlist;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.watchlist.event.ZipFileWatchlistSavedEvent;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;

import java.net.URI;

@RequiredArgsConstructor
class WatchlistEventListener {

  private final WatchlistLoader watchlistLoader;
  private final ProcessWatchlistArchiveUseCase processWatchlist;

  @Async
  @EventListener
  public void onZipFileWatchlistSaved(ZipFileWatchlistSavedEvent event) {
    var zipUri = URI.create(event.getZipUri());
    processWatchlist.processZipFile(watchlistLoader.loadWatchlist(zipUri));
  }
}

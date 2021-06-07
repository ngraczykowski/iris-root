package com.silenteight.hsbc.bridge.watchlist;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.file.DownloadResourceUseCase;
import com.silenteight.hsbc.bridge.file.ResourceIdentifier;
import com.silenteight.hsbc.bridge.watchlist.event.OriginalWatchlistSavedEvent;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;

@RequiredArgsConstructor
class WatchlistEventListener {

  private final DownloadResourceUseCase downloadResourceUseCase;
  private final ProcessWatchlistArchiveUseCase processWatchlist;

  @Async
  @EventListener
  public void onOriginalWatchlistSaved(OriginalWatchlistSavedEvent event) {
    var core = downloadResourceUseCase.download(
        ResourceIdentifier.of(event.getCoreWatchlistUri()));
    var aliases = downloadResourceUseCase.download(
        ResourceIdentifier.of(event.getAliasesWatchlistUri()));

    processWatchlist.process(RetrievedWatchlist.builder()
        .core(core)
        .aliases(aliases)
        .keywordsUri(event.getKeywordsWatchlistUri())
        .build()
    );
  }

}

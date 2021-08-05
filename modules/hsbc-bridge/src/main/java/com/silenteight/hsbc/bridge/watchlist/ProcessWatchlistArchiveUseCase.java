package com.silenteight.hsbc.bridge.watchlist;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.bridge.unpacker.FileManager;

import java.io.InputStream;

import static com.silenteight.worldcheck.api.v1.WatchlistType.WORLD_CHECK;
import static com.silenteight.worldcheck.api.v1.WatchlistType.WORLD_CHECK_ALIASES;
import static com.silenteight.worldcheck.api.v1.WatchlistType.WORLD_CHECK_KEYWORDS;
import static java.util.Set.of;

@Slf4j
@RequiredArgsConstructor
class ProcessWatchlistArchiveUseCase {

  private final WatchlistSaver saveFileUseCase;
  private final FileManager fileManager;
  private final WorldCheckNotifier worldCheckNotifier;

  public void process(RetrievedWatchlist retrievedWatchlist) {
    var savedCoreUri = processWatchlist(retrievedWatchlist.getCore());
    var savedAliasesUri = processWatchlist(retrievedWatchlist.getAliases());

    var watchlistCollection = of(
        new WatchlistIdentifier(savedCoreUri, WORLD_CHECK),
        new WatchlistIdentifier(savedAliasesUri, WORLD_CHECK_ALIASES),
        new WatchlistIdentifier(retrievedWatchlist.getKeywordsUri(), WORLD_CHECK_KEYWORDS)
    );

    worldCheckNotifier.notify(watchlistCollection);
  }

  private String processWatchlist(InputStream watchlistStream) {
    var watchlist = fileManager.unzip(watchlistStream);
    var name = watchlist.getName();
    var path = watchlist.getPath();
    try {
      var watchlistUri = saveFileUseCase.saveFile(name, path);
      var watchlistUriPath = watchlistUri.getPath();
      log.info(name + " watchlist successfully saved at minIO on path: " + watchlistUriPath);
      return watchlistUri.toString();
    } finally {
      fileManager.delete(path);
    }
  }
}

package com.silenteight.hsbc.bridge.watchlist;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.bridge.unpacker.FileManager;
import com.silenteight.hsbc.bridge.unpacker.UnzippedObject;
import com.silenteight.worldcheck.api.v1.WatchlistType;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
class ProcessWatchlistArchiveUseCase {

  private final WatchlistSaver saveFileUseCase;
  private final FileManager fileManager;
  private final WorldCheckNotifier worldCheckNotifier;

  private void process(RetrievedWatchlist retrievedWatchlist) {
    var savedCoreUri = processWatchlist(retrievedWatchlist.getCore());
    var savedAliasesUri = processWatchlist(retrievedWatchlist.getAliases());

    var watchlistCollection = Set.of(
        new WatchlistIdentifier(savedCoreUri, WatchlistType.WORLD_CHECK),
        new WatchlistIdentifier(savedAliasesUri, WatchlistType.WORLD_CHECK_ALIASES),
        new WatchlistIdentifier(retrievedWatchlist.getKeywordsUri(), WatchlistType.WORLD_CHECK_KEYWORDS)
    );

    worldCheckNotifier.notify(watchlistCollection);
  }

  public void processZipFile(InputStream zipFile) {
    var unzippedFiles = fileManager.unpackZip(zipFile);

    try {
      var rawWatchlist = getRawWatchlist(unzippedFiles);
      var keywordsUri = saveFileUseCase.saveFile(
          rawWatchlist.getKeywords().getName(), rawWatchlist.getKeywords().getPath());
      log.info(
          "{} watchlist successfully saved at minIO on path: {}",
          rawWatchlist.getKeywords().getName(), keywordsUri.getPath());

      process(RetrievedWatchlist.builder()
          .core(new FileInputStream(rawWatchlist.getCore().getPath()))
          .aliases(new FileInputStream(rawWatchlist.getNameAliases().getPath()))
          .keywordsUri(keywordsUri.toString())
          .build());
    } catch (FileNotFoundException e) {
      throw new WatchlistNotFoundException(e);
    } finally {
      unzippedFiles.forEach(file -> fileManager.delete(file.getPath()));
    }
  }

  private RawWatchlist getRawWatchlist(List<UnzippedObject> unzippedFiles) {
    var core = findWatchlistByName(unzippedFiles, "world-check.xml", ".gz")
        .orElseThrow(() -> new WatchlistNotFoundException(
            "Watchlist core not found! File should contain name 'world-check.xml' and have .gz extension"));
    var nameAliases = findWatchlistByName(unzippedFiles, "native-aliases", ".gz")
        .orElseThrow(() -> new WatchlistNotFoundException(
            "Watchlist aliases not found! File should contain name 'native-aliases' and have .gz extension "));
    var keywords = findWatchlistByName(unzippedFiles, "keywords", ".xml")
        .orElseThrow(() -> new WatchlistNotFoundException(
            "Watchlist keywords not found! File should contain name 'keywords' and have .xml extension"));
    var coreChecksum = findWatchlistByName(unzippedFiles, "world-check-checksum", ".md5")
        .orElseThrow(() -> new WatchlistNotFoundException(
            "Core checksum not found! File should contain name 'world-check-checksum' and have .md5 extension"));
    var nameAliasesChecksum = findWatchlistByName(unzippedFiles, "native-aliases", ".md5")
        .orElseThrow(() -> new WatchlistNotFoundException(
            "Aliases checksum not found! File should contain name 'native-aliases' and have .md5 extension"));

    return RawWatchlist.builder()
        .core(core)
        .nameAliases(nameAliases)
        .keywords(keywords)
        .coreChecksum(coreChecksum)
        .nameAliasesChecksum(nameAliasesChecksum)
        .build();
  }

  private Optional<UnzippedObject> findWatchlistByName(
      List<UnzippedObject> list, String name, String extension) {
    return list.stream()
        .filter(e -> {
          var fileName = e.getName().toLowerCase();
          return fileName.contains(name) && fileName.endsWith(extension);
        }).findFirst();
  }

  private String processWatchlist(InputStream watchlistStream) {
    var watchlist = fileManager.unpackGzip(watchlistStream);
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

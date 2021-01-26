package com.silenteight.agent.common.io;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static java.util.Collections.emptyMap;


// Lists all files in given resources directory
// JAR and filesystem aware
// Based on https://stackoverflow.com/a/28057735/5853996
@RequiredArgsConstructor
@Slf4j
public class ResourceDirectoryFilenamesFinder {

  private static final int MAX_DEPTH = 1;
  private static final String JAR_SCHEME = "jar";
  private final String resourceDirectoryPath;

  public Stream<String> find() throws IOException {
    log.debug("Reading files in {}", resourceDirectoryPath);
    var fileNames = new ArrayList<String>();
    var directoryUri = tryGettingDirectoryUri();

    try (var ignored = newFilesystemIfJar(directoryUri)) {
      var directoryPath = Paths.get(directoryUri);
      forEachFilenameInDirectory(directoryPath, fileNames::add);
    }

    return fileNames.stream();
  }

  private static void forEachFilenameInDirectory(
      Path directoryPath, Consumer<String> consumer) throws IOException {
    try (var walker = Files.walk(directoryPath, MAX_DEPTH)) {
      walker.filter(path -> !path.equals(directoryPath))
          .map(Path::getFileName)
          .map(Path::toString)
          .forEach(consumer);
    }
  }

  private URI tryGettingDirectoryUri() throws IOException {
    try {
      return ResourceDirectoryFilenamesFinder.class.getResource(resourceDirectoryPath).toURI();
    } catch (URISyntaxException e) {
      throw new IOException(e);
    }
  }

  private static FileSystem newFilesystemIfJar(URI directoryUri) throws IOException {
    return directoryUri.getScheme().equals(JAR_SCHEME)
           ? FileSystems.newFileSystem(directoryUri, emptyMap())
           : null;
  }
}

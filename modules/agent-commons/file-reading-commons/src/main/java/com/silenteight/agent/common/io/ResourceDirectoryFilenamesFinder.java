package com.silenteight.agent.common.io;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static java.util.Collections.emptyMap;
import static java.util.Objects.nonNull;


// Lists all files in given resources directory
// JAR and filesystem aware
// Based on https://stackoverflow.com/a/28057735/5853996
@RequiredArgsConstructor
@Slf4j
public class ResourceDirectoryFilenamesFinder {

  private static final int MAX_DEPTH = 1;
  private static final String JAR_SCHEME = "jar";
  private static final String URI_DIR_SEPARATOR = "!";
  private static final String JAR_EXTENSION_WITH_DOT = ".jar";
  private final String resourceDirectoryPath;

  public Stream<String> find() throws IOException {
    log.debug("Reading files in {}", resourceDirectoryPath);
    var fileNames = new ArrayList<String>();
    var directoryUri = tryGettingDirectoryUri(resourceDirectoryPath);

    try (var jarFileSystem = newFilesystemIfJar(directoryUri)) {
      Path directoryPath;
      if (nonNull(jarFileSystem)) {
        directoryPath = jarFileSystem.getPath(resourceDirectoryPath);
      } else {
        directoryPath = Paths.get(directoryUri);
      }
      forEachFilenameInDirectory(directoryPath, fileNames::add);
    }
    return fileNames.stream();
  }

  private static Optional<String> getNestedJarPath(String directoryUriString) {
    var dirs = directoryUriString.split(URI_DIR_SEPARATOR);
    if (hasNestedJar(directoryUriString)) {
      return Optional.of(dirs[1]);
    }
    return Optional.empty();
  }

  private static boolean hasNestedJar(String directoryUriString) {
    var dirs = directoryUriString.split(URI_DIR_SEPARATOR);
    return dirs.length > 2 && dirs[1].contains(JAR_EXTENSION_WITH_DOT);
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

  private static URI tryGettingDirectoryUri(String resourceDirectoryPath) throws IOException {
    try {
      URL directoryUrl = ResourceDirectoryFilenamesFinder.class
          .getResource(resourceDirectoryPath);

      if (directoryUrl != null) {
        return directoryUrl.toURI();
      }

      throw new IOException();
    } catch (URISyntaxException e) {
      throw new IOException(e);
    }
  }

  private static FileSystem newFilesystemIfJar(URI directoryUri) throws IOException {
    FileSystem fileSystem = null;
    if (JAR_SCHEME.equals(directoryUri.getScheme())) {
      fileSystem = getOrCreateFileSystem(directoryUri);
      var nestedJarPath = getNestedJarPath(directoryUri.toString());
      if (nestedJarPath.isPresent()) {
        var path = fileSystem.getPath(nestedJarPath.get());
        fileSystem = FileSystems.newFileSystem(path, (ClassLoader) null);
      }
    }
    return fileSystem;
  }

  private static FileSystem getOrCreateFileSystem(URI directoryUri) throws IOException {
    FileSystem fileSystem;
    try {
      fileSystem = FileSystems.getFileSystem(directoryUri);
    } catch (FileSystemNotFoundException e) {
      // file system does not exist, create a new one
      fileSystem = FileSystems.newFileSystem(directoryUri, emptyMap());
    }
    return fileSystem;
  }
}

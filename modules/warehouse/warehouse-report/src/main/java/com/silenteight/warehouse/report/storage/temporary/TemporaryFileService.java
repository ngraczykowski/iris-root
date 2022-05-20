package com.silenteight.warehouse.report.storage.temporary;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.function.Consumer;

import static java.nio.file.Files.createTempFile;
import static java.nio.file.Files.delete;
import static java.nio.file.Files.newInputStream;
import static java.nio.file.Files.newOutputStream;
import static java.nio.file.StandardOpenOption.APPEND;

@RequiredArgsConstructor
@Slf4j
class TemporaryFileService implements TemporaryFileStorage {

  @Override
  public void doOnTempFile(String name, Consumer<FileStorage> fileConsumer) {
    var tempFile = createFile(name);
    fileConsumer.accept(tempFile);
    tempFile.clear();
  }

  TemporaryFile createFile(String name) {
    return new TemporaryFile(name);
  }

  @RequiredArgsConstructor
  static class TemporaryFileConsumer implements Consumer<Collection<String>> {

    private final File file;

    @Override
    public void accept(Collection<String> lines) {
      try (OutputStream fileOutputStream = newOutputStream(file.toPath(), APPEND)) {
        lines.forEach(line -> writeLine(line, fileOutputStream));
      } catch (IOException e) {
        throw new TemporaryReportNotSavedException("Cannot create stream writer", e);
      }
    }

    private static void writeLine(String line, OutputStream outputStream) {
      try {
        outputStream.write(line.getBytes());
      } catch (IOException e) {
        throw new TemporaryReportNotSavedException("Cannot write report line to writer.", e);
      }
    }
  }

  @RequiredArgsConstructor
  static class TemporaryFile implements FileStorage {

    private static final String CSV = ".csv";

    private final String name;
    private File file;
    private InputStream inputStream;

    @Override
    public TemporaryFileConsumer getConsumer() {
      if (file == null)
        createFile();

      return new TemporaryFileConsumer(file);
    }

    @SneakyThrows
    private void createFile() {
      file = createTempFile(name, CSV).toFile();
      file.deleteOnExit();
      inputStream = newInputStream(file.toPath());
    }

    @SneakyThrows
    void clear() {
      log.debug("Removing temporary file: {}", file.getName());
      delete(file.toPath());
      inputStream.close();
    }

    @Override
    public InputStream getInputStream() {
      return inputStream;
    }
  }
}

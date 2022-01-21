package com.silenteight.agent.common.dictionary;

import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

import static java.nio.charset.StandardCharsets.UTF_8;

@RequiredArgsConstructor
class FileDictionarySource implements DictionarySource {

  private static final Charset CHARSET = UTF_8;

  private final Path path;

  @Override
  public String getIdentifier() {
    return path.toAbsolutePath().toString();
  }

  @Override
  public DictionaryStream stream() {
    return new FileDictionaryStream();
  }

  static class FileReadException extends RuntimeException {

    private static final long serialVersionUID = 6990805382732829186L;

    FileReadException(Path path, IOException e) {
      super("Could not read dictionary file from " + path, e);
    }
  }

  private class FileDictionaryStream implements DictionaryStream {

    private final Stream<String> lines = getStream();

    @Override
    public Stream<String> lines() {
      return lines;
    }

    @Override
    public void close() {
      lines.close();
    }

    private Stream<String> getStream() {
      try {
        return Files.lines(path, CHARSET);
      } catch (IOException e) {
        throw new FileReadException(path, e);
      }
    }
  }
}

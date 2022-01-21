package com.silenteight.agent.common.dictionary;

import lombok.RequiredArgsConstructor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.stream.Stream;

import static java.lang.String.format;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Optional.ofNullable;


@RequiredArgsConstructor
class ResourceDictionarySource implements DictionarySource {

  private static final Charset CHARSET = UTF_8;

  private final String name;
  private final Class<?> clazz;

  @Override
  public String getIdentifier() {
    return format("%s (%s)", name, clazz);
  }

  @Override
  public DictionaryStream stream() {
    return new ResourceDictionaryStream();
  }

  static class ResourceNotFoundException extends RuntimeException {

    private static final long serialVersionUID = -3570407037182301746L;

    ResourceNotFoundException(String name) {
      super("Resource not found: " + name);
    }
  }

  static class ResourceCloseException extends RuntimeException {

    private static final long serialVersionUID = 6990805382732829186L;

    ResourceCloseException(String name, IOException e) {
      super("Could not close resource: " + name, e);
    }
  }

  private class ResourceDictionaryStream implements DictionaryStream {

    private final BufferedReader reader =
        new BufferedReader(new InputStreamReader(getInputStream(), CHARSET));

    private Stream<String> lines;

    @Override
    public Stream<String> lines() {
      if (lines == null) {
        lines = reader.lines();
      }
      return lines;
    }

    @Override
    public void close() {
      try {
        reader.close();
      } catch (IOException e) {
        throw new ResourceCloseException(name, e);
      } finally {
        if (lines != null) {
          lines.close();
        }
      }
    }

    private InputStream getInputStream() {
      return ofNullable(clazz.getResourceAsStream(name))
          .orElseThrow(() -> new ResourceNotFoundException(name));
    }
  }
}

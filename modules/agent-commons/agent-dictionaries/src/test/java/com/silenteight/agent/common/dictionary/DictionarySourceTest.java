package com.silenteight.agent.common.dictionary;

import com.silenteight.agent.common.dictionary.ResourceDictionarySource.ResourceNotFoundException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mockito;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import static com.silenteight.agent.common.dictionary.DictionarySource.*;
import static java.util.List.of;
import static java.util.stream.Collectors.toUnmodifiableList;
import static org.apache.commons.io.FileUtils.writeLines;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class DictionarySourceTest {

  @TempDir
  static Path tempDir;

  @Test
  void shouldCreateSourceFromLines() {
    var lines = of("line1", "line2", "line3");

    var source = fromLines(lines);
    var identifier = source.getIdentifier();

    assertThat(identifier).isEqualTo(lines.toString());
    assertThat(readLines(source)).containsExactlyElementsOf(lines);
  }

  @Test
  void shouldCreateSourceFromResource() {
    var resource = "/com/silenteight/agent/common/dictionary/source/test_source.txt";

    var source = fromResource(getClass(), resource);
    var identifier = source.getIdentifier();

    assertThat(identifier)
        .contains(getClass().toString())
        .contains(resource);
    assertThat(readLines(source)).containsExactly("line1", "line2", "line3");
  }

  @Test
  void shouldCreateSourceFromResourceUsingRelativePath() {
    var resource = "source/test_source.txt";

    var source = fromResource(getClass(), resource);
    var identifier = source.getIdentifier();

    assertThat(identifier)
        .contains(getClass().toString())
        .contains(resource);
    assertThat(readLines(source)).containsExactly("line1", "line2", "line3");
  }

  @Test
  void shouldThrowResourceNotFoundException_whenOpenStreamAndResourceDoesNotExist() {
    var name = "not_existing_resource";

    var source = fromResource(getClass(), name);

    assertThrows(ResourceNotFoundException.class, source::stream);
  }

  @Test
  void shouldCreateSourceFromFile() throws IOException {
    var tempFile = Files.createFile(tempDir.resolve("dictionary1.txt"));
    writeLines(tempFile.toFile(), of("line1", "line2"));

    var source = fromFile(tempFile);
    var identifier = source.getIdentifier();

    assertThat(identifier).isEqualTo(tempFile.toAbsolutePath().toString());
    assertThat(readLines(source)).containsExactly("line1", "line2");
  }

  @Test
  void shouldCreateSourceFromConfigFile() {
    var path = "dictionaries/test.dict";

    var source = fromConfFile(path);
    var identifier = source.getIdentifier();

    assertThat(identifier).endsWith(path);
    assertThat(readLines(source)).containsExactly("key=value1;value2;value3");
  }

  @Test
  void shouldCreateSourceFromManyStreams() {
    var source1 = new TestSource("s1", Stream.of("line1", "line2"));
    var source2 = new TestSource("s2", Stream.of());
    var source3 = new TestSource("s3", Stream.of("line3", "line4"));

    var source = fromMany(source1, source2, source3);
    var identifier = source.getIdentifier();

    assertThat(identifier).isEqualTo("s1,s2,s3");
    assertThat(of(source1, source2, source3)).allMatch(TestSource::hasNotBeenUsed);
    assertThat(readLines(source)).containsExactly("line1", "line2", "line3", "line4");
    assertThat(of(source1, source2, source3)).allMatch(TestSource::isStreamClosed);
  }

  @Test
  void shouldCloseAllUsedSourceStreamsIfExceptionOccurredWhileReading() {
    var source1 = new TestSource("s1", Stream.of("line1", "line2", "line3"));
    var source2 = new TestSource("s2", Stream.of("line4", "line5", "line6").peek(value -> {
          if (value.equals("line5")) {
            throw new RuntimeException("Failed to construct");
          }
        }
    ));
    var source3 = new TestSource("s3", Stream.of("line7", "line8", "line9"));
    var source = fromMany(source1, source2, source3);

    assertThat(of(source1, source2, source3)).allMatch(TestSource::hasNotBeenUsed);
    assertThrows(RuntimeException.class, () -> readLines(source));

    assertThat(source1).matches(TestSource::isStreamClosed);
    assertThat(source1.getReadLinesCounter()).hasValue(3);

    assertThat(source2).matches(TestSource::isStreamClosed);
    assertThat(source2.getReadLinesCounter()).hasValue(1);

    assertThat(source3).matches(TestSource::hasNotBeenUsed);
    assertThat(source3.getReadLinesCounter()).hasValue(0);
  }

  @Test
  void shouldCreateDictionary() {
    var source = new TestSource("id", Stream.of("line1", "line2"));

    var dictionary = source
        .getOrCreate(TestDictionary.class, new TestDictionaryFactory());

    assertThat(dictionary.getLines()).containsExactly("line1", "line2");
  }

  @Test
  void shouldCreateDictionaryOnlyOnceForGivenIdentifier() {
    var factory = Mockito.spy(new TestDictionaryFactory());
    var lines = of("line1", "line2", "line3");
    var source1 = new TestSource("id1", lines.stream());
    var source2 = new TestSource("id2", lines.stream());

    source1.getOrCreate(TestDictionary.class, factory);
    source1.getOrCreate(TestDictionary.class, factory);

    verify(factory, times(1)).apply(any());

    source2.getOrCreate(TestDictionary.class, factory);
    source2.getOrCreate(TestDictionary.class, factory);

    verify(factory, times(2)).apply(any());
  }

  @Test
  void shouldCloseSourceStreamEvenIfNotUsedInFactory() {
    var source = new TestSource("test", Stream.of("line1", "line2"));
    Function<Stream<String>, TestDictionary> failingFactory = s -> {
      throw new RuntimeException("Failed to construct");
    };

    assertThrows(RuntimeException.class, () -> source
        .getOrCreate(TestDictionary.class, failingFactory));
    assertThat(source).matches(TestSource::isStreamClosed);
  }

  private List<String> readLines(DictionarySource source) {
    try (var stream = source.stream()) {
      return stream.lines().collect(toUnmodifiableList());
    }
  }
}

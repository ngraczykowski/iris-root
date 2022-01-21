package com.silenteight.agent.common.dictionary;

import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toUnmodifiableList;

/**
 * Provides stream of dictionary text lines and the unique identifier of the source. Allows creating
 * dictionary instance once per identifier and dictionary class.
 */
public interface DictionarySource {

  /**
   * @return Identifier used by {@link #getOrCreate} to determine if existing dictionary should be
   *     reused or should create a new one from the factory.
   */
  String getIdentifier();

  /**
   * @return DictionaryStream of text lines.
   *
   * @apiNote Remember to close stream with {@link DictionaryStream#close()}. Hint: use with
   *     try-with-resources block.
   */
  //todo Before moving method to agent commons,
  // it should have protected access to not forward unclosed stream outside
  DictionaryStream stream();

  /**
   * Creates DictionarySource from collection of lines.
   */
  static DictionarySource fromLines(Collection<String> lines) {
    return new LineDictionarySource(lines);
  }

  /**
   * Creates DictionarySource from file. Lines will be loaded lazily.
   */
  static DictionarySource fromFile(Path path) {
    return new FileDictionarySource(path);
  }

  /**
   * Creates DictionarySource from resource. Lines will be loaded lazily. Resource name may be
   * absolute or relative to the clazz package, e.g. for "{package_name}/source.txt" you can pass
   * clazz={package_name}.SomeClass.class and name=source.txt.
   */
  static DictionarySource fromResource(Class<?> clazz, String name) {
    return new ResourceDictionarySource(name, clazz);
  }

  /**
   * Creates DictionarySource from config file: {AGENT_HOME}/conf/{path}. Lines will be loaded
   * lazily.
   */
  static DictionarySource fromConfFile(String path) {
    return new ConfigFileDictionarySource(path);
  }

  /**
   * See: {@link #fromMany(Collection)}
   */
  static DictionarySource fromMany(DictionarySource... sources) {
    return new MultiDictionarySource(List.of(sources));
  }

  /**
   * See: {@link #fromMany(Collection)}
   */
  static DictionarySource fromMany(Stream<? extends DictionarySource> sources) {
    return new MultiDictionarySource(sources.collect(toUnmodifiableList()));
  }

  /**
   * Creates DictionarySource from many {@link DictionarySource} instances. Lines will be loaded
   * lazily.
   */
  static DictionarySource fromMany(Collection<? extends DictionarySource> sources) {
    return new MultiDictionarySource(sources);
  }

  /**
   * Creates {@link Dictionary} instance only once per source identifier and dictionary class.
   */
  default <TargetT extends Dictionary> TargetT getOrCreate(
      Class<TargetT> clazz, Function<Stream<String>, TargetT> factory) {

    var provider = DictionaryInstanceProvider.<TargetT>builder()
        .identifier(getIdentifier())
        .clazz(clazz)
        .factory(() -> createDictionary(factory))
        .build();

    return provider.getOrCreate();
  }

  private <TargetT extends Dictionary> TargetT createDictionary(
      Function<Stream<String>, TargetT> factory) {

    try (var stream = stream()) {
      return factory.apply(stream.lines());
    }
  }
}

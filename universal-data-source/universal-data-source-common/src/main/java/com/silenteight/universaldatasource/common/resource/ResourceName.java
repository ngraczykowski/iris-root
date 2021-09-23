package com.silenteight.universaldatasource.common.resource;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;

import java.util.LinkedHashMap;
import java.util.Map;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public class ResourceName {

  private static final String DELIMITER = "/";
  private final Map<String, String> pathTokens;

  public static ResourceName create(@NonNull String path) {
    return new ResourceName(tokenize(path));
  }

  private static Map<String, String> tokenize(String path) {
    Map<String, String> tokenized = new LinkedHashMap<>();
    var tokens = Splitter.on(DELIMITER).trimResults().split(path).iterator();
    while (tokens.hasNext()) {
      var name = tokens.next();
      if (tokens.hasNext()) {
        tokenized.put(name, tokens.next());
      }
    }
    return tokenized;
  }

  public String getPath() {
    return Joiner.on(DELIMITER).withKeyValueSeparator(DELIMITER).join(pathTokens);
  }

  @Override
  public String toString() {
    return getPath();
  }

  public ResourceName remove(@NonNull String name) {
    pathTokens.remove(name);
    return this;
  }

  public boolean contains(String name) {
    return pathTokens.containsKey(name);
  }

  public ResourceName replaceLong(@NonNull String name, long value) {
    pathTokens.replace(name, Long.toString(value));
    return this;
  }

  public ResourceName replaceName(@NonNull String name, String withName, String value) {
    Map<String, String> tokens = new LinkedHashMap<>();
    pathTokens.forEach((prevName, prevValue) -> {
      if (prevName.equals(name)) {
        tokens.put(withName, value);
      } else {
        tokens.put(prevName, prevValue);
      }
    });
    return new ResourceName(tokens);
  }

  public ResourceName add(@NonNull String name, String value) {
    pathTokens.put(name, value);
    return this;
  }

  public String get(@NonNull String name) {
    if (pathTokens.containsKey(name))
      return pathTokens.get(name);
    throw partNotExists(name);
  }

  public long getLong(@NonNull String name) {
    if (pathTokens.containsKey(name)) {
      return Long.parseLong(pathTokens.get(name));
    }
    throw partNotExists(name);
  }

  public ResourceName copy() {
    return new ResourceName(new LinkedHashMap<>(pathTokens));
  }

  private static InvalidResourceNameException partNotExists(String name) {
    return new InvalidResourceNameException("Resource name has no part '" + name + "'.");
  }
}

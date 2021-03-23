package com.silenteight.adjudication.engine.common.resource;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;

import java.util.LinkedHashMap;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public class ResourceName {

  private static final String NAME_SPLITTER = "/";
  private Map<String, Long> pathTokens;

  public static final ResourceName getResource(@NonNull String path) {
    return new ResourceName(tokenize(path));
  }

  private static Map<String, Long> tokenize(String path) {
    Map<String, Long> tokenized = new LinkedHashMap<>();
    var tokens = Splitter.on(NAME_SPLITTER).trimResults().splitToList(path);
    for (int i = 0; i < tokens.size(); i = i + 2) {
      tokenized.put(tokens.get(i), Long.parseLong(tokens.get(i + 1)));
    }
    return tokenized;
  }

  public String getPath() {
    return Joiner.on(NAME_SPLITTER).withKeyValueSeparator(NAME_SPLITTER).join(pathTokens);
  }

  public ResourceName remove(@NonNull String name) {
    pathTokens.remove(name);
    return this;
  }

  public ResourceName replaceId(@NonNull String name, long value) {
    pathTokens.replace(name, value);
    return this;
  }

  public ResourceName replaceName(@NonNull String name, String withName, long value) {
    Map<String, Long> tokens = new LinkedHashMap<>();
    pathTokens.keySet().forEach(s -> {
      if (s.equals(name)) {
        tokens.put(withName, value);
      } else {
        tokens.put(s, pathTokens.get(s));
      }
    });
    return new ResourceName(tokens);
  }

  public ResourceName add(@NonNull String name, long value) {
    pathTokens.put(name, value);
    return this;
  }

  public long getId(@NonNull String name) {
    if (pathTokens.containsKey(name)) {
      return pathTokens.get(name);
    }
    throw new RuntimeException("No such token");
  }

  public ResourceName copy() {
    return new ResourceName(new LinkedHashMap<>(pathTokens));
  }
}

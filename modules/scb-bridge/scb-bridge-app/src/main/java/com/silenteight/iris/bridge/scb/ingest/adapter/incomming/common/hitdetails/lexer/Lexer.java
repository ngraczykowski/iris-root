/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.hitdetails.lexer;

import lombok.NonNull;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;

public class Lexer {

  private static final Pattern DIVIDER_PATTERN =
      compile("\\s*=+\\s*");
  private static final Pattern KEY_1_PATTERN =
      compile("^\\s*(?<key>[A-Za-z0-9]+):\\s*$");
  private static final Pattern KEY_2_PATTERN =
      compile("^\\s*(?<key>[A-Z\\s\\-0-9]+):\\s*$");
  private static final Pattern KEY_VALUE_1_PATTERN =
      compile("^\\s*(?<key>[A-Za-z0-9]+):\\s*(?<value>[^\\s][^:]*)$");
  private static final Pattern KEY_VALUE_2_PATTERN =
      compile("^\\s*(?<key>[A-Z\\s\\-0-9]+):\\s*(?<value>[^\\s][^:]*)$");
  private static final Pattern LIST_VALUE_PATTERN =
      compile("^\\s*((?<active>\\*)|(?<normal>-))\\s+(?<value>[^\\s][^:]*)$");
  private static final Pattern TEXT_LINE_PATTERN =
      compile("\\s*(?<text>[^\\s].*)");
  private static final Pattern EMPTY_LINE_PATTERN =
      compile("^\\s*$");
  private static final Pattern NEW_LINE_PATTERN = Pattern.compile("\n");

  private final List<Predicate<String>> elementFinders;
  private final LexerEventListener listener;

  private int lineIndex;

  public Lexer(LexerEventListener listener) {
    this.listener = listener;
    this.elementFinders = Arrays.asList(
        this::findDivider,
        this::findKeyValue1,
        this::findKeyValue2,
        this::findKey1,
        this::findKey2,
        this::findListValue,
        this::findTextLine,
        this::findEmptyLine
    );
  }

  public void lex(@NonNull String text) {
    lineIndex = 0;
    listener.onStart();
    for (String line : NEW_LINE_PATTERN.split(text)) {
      lexLine(line);
      lineIndex++;
    }
    listener.onFinish();
  }

  private void lexLine(String line) {
    if (!findElement(line)) {
      listener.onError(lineIndex, 0, line);
    }
  }

  private boolean findElement(String line) {
    return elementFinders.stream().anyMatch(f -> f.test(line));
  }

  private boolean findDivider(String line) {
    if (DIVIDER_PATTERN.matcher(line).matches()) {
      listener.onSectionDivider(lineIndex, 0);
      return true;
    }
    return false;
  }

  private boolean findKey1(String line) {
    return findKey(line, KEY_1_PATTERN);
  }

  private boolean findKey2(String line) {
    return findKey(line, KEY_2_PATTERN);
  }

  private boolean findKeyValue1(String line) {
    return findKeyValue(line, KEY_VALUE_1_PATTERN);
  }

  private boolean findKeyValue2(String line) {
    return findKeyValue(line, KEY_VALUE_2_PATTERN);
  }

  private boolean findListValue(String line) {
    Matcher matcher = LIST_VALUE_PATTERN.matcher(line);
    if (matcher.find()) {
      if (matcher.group("active") != null)
        listener.onActiveListPrefix(lineIndex, matcher.start("active"));
      else if (matcher.group("normal") != null)
        listener.onListPrefix(lineIndex, matcher.start("normal"));
      else
        throw new IllegalStateException();

      consumeMatcherTextValue(matcher);
      return true;
    }
    return false;
  }

  private boolean findTextLine(String line) {
    Matcher matcher = TEXT_LINE_PATTERN.matcher(line);
    if (matcher.find()) {
      listener.onText(lineIndex, matcher.start("text"), matcher.group("text").trim());
      return true;
    }
    return false;
  }

  private boolean findEmptyLine(String line) {
    if (EMPTY_LINE_PATTERN.matcher(line).matches()) {
      listener.onEmptyLine();
      return true;
    }
    return false;
  }

  private boolean findKey(String line, Pattern pattern) {
    Matcher matcher = pattern.matcher(line);
    if (matcher.find()) {
      listener.onKey(lineIndex, matcher.start("key"), matcher.group("key"));
      return true;
    }
    return false;
  }

  private boolean findKeyValue(String line, Pattern pattern) {
    Matcher matcher = pattern.matcher(line);
    if (matcher.find()) {
      listener.onKey(lineIndex, matcher.start("key"), matcher.group("key"));
      consumeMatcherTextValue(matcher);
      return true;
    }
    return false;
  }

  private void consumeMatcherTextValue(Matcher matcher) {
    String group = "value";
    Optional.ofNullable(matcher.group(group))
        .map(String::trim)
        .ifPresent(v -> listener.onText(lineIndex, matcher.start(group), v));
  }
}

package com.silenteight.payments.bridge.etl.processing.model;

import lombok.NonNull;

import com.silenteight.payments.bridge.svb.oldetl.model.UnsupportedMessageException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import javax.annotation.Nonnull;

import static java.util.stream.Collectors.toList;

public class MessageData {

  private final List<MessageTag> tags;

  public MessageData(@NonNull List<MessageTag> tags) {
    this.tags = tags;
  }

  public int getSize() {
    return tags.size();
  }

  @Nonnull
  public Stream<MessageTag> findAll(@NonNull String tagName) {
    return tags.stream().filter(tag -> tagName.equals(tag.getName()));
  }

  @Nonnull
  public Stream<String> findAllValues(String tagName) {
    return findAll(tagName).map(MessageTag::getValue);
  }

  public List<String> getAllMatchingTagValues(String tag, String matchingText) {
    return findAllValues(tag).filter(s -> s.contains(matchingText)).collect(toList());
  }

  @Nonnull
  public Optional<MessageTag> findFirst(@NonNull String tagName) {
    return tags.stream().filter(tag -> tagName.equals(tag.getName())).findFirst();
  }

  @Nonnull
  private MessageTag get(String tagName) {
    return findFirst(tagName)
        .orElseThrow(() -> new UnsupportedMessageException("Tag not found: " + tagName));
  }

  @Nonnull
  public Optional<String> findFirstValue(String tagName) {
    return findFirst(tagName).map(MessageTag::getValue);
  }

  @Nonnull
  public String getValue(String tagName) {
    return get(tagName).getValue();
  }

  @Nonnull
  public List<String> getLines(String tagName) {
    return get(tagName).getLines();
  }

  public int getTagLineCount(String tagName) {
    return getLines(tagName).size();
  }
}

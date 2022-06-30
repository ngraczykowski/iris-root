package com.silenteight.agent.common.metrics.micrometer;

import lombok.NoArgsConstructor;

import com.silenteight.agent.common.metrics.MetricTag;

import io.micrometer.core.instrument.Tag;

import java.util.List;

import static java.util.stream.Collectors.toUnmodifiableList;
import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
class TagsMapper {

  static List<Tag> map(List<MetricTag> tags) {
    return tags
        .stream()
        .map(t -> Tag.of(t.getName(), t.getValue()))
        .collect(toUnmodifiableList());
  }
}

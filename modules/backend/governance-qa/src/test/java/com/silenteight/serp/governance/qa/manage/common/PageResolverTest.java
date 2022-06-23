package com.silenteight.serp.governance.qa.manage.common;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static com.silenteight.serp.governance.qa.manage.common.PageResolver.getNextItem;
import static java.util.Collections.emptyList;
import static java.util.List.of;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class PageResolverTest {

  @ParameterizedTest
  @MethodSource("provideTimestampableForNull")
  void shouldReturnNullHeaderNextItem(
      List<Tokenable> timestampables,
      Integer pageSize,
      Integer expectedNextItem) {

    assertThat(getNextItem(timestampables, pageSize)).isEqualTo(expectedNextItem);
  }

  private static Stream<Arguments> provideTimestampableForNull() {
    return Stream.of(
        Arguments.of(emptyList(), 5, null),
        Arguments.of(emptyList(), 0, null),
        Arguments.of(of(new GenericTimestampableFixture()), 2, null)
    );
  }

  @Test
  void shouldReturnLastAddedAt() {
    List<Tokenable> timestampables = of(new GenericTimestampableFixture(),
        new GenericTimestampableFixture());
    Tokenable lastItem = timestampables.get(timestampables.size() - 1);
    assertThat(getNextItem(timestampables, 1)).isEqualTo(lastItem.getToken());
  }
}

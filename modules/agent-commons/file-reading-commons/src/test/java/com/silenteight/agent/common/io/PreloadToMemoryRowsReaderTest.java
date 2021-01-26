package com.silenteight.agent.common.io;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class PreloadToMemoryRowsReaderTest {

  @Mock
  private RowsReader<String> mockReader;

  @Test
  void consumesIteratorOnConstruction() {
    var iterator = List.of("a", "b", "c").iterator();
    given(mockReader.iterator()).willReturn(iterator);

    new PreloadToMemoryRowsReader<>(mockReader);

    assertThat(iterator.hasNext()).isFalse();
  }

  @Test
  void doesNotUseChildrenReader_whenUsed() {
    var iterator = List.of("a", "b", "c").iterator();
    given(mockReader.iterator()).willReturn(iterator);

    var preloaded = new PreloadToMemoryRowsReader<>(mockReader);

    //noinspection ResultOfMethodCallIgnored
    preloaded.stream().collect(toList());
    then(mockReader).should().iterator();
    then(mockReader).shouldHaveNoMoreInteractions();
  }

  @Test
  void canReadSameDataAsFromChildrenReader() {
    var childrenData = List.of("a", "b", "c");
    given(mockReader.iterator()).willReturn(childrenData.iterator());

    var preloaded = new PreloadToMemoryRowsReader<>(mockReader);
    var actualData = preloaded.stream().collect(toList());

    assertThat(actualData).isEqualTo(childrenData);
  }
}

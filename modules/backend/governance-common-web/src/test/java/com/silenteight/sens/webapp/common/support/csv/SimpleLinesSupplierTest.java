package com.silenteight.sens.webapp.common.support.csv;

import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.*;

class SimpleLinesSupplierTest {

  @Test
  public void fullCsvWhenNoCellAdded() {
    // when
    Stream<String> lines = new SimpleLinesSupplier(asList("row1", "row2")).lines();

    // then
    assertThat(lines).containsExactly("row1", "row2");
  }
}

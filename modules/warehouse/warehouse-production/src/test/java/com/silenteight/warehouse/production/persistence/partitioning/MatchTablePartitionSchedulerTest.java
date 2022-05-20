package com.silenteight.warehouse.production.persistence.partitioning;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MatchTablePartitionSchedulerTest {

  NamedParameterJdbcTemplate jdbcTemplate = Mockito.mock(NamedParameterJdbcTemplate.class);

  MatchTablePartitionScheduler underTest;

  ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);

  static Stream<Arguments> getSamplePartitions() {
    return Stream.of(
        Arguments.of("FOR VALUES FROM ('1') TO ('1500000')", 1500000),
        Arguments.of("FOR VALUES FROM ('1500001') TO ('3000000')", 3000000),
        Arguments.of("FOR VALUES FROM ('3000001') TO ('4500000')", 4500000),
        Arguments.of("FOR VALUES FROM ('4500001') TO ('5000000')", 5000000),
        Arguments.of("FOR VALUES FROM ('5000001') TO ('6500000')", 6500000),
        Arguments.of("FOR VALUES FROM ('6500001') TO ('8000000')", 8000000));
  }

  static Stream<Arguments> getBrokenPartitions() {
    return Stream.of(
        Arguments.of("FOR VALUES FROM ('sd') TO ('asdd')"),
        Arguments.of("FOR VALUES FROM ('1500001') TO '3000000')"),
        Arguments.of("FOR VALUES FROM ('3000001) TO ('4500000')"),
        Arguments.of("FOR VALUES FROM ('2022-02-01') TO ('2022-02-31')"),
        Arguments.of("FOR FROM ('5000001') TO ('6500000')"),
        Arguments.of("FOR VALUES IN ('analysis/20')"));
  }

  @BeforeEach
  public void setUp() {
    underTest = new MatchTablePartitionScheduler(jdbcTemplate, 1500000);
  }

  @ParameterizedTest
  @MethodSource("getSamplePartitions")
  public void shouldExtractPartitionRange(String partName, Integer value) {
    assertThat(underTest.extractMaxRange(partName)).isEqualTo(value);
  }

  @ParameterizedTest
  @MethodSource("getBrokenPartitions")
  public void shouldThrowExceptionForIncorrectRange(String partName) {
    assertThatThrownBy(() -> underTest.extractMaxRange(partName))
        .isInstanceOf(IllegalStateException.class);
  }

  @Test
  public void shouldReturnCorrectMaxValueOrZero() {
    when(jdbcTemplate.queryForObject(anyString(),
        anyMap(), eq(Integer.class))).thenReturn(1500000);
    assertThat(underTest.getMaxValueForTable()).isEqualTo(1500000);

    when(jdbcTemplate.queryForObject(anyString(),
        anyMap(), eq(Integer.class))).thenReturn(null);
    assertThat(underTest.getMaxValueForTable()).isEqualTo(0);
  }

  @Test
  public void shouldCorrectlyCreateInitialPartition() {
    underTest.createPartition(0, 1500000);

    verify(jdbcTemplate).execute(argumentCaptor.capture(), any());

    assertThat(argumentCaptor.getValue())
        .isEqualTo("CREATE TABLE warehouse_match_1500000 PARTITION "
            + "OF warehouse_match FOR VALUES FROM (1) TO (1500000)");
  }

  @Test
  public void shouldCorrectlyCreateNextPartitions() {
    underTest.createPartition(1500000, 1500000);

    verify(jdbcTemplate).execute(argumentCaptor.capture(), any());

    assertThat(argumentCaptor.getValue())
        .isEqualTo("CREATE TABLE warehouse_match_3000000 PARTITION "
            + "OF warehouse_match FOR VALUES FROM (1500001) TO (3000000)");
  }
}
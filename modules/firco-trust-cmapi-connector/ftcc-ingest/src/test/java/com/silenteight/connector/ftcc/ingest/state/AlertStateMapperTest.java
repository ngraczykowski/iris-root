package com.silenteight.connector.ftcc.ingest.state;

import com.silenteight.proto.fab.api.v1.AlertMessageStored.State;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static com.silenteight.proto.fab.api.v1.AlertMessageStored.State.NEW;
import static com.silenteight.proto.fab.api.v1.AlertMessageStored.State.SOLVED_FALSE_POSITIVE;
import static com.silenteight.proto.fab.api.v1.AlertMessageStored.State.SOLVED_TRUE_POSITIVE;
import static com.silenteight.proto.fab.api.v1.AlertMessageStored.State.STATE_UNSPECIFIED;
import static org.assertj.core.api.Assertions.*;

class AlertStateMapperTest {

  private static final String PENDING_STATUS = "PENDING";
  private static final String FALSE_POSITIVE_STATUS = "FALSE_POSITIVE";
  private static final String TRUE_POSITIVE_STATUS = "TRUE_POSITIVE";

  private AlertStateMapper underTest;

  @BeforeEach
  void setUp() {
    underTest = new AlertStateMapper(
        Map.of(
            PENDING_STATUS, NEW,
            FALSE_POSITIVE_STATUS, SOLVED_FALSE_POSITIVE,
            TRUE_POSITIVE_STATUS, SOLVED_TRUE_POSITIVE));
  }

  @Test
  void shouldMapToSpecificState() {
    // when
    State state = underTest.map(PENDING_STATUS);

    // then
    assertThat(state).isEqualTo(NEW);
  }

  @Test
  void shouldMapToUnspecifiedState() {
    // when
    State state = underTest.map("UNKNOWN_STATE");

    // then
    assertThat(state).isEqualTo(STATE_UNSPECIFIED);
  }
}

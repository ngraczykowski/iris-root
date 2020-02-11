package com.silenteight.sens.webapp.user.sync.analyst;

import com.silenteight.sens.webapp.user.sync.analyst.dto.InternalAnalyst;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.silenteight.sens.webapp.user.sync.analyst.AnalystFixtures.INTERNAL_ANALYST_JOHN_SMITH;
import static com.silenteight.sens.webapp.user.sync.analyst.AnalystFixtures.INTERNAL_ANALYST_ROBERT_DOE;
import static org.assertj.core.api.Assertions.*;

class AnalystQueryTest {

  private InMemoryAnalystRepository repository;
  private AnalystQuery underTest;

  @BeforeEach
  void setUp() {
    repository = new InMemoryAnalystRepository();
    underTest = new AnalystQuery(repository);
  }

  @Test
  void emptyAnalystsListWhenAnalystsNotAvailable() {
    // when
    List<InternalAnalyst> analysts = underTest.list();

    // then
    assertThat(analysts).isEmpty();
  }

  @Test
  void analystsListWhenAnalystsAvailable() {
    // given
    repository.save(INTERNAL_ANALYST_JOHN_SMITH);
    repository.save(INTERNAL_ANALYST_ROBERT_DOE);

    // when
    List<InternalAnalyst> analysts = underTest.list();

    // then
    assertThat(analysts).containsExactlyInAnyOrder(INTERNAL_ANALYST_JOHN_SMITH,
        INTERNAL_ANALYST_ROBERT_DOE);
  }
}

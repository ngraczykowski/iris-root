package com.silenteight.sens.webapp.aspects.logging;

import com.silenteight.sens.webapp.aspects.logging.LogContext.LogContextAction;

import org.assertj.core.data.MapEntry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;

import java.util.Map;

import static org.assertj.core.api.Assertions.*;

class LogContextAspectTest {

  private static final String KEY = "key";
  private static final String VALUE = "value";
  private static final MapEntry<String, String> KEY_VALUE = entry(KEY, VALUE);

  private static final String ANOTHER_KEY = "another.key";
  private static final String ANOTHER_VALUE = "another value";
  private static final MapEntry<String, String> ANOTHER_KEY_VALUE =
      entry(ANOTHER_KEY, ANOTHER_VALUE);

  @BeforeEach
  void setUp() {
    MDC.clear();
  }

  @Test
  void testNoContext() {
    assertContextEmpty();
  }

  @Test
  void testSingleEntryInContext() {
    MDC.put(KEY, VALUE);
    assertContext(KEY_VALUE);
  }

  @Test
  void testClearing() {
    MDC.put(KEY, VALUE);
    methodClearingContext();
    assertContextEmpty();
  }

  @LogContext(LogContextAction.CLEAR)
  private void methodClearingContext() {
    // Intentionally left empty.
  }

  @Test
  void testClearingAndPreserving() {
    MDC.put(KEY, VALUE);
    methodAssertingContextEmpty();
    assertContext(KEY_VALUE);
  }

  @LogContext(LogContextAction.CLEAR_PRESERVE)
  private void methodAssertingContextEmpty() {
    assertContextEmpty();
  }

  @Test
  void testPreserving() {
    MDC.put(KEY, VALUE);
    methodExtendingContext();
    assertContext(KEY_VALUE);
  }

  @LogContext(LogContextAction.PRESERVE)
  private void methodExtendingContext() {
    MDC.put(ANOTHER_KEY, ANOTHER_VALUE);
  }

  @Test
  void testPreservingClearContext() {
    methodExtendingContext();
    assertContextEmpty();
  }

  @Test
  void testClearPreserveMerging() {
    MDC.put(KEY, VALUE);
    methodExtendingClearThenMergingContext();
    assertContext(KEY_VALUE, ANOTHER_KEY_VALUE);
  }

  @LogContext(LogContextAction.CLEAR_PRESERVE_MERGE)
  private void methodExtendingClearThenMergingContext() {
    assertContextEmpty();
    MDC.put(ANOTHER_KEY, ANOTHER_VALUE);
  }

  @Test
  void testMerging() {
    MDC.put(KEY, VALUE);
    methodExtendingAndMergingContext();
    assertContext(KEY_VALUE, ANOTHER_KEY_VALUE);
  }

  @LogContext(LogContextAction.PRESERVE_MERGE)
  private void methodExtendingAndMergingContext() {
    MDC.put(ANOTHER_KEY, ANOTHER_VALUE);
  }

  @Test
  void testMergingToClearContext() {
    methodExtendingAndMergingContext();
    assertContext(ANOTHER_KEY_VALUE);
  }

  private static void assertContextEmpty() {
    assertThat(MDC.getCopyOfContextMap()).isNullOrEmpty();
  }

  @SafeVarargs
  private static void assertContext(Map.Entry<String, String>... expected) {
    assertThat(MDC.getCopyOfContextMap()).containsOnly(expected);
  }
}

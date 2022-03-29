package com.silenteight.agents.logging;

import lombok.experimental.UtilityClass;

import org.slf4j.Logger;

import java.util.function.Supplier;
import java.util.stream.Stream;

import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;
import static org.apache.commons.lang3.exception.ExceptionUtils.getRootCauseMessage;

@UtilityClass
@SuppressWarnings("unused")
public class AgentLogger {

  /**
   * <p>Logs exception message without printing stacktrace</p>
   * <p>Intended use: recoverable errors</p>
   */
  public static void warn(Logger log, String message, Throwable error) {
    if (log.isWarnEnabled()) {
      var causeMessage = defaultIfNull(getRootCauseMessage(error), "unknown cause message");
      log.warn(message, causeMessage);
    }
  }

  /**
   * <p> Logs message</p>
   * <pre>
   *  Intended use:
   *  - possible invalid state of application,
   *  - unrecognized formats of data
   *  etc
   * </pre>
   *
   * @param parameters
   *     Parameters values coming from computational expensive methods (toString() etc)
   */
  @SafeVarargs
  public static void warn(Logger log, String message, Supplier<Object>... parameters) {
    if (log.isWarnEnabled()) {
      log.warn(message, unpackSuppliers(parameters));
    }
  }

  /**
   * <p>Logs info message</p>
   * <pre>Intended use:
   * - Purely informative information, e.g. processing steps and results
   * </pre>
   *
   * @param parameters
   *     Parameters values coming from computational expensive methods (toString() etc)
   */
  @SafeVarargs
  public static void info(Logger log, String message, Supplier<Object>... parameters) {
    if (log.isInfoEnabled()) {
      log.info(message, unpackSuppliers(parameters));
    }
  }

  /**
   * <p>Logs debug message</p>
   * <pre>Intended use:
   * - Guide developer through the processing path.
   * - Log most important branching points or selection of algorithms
   * </pre>
   *
   * @param parameters
   *     Parameters values coming from computational expensive methods (toString() etc)
   */
  @SafeVarargs
  public static void debug(Logger log, String message, Supplier<Object>... parameters) {
    if (log.isDebugEnabled()) {
      log.debug(message, unpackSuppliers(parameters));
    }
  }

  /**
   * <p>Logs trace message</p>
   * <pre>Intended use:
   * - Log input and output of public methods
   * - Any information that might be relevant to understanding flow of the processing
   * that were not covered by DEBUG level
   * </pre>
   *
   * @param parameters
   *     Parameters values coming from computational expensive methods (toString() etc)
   */
  @SafeVarargs
  public static void trace(Logger log, String message, Supplier<Object>... parameters) {
    if (log.isTraceEnabled()) {
      log.trace(message, unpackSuppliers(parameters));
    }
  }

  @SafeVarargs
  private static Object[] unpackSuppliers(Supplier<Object>... parameters) {
    return Stream.of(parameters).map(Supplier::get).toArray();
  }
}

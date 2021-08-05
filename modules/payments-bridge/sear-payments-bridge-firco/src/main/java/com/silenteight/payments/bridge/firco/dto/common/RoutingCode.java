package com.silenteight.payments.bridge.firco.dto.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Identifies the behavior associated to the decision made on the message.
 */
@RequiredArgsConstructor
@Getter
public enum RoutingCode {
  /**
   * For times where the routing code input value is not on this list.
   */
  ROUTING_CODE_INVALID(""),
  /**
   * For when an operator opens the message. Means nothing to routing.
   */
  UNKNOWN("-1"),
  NO_HIT("0"),
  HIT("1"),
  PASSED("2"),
  FAILED("3"),
  FILTER_IN_BYPASS_MODE("4"),
  /**
   * Requester sends the message back to the requesting application with a request to be
   * re-filtered. Requester does not by itself re-filter the message.
   */
  RECHECK("5"),
  NOT_REVIEWED("6"),
  NON_BLOCKING("7"),
  NON_CHECKING("8"),
  /**
   * Cancel is like a failed but not due to a hit (i.e., the message life span has expired).
   */
  CANCEL("9"),
  UNQUEUE("10"),
  CLOSE("11"),
  PENDING("12"),
  ERROR("100");

  private final String code;

  public static RoutingCode fromValue(String routingCodeValue) {
    RoutingCode[] values = RoutingCode.values();

    for (int i = 1, valuesLength = values.length; i < valuesLength; i++) {
      if (routingCodeValue.equals(values[i].getCode())) {
        return values[i];
      }
    }

    return ROUTING_CODE_INVALID;
  }
}

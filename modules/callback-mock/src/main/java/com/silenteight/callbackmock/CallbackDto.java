/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.callbackmock;

import lombok.Builder;
import lombok.Value;

import java.util.Map;

@Value
@Builder
public class CallbackDto {

  Map<String, String> headers;
  String body;
}

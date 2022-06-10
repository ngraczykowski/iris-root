/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package utils.datageneration.callback;

import lombok.Value;

import java.util.Map;

@Value
public class CallbackDto {

  Map<String, String> headers;
  String body;
}

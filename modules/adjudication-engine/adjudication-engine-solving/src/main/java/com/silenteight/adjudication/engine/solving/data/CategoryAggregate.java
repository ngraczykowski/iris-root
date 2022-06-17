/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.adjudication.engine.solving.data;

import java.io.Serializable;

public record CategoryAggregate(String categoryName,
                                String categoryValue) implements Serializable {
}

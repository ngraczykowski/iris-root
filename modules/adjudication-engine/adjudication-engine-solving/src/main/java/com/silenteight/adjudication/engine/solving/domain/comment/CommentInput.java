/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.adjudication.engine.solving.domain.comment;

import java.io.Serializable;

public record CommentInput(long alertId, String value) implements Serializable {

}

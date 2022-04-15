package com.silenteight.sens.webapp.grpc;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.google.rpc.Code;
import com.google.rpc.Status;
import io.grpc.StatusRuntimeException;

import static io.grpc.protobuf.StatusProto.toStatusRuntimeException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class GrpcFixtures {

  static final Status NOT_FOUND_STATUS =
      Status.newBuilder().setCode(Code.NOT_FOUND_VALUE).build();
  public static final StatusRuntimeException NOT_FOUND_RUNTIME_EXCEPTION =
      toStatusRuntimeException(NOT_FOUND_STATUS);

  static final Status INTERNAL_STATUS =
      Status.newBuilder().setCode(Code.INTERNAL_VALUE).build();
  public static final StatusRuntimeException OTHER_STATUS_RUNTIME_EXCEPTION =
      toStatusRuntimeException(INTERNAL_STATUS);
}

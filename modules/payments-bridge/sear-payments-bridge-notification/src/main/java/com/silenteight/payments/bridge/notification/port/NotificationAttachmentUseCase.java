package com.silenteight.payments.bridge.notification.port;

import java.util.List;

public interface NotificationAttachmentUseCase {

  byte[] mergeCsvAttachments(List<byte[]> files);
}

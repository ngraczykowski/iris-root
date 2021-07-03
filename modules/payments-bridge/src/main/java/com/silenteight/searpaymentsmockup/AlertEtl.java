package com.silenteight.searpaymentsmockup;

import lombok.NonNull;

interface AlertEtl {
  long invoke(@NonNull AlertDto alertDto);
}

export interface BatchType {
  name: string;
  canActivate: boolean;
}

export interface BatchTypes {
  available: BatchType[];
  assigned: BatchType[];
  activated: BatchType[];
}

export interface BatchTypeUpdates {
  toActivate: BatchType[];
  toDeactivate: BatchType[];
  toAssign: BatchType[];
  toUnassign: BatchType[];
}

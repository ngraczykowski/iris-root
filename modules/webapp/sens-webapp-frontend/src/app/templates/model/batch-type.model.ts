export interface BatchTypesDTO {
  assigned: string[];
  activated: string[];
  available: AvailableBatchType[];
}

export interface AvailableBatchType {
  decisionTreeName: string;
  decisionTreeId: string;
  batchType: string;
  canActivate: boolean;
}

export interface BatchTypesUpdateDTO {
  toAssign: string[];
  toUnassign: string[];
  toActivate: string[];
  toDeactivate: string[];
}

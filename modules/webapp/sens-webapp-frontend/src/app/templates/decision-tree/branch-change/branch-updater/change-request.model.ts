export enum StatusChange {
  NONE = 'NONE',
  ENABLED = 'ENABLED',
  DISABLED = 'DISABLED'
}

export interface ChangeRequest {

  decisionTreeId: number;
  matchGroupIds: number[];
  solution: string;
  status: StatusChange;
  comment: string;
}

export interface ProposeChangesResponse {
  changes: ProposedChange[];
}

export interface ProposedChange {

  changeId: number;
  matchGroupId: number;
}

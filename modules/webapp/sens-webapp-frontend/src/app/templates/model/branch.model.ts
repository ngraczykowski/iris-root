import { Summary } from './summary.model';

export class DecisionTreeInfo {
  id: number;
  name: string;
}

export class Feature {
  name: string;
  value: string;
}

export class Branch {
  decisionTreeInfo: DecisionTreeInfo;
  matchGroupId: number;
  features: Feature[];
  solution: string;
  score: number;
  enabled: boolean;
  reviewed: boolean;
  reviewedAt: String;
  reviewedBy: String;
  pendingChanges: boolean;
  lastUsedAt: string;
}

export class ChangeLog {
  entries: ChangeLogEntry[];
}

export class ChangeLogEntry {
  changeType: string;
  description: string;
  user: string;
  date: string;
}

export class BranchDetails {
  decisionTreeInfo: DecisionTreeInfo;
  matchGroupId: number;
  features: Feature[];
  solution: string;
  summary: Summary;
  active: boolean;
  enabled: boolean;
  reviewed: boolean;
  changeLog: ChangeLog;
  disabledByCircuitBreaker: boolean;

  deserialize(input: any): BranchDetails {
    Object.assign(this, input);
    return this;
  }
}

export class BranchModel {
  featureNames: string[];
}

export class BranchPageResponse {
  total: number;
  branchModel: BranchModel;
  branches: Branch[];

  deserialize(input: any): BranchPageResponse {
    Object.assign(this, input);
    return this;
  }
}

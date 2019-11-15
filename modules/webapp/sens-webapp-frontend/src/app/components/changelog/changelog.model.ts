export interface ReasoningBranchId {
  decisionTreeId: number;
  matchGroupId: number;
}

export interface ChangelogView {
  reasoningBranchId: ReasoningBranchId;
  solutionChange: Change<string>;
  statusChange: Change<boolean>;
  changelog: Changelog[];
}

export interface Change<T> {
  current: T;
  proposed: T;
}

export interface Changelog {
  comment: String;
  userName: String;
  timestamp: number;
  level: number;
}

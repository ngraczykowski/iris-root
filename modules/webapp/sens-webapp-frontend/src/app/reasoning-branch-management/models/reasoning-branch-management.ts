export interface ReasoningBranchDetails {
  reasoningBranchId: number;
  aiSolution: string;
  active: boolean;
}

export enum ReasoningBranchEmptyStates {
  DEFAULT = 'DEFAULT',
  NORESULTS = 'NORESULTS',
  TIMEOUT = 'TIMEOUT'
}

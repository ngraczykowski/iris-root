export interface ChangeRequest {
  bulkChangeId: string;
  id: number;
  createdAt: string;
  comment: string;
  reasoningBranchIds?: Array<ReasoningBranchIds>;
  aiSolution?: string;
  active?: boolean;
}

export interface ReasoningBranchIds {
  decisionTreeId: number;
  featureVectorId: number;
}

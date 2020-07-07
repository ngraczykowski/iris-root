export interface ReasoningBranchesListRequest {
  aiSolution?: string;
  active?: boolean;
  offset: number;
  limit: number;
}

export interface ReasoningBranchesListResponse {
  branches: ReasoningBranchesList[];
  total: number;
}

export interface BulkChangesResponse {
  reasoningBranchId: ReasoningBranchId;
  bulkChangeIds: string[];
}

export interface BulkChanges extends BulkChangesResponse {
  bulkChanges?: PendingChanges[];
}

export interface PendingChangesResponse {
  id: number;
  bulkChangeId: string;
  createdBy: string;
  createdAt: string;
  comment: string;
}

export type PendingChanges = PendingChangesResponse;

export interface ReasoningBranchesList {
  reasoningBranchId: ReasoningBranchId;
  aiSolution: string;
  active: boolean;
  updatedAt: string;
  createdAt: string;

  pendingChanges?: PendingChanges[];
}

export interface ReasoningBranchId {
  decisionTreeId: number;
  featureVectorId: number;
}


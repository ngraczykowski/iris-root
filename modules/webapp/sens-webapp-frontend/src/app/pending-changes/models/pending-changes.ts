export interface PendingChangeResponse {
  bulkChangeId: string;
  createdAt: string;
  comment: string;
  id: number;
  createdBy: string;

  state?: string;
  decidedAt?: string;
  decidedBy?: string;
  deciderComment?: string;
}

export interface BulkChangeResponse {
  id: string;
  reasoningBranchIds: ReasoningBranchIds[];
  aiSolution: string;
  active: boolean;
  createdAt: string;
}

export interface ReasoningBranchIds {
  decisionTreeId: number;
  featureVectorId: number;
}

export interface PendingChange {
  bulkChangeId: string;
  comment: string;
  createdAt: string;
  createdBy: string;
  reasoningBranchIds: ReasoningBranchIds[];
  id: number;
  aiSolution: string;
  active: boolean;
  state?: string;
  decidedAt?: string;
  decidedBy?: string;
  deciderComment?: string;
}

export enum PendingChangesStatus {
  PENDING = 'PENDING',
  CLOSED = 'CLOSED'
}

export interface DiscrepantBranchesResponse {
  branchId: DiscrepantBranchId;
  detectedAt: string;
}

export interface DiscrepantBranchId {
  decisionTreeId: number;
  featureVectorId: number;
}

export interface DiscrepanciesList {
  id: number;
  alertId: string;
  aiComment: string;
  aiCommentDate: string;
  analystComment: string;
  analystCommentDate: string;
}

export enum CircuitBreakerDiscrepancyStatus {
  ACTIVE = 'ACTIVE',
  ARCHIVED = 'ARCHIVED'
}

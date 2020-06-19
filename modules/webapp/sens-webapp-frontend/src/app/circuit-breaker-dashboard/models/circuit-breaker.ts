export interface DiscrepantBranchesResponse {
  discrepantBranchId: DiscrepantBranchDetails;
  detectedAt: string;
}

export interface DiscrepantBranchDetails {
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

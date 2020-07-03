export interface ValidateBranchIdsPayload {
  decisionTreeId: number;
  branchIds: number[];
}

export interface ValidateBranchIdsErrorResponse {
  error: {
    key: string;
    date: number;
    exception: string;
    extras: {
      branchIds: number[];
    } | any;
  };
}

export interface ValidateBranchIdsResponse {
  branchIds: Array<{
    branchId: number;
    featureVectorSignature: string;
  }>;
}

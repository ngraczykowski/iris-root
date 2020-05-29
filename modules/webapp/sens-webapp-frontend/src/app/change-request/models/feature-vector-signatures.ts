export interface FeatureVectorSignaturesPayload {
  decisionTreeId: number;
  featureVectorSignatures: string[];
}

export interface FeatureVectorSignaturesErrorResponse {
  error: {
    key: string;
    date: number;
    exception: string;
    extras: {
      featureVectorSignatures: number[];
    };
  };
}

export interface FeatureVectorSignaturesResponse {
  branchIds: Array<{
    branchId: number;
    featureVectorSignature: string;
  }>;
}

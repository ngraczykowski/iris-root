export interface InboxStatistics {
  total: number;
  stats: any;
}

export interface InboxMessage {
  id: number;
  type: string;
  referenceId: string;
  message: string;
  state: string;
  date: Date;
  extra: any;
}

export interface InboxMessageRequest {
  page: number;
  size: number;
  state: string;
}

export interface DisabledBranchInboxMessageExtra {
  decisionTreeId: number;
  decisionTreeName: string;
  matchGroupId: number;
  aiDecision: string;
  suspendingAlerts: SuspendingAlert[];
}

export interface SuspendingAlert {
  externalId: string;
  analystDecision: string;
}

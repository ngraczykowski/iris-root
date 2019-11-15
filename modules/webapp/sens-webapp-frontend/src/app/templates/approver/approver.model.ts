import { ReasoningBranchId } from '@app/components/changelog/changelog.model';

export interface Description {
  maker: string;
  comment: string;
  timestamp: string;
}

export interface ChangeRequest {
  changeId: number;
  decisionTreeId: number;
  decisionTreeName: string;
  reasoningBranchId: ReasoningBranchId;
  decision: Change<string>;
  status: Change<boolean>;
  score: number;
  description: Description;
}

export interface ApprovalQueueView {
  changeRequests: ChangeRequest[];
}

export interface Change<T> {
  from: T;
  to: T;
}

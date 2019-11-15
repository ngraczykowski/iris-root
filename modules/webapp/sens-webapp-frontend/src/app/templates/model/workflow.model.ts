export interface WorkflowList {
  workflows: Workflow[];
}

export interface Workflow {
  decisionTreeId: number;
  decisionTreeName: string;
  makers: string[];
  approvalLevels: number;
}

export interface ApprovalLevel {
  approvers: string[];
}

export interface WorkflowDetails {
  decisionTreeId: number;
  decisionTreeName: string;
  makers: string[];
  approvalLevels: ApprovalLevel[];
}

export interface WorkflowChange {
  decisionTreeId: number;
  makers: string[];
  approvalLevels: ApprovalLevel[];
}

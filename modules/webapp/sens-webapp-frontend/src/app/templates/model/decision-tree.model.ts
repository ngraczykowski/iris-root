import { Summary } from './summary.model';

export interface Model {
  id: string;
  name: string;
}

export interface DecisionTreeStatus {
  name: string;
  errorMessage?: string;
}

export interface Agent {
  name: string;
  description: string;
}

export enum DecisionTreePermission {
  DECISION_TREE_VIEW = 'DECISION_TREE_VIEW',
  DECISION_TREE_CHANGE = 'DECISION_TREE_CHANGE'
}

export interface DecisionTreeModel {
  id: number;
  name: string;
  active: boolean;
  status: DecisionTreeStatus;
  model: Model;
  agents: Agent[];
  activeReasoningBranches: number;
  totalReasoningBranches: number;
  outputPorts: string[];
  activations: string[];
  matchAlerts: number;
  permissions: DecisionTreePermission[];
}

export interface OutputPort {
  name: string;
  url: string;
}

export interface DecisionTreeDetailsModel {
  id: number;
  name: string;
  active: boolean;
  status: DecisionTreeStatus;
  model: Model;
  summary: Summary;
  agents: Agent[];
  outputPorts: OutputPort[];
  activations: string[];
  activeReasoningBranches: number;
  totalReasoningBranches: number;
  matchAlerts: number;
  permissions: DecisionTreePermission[];
}

export interface DecisionTreeFeaturesModel {
  name: string;
  description: string;
}

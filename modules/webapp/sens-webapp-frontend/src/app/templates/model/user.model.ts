export interface User {
  id: number;
  type: UserType;
  userName: string;
  displayName: string;
  roles: UserRole[];
  assignments: Assignment[];
  assignmentViews: AssignmentDto[];
  active: boolean;
  superUser: boolean;
}

export interface UserTableCell {
  userName: string;
  displayName: string;
  active: boolean;
  filterQuery?: string;
}

export interface AssignmentDto {
  decisionTreeId: string;
  decisionTreeName: string;
  role: string;
  level: number;
}

export interface Assignment {
  decisionTreeId: number;
  decisionTreeName: string;
  name: string;
}


export interface AssignmentView {
  decisionTreeName: string;
  role: string;
}

export enum UserRole {
  ROLE_ANALYST = 'ROLE_ANALYST',
  ROLE_APPROVER = 'ROLE_APPROVER',
  ROLE_AUDITOR = 'ROLE_AUDITOR',
  ROLE_BATCH_TYPE_MANAGER = 'ROLE_BATCH_TYPE_MANAGER',
  ROLE_DECISION_TREE_MANAGER = 'ROLE_DECISION_TREE_MANAGER',
  ROLE_USER_MANAGER = 'ROLE_USER_MANAGER',
  ROLE_INBOX_OPERATOR = 'ROLE_INBOX_OPERATOR',
  ROLE_MAKER = 'ROLE_MAKER'
}

export enum UserType {
  INTERNAL = 'INTERNAL',
  EXTERNAL = 'EXTERNAL'
}

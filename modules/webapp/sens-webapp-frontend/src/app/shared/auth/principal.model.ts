export class Principal {
  constructor(
      public userName: string,
      public displayName: string,
      public authorities: string[],
      public superUser: boolean
  ) { }

  hasAuthority(authority: string): boolean {
    return this.authorities.includes(authority);
  }
}

export const enum Authority {
  DECISION_TREE_LIST = 'DECISION_TREE_LIST',
  DECISION_TREE_MANAGE = 'DECISION_TREE_MANAGE',
  BATCH_TYPE_MANAGE = 'BATCH_TYPE_MANAGE',
  AUDIT_GENERATE_REPORT = 'AUDIT_GENERATE_REPORT',
  INBOX_MANAGE = 'INBOX_MANAGE',
  WORKFLOW_MANAGE = 'WORKFLOW_MANAGE',
  USER_MANAGE = 'USER_MANAGE',
  USER_VIEW = 'USER_VIEW',
  SOLUTION_VIEW = 'SOLUTION_VIEW'
}

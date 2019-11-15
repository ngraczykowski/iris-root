interface BreadcrumbNameFormatSupport {
  formatName(params): string;
}

interface BreadcrumbUrlFormatSupport {
  formatUrl(params): string;
}

export class DecisionTreesBreadcrumbFormatter implements BreadcrumbNameFormatSupport, BreadcrumbUrlFormatSupport {
  formatName(): string {
    return 'Decision Trees';
  }
  formatUrl(): string {
    return '/decision-tree';
  }
}

export class DecisionTreeBreadcrumbFormatter implements BreadcrumbNameFormatSupport, BreadcrumbUrlFormatSupport {

  formatName(params: { id, name?}): string {
    return `Decision Tree #${params.id}${params.name ? ` (${params.name})` : ''}`;
  }

  formatUrl(params: { id }): string {
    return `/decision-tree/${params.id}`;
  }
}

export class BranchBreadcrumbFormatter implements BreadcrumbNameFormatSupport, BreadcrumbUrlFormatSupport {

  formatName(params: { decisionTreeId, id }): string {
    return `Reasoning Branch #${params.decisionTreeId}-${params.id}`;
  }

  formatUrl(params: { decisionTreeId, matchGroupId }): string {
    return `/decision-tree/${params.decisionTreeId}/reasoning-branch/${params.matchGroupId}`;
  }
}

export class AlertBreadcrumbFormatter implements BreadcrumbNameFormatSupport {

  formatName(params: { id }): string {
    return `Alert (${params.id})`;
  }
}

export const DECISION_TREES = new DecisionTreesBreadcrumbFormatter();
export const DECISION_TREE = new DecisionTreeBreadcrumbFormatter();
export const BRANCH = new BranchBreadcrumbFormatter();
export const ALERT = new AlertBreadcrumbFormatter();

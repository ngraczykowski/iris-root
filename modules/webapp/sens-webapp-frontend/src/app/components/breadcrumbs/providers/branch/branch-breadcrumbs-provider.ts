import { BranchDetails } from '@app/templates/model/branch.model';
import { Breadcrumb, BreadcrumbsProvider } from '../../breadcrumbs.component';
import { BRANCH, DECISION_TREE, DECISION_TREES } from '../breadcrumb-formatters';

// TODO (iwnek) make spec
export class BranchBreadcrumbsProvider implements BreadcrumbsProvider {

  constructor(private branchDetails: BranchDetails) { }

  get(): Breadcrumb[] {
    return [
      {
        links: [
          {
            name: DECISION_TREES.formatName(),
            url: DECISION_TREES.formatUrl()
          }
        ]
      },
      {
        links: [
          {
            name: DECISION_TREE.formatName({ id: this.branchDetails.decisionTreeInfo.id, name: this.branchDetails.decisionTreeInfo.name }),
            url: DECISION_TREE.formatUrl({ id: this.branchDetails.decisionTreeInfo.id })
          }
        ]
      },
      {
        links: [
          {
            name: BRANCH.formatName({ id: this.branchDetails.matchGroupId, decisionTreeId: this.branchDetails.decisionTreeInfo.id, }),
          }
        ]
      }
    ];
  }
}

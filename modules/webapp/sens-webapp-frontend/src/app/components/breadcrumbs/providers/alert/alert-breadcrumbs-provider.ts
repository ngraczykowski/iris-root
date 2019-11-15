import {
  Breadcrumb,
  BreadcrumbLink,
  BreadcrumbsProvider
} from '@app/components/breadcrumbs/breadcrumbs.component';
import {
  ALERT,
  BRANCH,
  DECISION_TREE,
  DECISION_TREES
} from '@app/components/breadcrumbs/providers/breadcrumb-formatters';
import { AlertDetails } from '@app/templates/model/alert.model';
import { Branch, DecisionTreeInfo } from '@app/templates/model/branch.model';

// TODO (iwnek) make spec
export class AlertBreadcrumbsProvider implements BreadcrumbsProvider {

  constructor(private matchGroupId, private alertDetails: AlertDetails) { }

  get(): Breadcrumb[] {
    const decisionTreeInfo = this.resolveDecisionTreeInfo(this.alertDetails.branchInfos);
    const branchInfos = this.resolveBranchInfosToDisplay(this.matchGroupId, this.alertDetails.branchInfos);
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
            name: DECISION_TREE.formatName({ id: decisionTreeInfo.id, name: decisionTreeInfo.name }),
            url: DECISION_TREE.formatUrl({ id: decisionTreeInfo.id })
          }
        ]
      },
      {
        links: branchInfos.map(i => <BreadcrumbLink>{
          name: BRANCH.formatName({ id: i.matchGroupId, decisionTreeId: i.decisionTreeInfo.id}),
          url: BRANCH.formatUrl({ decisionTreeId: i.decisionTreeInfo.id, matchGroupId: i.matchGroupId })
        })
      },
      {
        links: [
          {
            name: ALERT.formatName({ id: this.alertDetails.party.externalId })
          }
        ]
      }
    ];
  }

  private resolveDecisionTreeInfo(branchInfos: Branch[]): DecisionTreeInfo {
    const decisionTreeIds = new Set();
    branchInfos.forEach(i => decisionTreeIds.add(i.decisionTreeInfo.id));
    if (decisionTreeIds.size > 1) {
      throw new Error('Currently multiple branches from different decision trees are not supported');
    }
    return branchInfos[0].decisionTreeInfo;
  }

  private resolveBranchInfosToDisplay(matchGroupId, branchInfos: Branch[]): Branch[] {
    const infos = branchInfos
      .filter(i => matchGroupId === undefined || String(i.matchGroupId) === matchGroupId);
    if (infos.length === 0) {
      throw new Error('Links should never be empty here.');
    }
    return infos;
  }
}

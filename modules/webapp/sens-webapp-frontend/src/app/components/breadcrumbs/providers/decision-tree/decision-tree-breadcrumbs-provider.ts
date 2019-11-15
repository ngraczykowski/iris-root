import { Breadcrumb, BreadcrumbsProvider } from '@app/components/breadcrumbs/breadcrumbs.component';
import { DecisionTreeDetails } from '@app/templates/model/decision-tree.model';
import { DECISION_TREE, DECISION_TREES } from '../breadcrumb-formatters';

// TODO (iwnek) make spec
export class DecisionTreeBreadcrumbsProvider implements BreadcrumbsProvider {

  constructor(private decisionTreeDetails: DecisionTreeDetails) { }

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
            name: DECISION_TREE.formatName({ id: this.decisionTreeDetails.id, name: this.decisionTreeDetails.name })
          }
        ]
      }
    ];
  }
}

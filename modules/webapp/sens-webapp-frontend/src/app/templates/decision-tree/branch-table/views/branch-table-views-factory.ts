import {
  Label,
  Row,
  TableData
} from '@app/components/dynamic-view-table/dynamic-view-table.component';
import { View } from '@app/components/dynamic-view/dynamic-view.component';
import {
  BranchLastUsedAtViewComponent,
  BranchLastUsedAtViewData
} from '@app/templates/decision-tree/branch-table/views/branch-last-used-at-view/branch-last-used-at-view.component';
import { Branch, BranchModel, Feature } from '@model/branch.model';
import { BranchChanges } from './branch-changes-view/branch-changes';
import { BranchChangesViewComponent } from './branch-changes-view/branch-changes-view.component';
import {
  BranchDecisionViewComponent,
  BranchDecisionViewData
} from './branch-decision-view/branch-decision-view.component';
import {
  BranchFeatureViewComponent,
  BranchFeatureViewData
} from './branch-feature-view/branch-feature-view.component';
import { BranchIdViewComponent, BranchIdViewData } from './branch-id-view/branch-id-view.component';
import {
  BranchLabelViewComponent,
  BranchLabelViewData
} from './branch-label-view/branch-label-view.component';
import {
  BranchReviewedStatusViewComponent,
  BranchReviewedStatusViewData
} from './branch-reviewed-status-view/branch-reviewed-status-view.component';
import {
  BranchScoreViewComponent,
  BranchScoreViewData
} from './branch-score-view/branch-score-view.component';
import {
  BranchStatusViewComponent,
  BranchStatusViewData
} from './branch-status-view/branch-status-view.component';

class BranchTableLabelViewsFactory {

  private static getLabels(branchModel: BranchModel) {
    const labels = ['Branch ID', 'Solution', 'Status', 'Score', 'Last used at', 'Status of changes', 'Review status'];
    branchModel.featureNames.forEach(f => labels.push(f));
    return labels;
  }

  static create(branchModel: BranchModel): View[] {
    return BranchTableLabelViewsFactory.getLabels(branchModel).map(f => <View>{
      component: BranchLabelViewComponent,
      data: <BranchLabelViewData> {
        text: f
      }
    });
  }
}

class BranchTableRowViewsFactory {

  private static createIdView(branch: Branch): View {
    return {
      component: BranchIdViewComponent,
      data: <BranchIdViewData> {
        decisionTreeId: branch.decisionTreeInfo.id,
        matchGroupId: branch.matchGroupId
      }
    };
  }

  private static createSolutionView(branch: Branch): View {
    return {
      component: BranchDecisionViewComponent,
      data: <BranchDecisionViewData> {decision: branch.solution}
    };
  }

  private static createStatusView(branch: Branch): View {
    return {
      component: BranchStatusViewComponent,
      data: <BranchStatusViewData> {enabled: branch.enabled}
    };
  }

  private static createScoreView(branch: Branch): View {
    return {
      component: BranchScoreViewComponent,
      data: <BranchScoreViewData> {score: branch.score}
    };
  }

  private static createReviewedStatusView(branch: Branch): View {
    return {
      component: BranchReviewedStatusViewComponent,
      data: <BranchReviewedStatusViewData> {
        reviewed: branch.reviewed,
        reviewedAt: branch.reviewedAt,
        reviewedBy: branch.reviewedBy
      }
    };
  }

  private static createLastUsedAtView(branch: Branch): View {
    return {
      component: BranchLastUsedAtViewComponent,
      data: <BranchLastUsedAtViewData> {lastUsedAt: branch.lastUsedAt}
    };
  }

  private static createFeatureView(feature: Feature): View {
    return {
      component: BranchFeatureViewComponent,
      data: <BranchFeatureViewData> {
        value: feature.value
      }
    };
  }

  private static createChangesView(branch: Branch): View {
    return {
      component: BranchChangesViewComponent,
      data: <BranchChanges> {
        decisionTreeId: branch.decisionTreeInfo.id,
        matchGroupId: branch.matchGroupId,
        pendingChanges: branch.pendingChanges
      }
    };
  }

  static create(branch: Branch): View[] {
    const views = [];

    views.push(
        BranchTableRowViewsFactory.createIdView(branch),
        BranchTableRowViewsFactory.createSolutionView(branch),
        BranchTableRowViewsFactory.createStatusView(branch),
        BranchTableRowViewsFactory.createScoreView(branch),
        BranchTableRowViewsFactory.createLastUsedAtView(branch),
        BranchTableRowViewsFactory.createChangesView(branch),
        BranchTableRowViewsFactory.createReviewedStatusView(branch)
    );

    branch.features
        .map(f => BranchTableRowViewsFactory.createFeatureView(f))
        .forEach(v => views.push(v));

    return views;
  }
}

export class BranchTableDataFactory {

  static create(total: number, branchModel: BranchModel, branches: Branch[]) {
    return <TableData>{
      total: total,
      labels: BranchTableLabelViewsFactory.create(branchModel)
          .map(v => <Label> {view: v}),
      rows: branches
          .map(b => BranchTableRowViewsFactory.create(b))
          .map(views => <Row> {views: views})
    };
  }
}

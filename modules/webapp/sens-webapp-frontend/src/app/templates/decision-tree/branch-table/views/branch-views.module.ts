import { NgModule } from '@angular/core';
import { ChangelogModule } from '@app/components/changelog/changelog.module';
import { SharedModule } from '@app/shared/shared.module';
import { BranchChangesViewComponent } from './branch-changes-view/branch-changes-view.component';
import { BranchDecisionViewComponent } from './branch-decision-view/branch-decision-view.component';
import { BranchFeatureViewComponent } from './branch-feature-view/branch-feature-view.component';
import { BranchIdViewComponent } from './branch-id-view/branch-id-view.component';
import { BranchLabelViewComponent } from './branch-label-view/branch-label-view.component';
import { BranchLastUsedAtViewComponent } from './branch-last-used-at-view/branch-last-used-at-view.component';
import { BranchReviewStatusViewDataComponent } from './branch-reviewed-status-view/branch-review-status-view-data/branch-review-status-view-data.component';
import { BranchReviewedStatusViewComponent } from './branch-reviewed-status-view/branch-reviewed-status-view.component';
import { BranchScoreViewComponent } from './branch-score-view/branch-score-view.component';
import { BranchStatusViewComponent } from './branch-status-view/branch-status-view.component';

@NgModule({
  imports: [
    SharedModule,
    ChangelogModule
  ],
  declarations: [
    BranchLabelViewComponent,
    BranchIdViewComponent,
    BranchDecisionViewComponent,
    BranchScoreViewComponent,
    BranchFeatureViewComponent,
    BranchReviewedStatusViewComponent,
    BranchStatusViewComponent,
    BranchChangesViewComponent,
    BranchReviewStatusViewDataComponent,
    BranchLastUsedAtViewComponent,
  ],
  entryComponents: [
    BranchLabelViewComponent,
    BranchIdViewComponent,
    BranchDecisionViewComponent,
    BranchScoreViewComponent,
    BranchFeatureViewComponent,
    BranchReviewedStatusViewComponent,
    BranchStatusViewComponent,
    BranchChangesViewComponent,
    BranchLastUsedAtViewComponent
  ]
})
export class BranchViewsModule {}

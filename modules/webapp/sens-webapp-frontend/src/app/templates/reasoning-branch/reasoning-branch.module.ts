import { NgModule } from '@angular/core';
import { ChangelogModule } from '@app/components/changelog/changelog.module';
import { HintFeedbackModule } from '@app/components/hint-feedback/hint-feedback.module';
import { SharedModule } from '@app/shared/shared.module';
import { DisabledBranchNotificationModule } from './disabled-branch-notification/disabled-branch-notification.module';
import { BranchReviewButtonModule } from './reasoning-branch-details/branch-review-button/branch-review-button.module';
import { ReasoningBranchDetailsModule } from './reasoning-branch-details/reasoning-branch-details.module';
import { ReasoningBranchFeaturesComponent } from './reasoning-branch-features/reasoning-branch-features.component';
import { ReasoningBranchComponent } from './reasoning-branch.component';
import { ReasoningBranchService } from './reasoning-branch.service';

@NgModule({
  imports: [
    SharedModule,
    DisabledBranchNotificationModule,
    BranchReviewButtonModule,
    ChangelogModule,
    ReasoningBranchDetailsModule,
    HintFeedbackModule
  ],
  providers: [
    ReasoningBranchService
  ],
  declarations: [
    ReasoningBranchComponent,
    ReasoningBranchFeaturesComponent
  ],
  exports: [
    ReasoningBranchComponent
  ]
})
export class ReasoningBranchModule {}

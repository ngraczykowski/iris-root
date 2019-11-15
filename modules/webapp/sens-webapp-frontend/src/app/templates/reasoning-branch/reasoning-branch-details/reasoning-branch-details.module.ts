import { NgModule } from '@angular/core';
import { ChangelogModule } from '@app/components/changelog/changelog.module';
import { SharedModule } from '@app/shared/shared.module';
import { BranchReviewButtonModule } from './branch-review-button/branch-review-button.module';
import { ReasoningBranchDetailsModalModule } from './reasoning-branch-details-changelog-modal/reasoning-branch-details-changelog-modal.module';
import { ReasoningBranchDetailsComponent } from './reasoning-branch-details.component';

@NgModule({
  imports: [
    SharedModule,
    BranchReviewButtonModule,
    ChangelogModule,
    ReasoningBranchDetailsModalModule
  ],
  declarations: [
    ReasoningBranchDetailsComponent,
  ],
  exports: [
    ReasoningBranchDetailsComponent
  ]
})

export class ReasoningBranchDetailsModule {
}

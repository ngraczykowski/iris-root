import { NgModule } from '@angular/core';
import { ChangelogModule } from '@app/components/changelog/changelog.module';
import { SharedModule } from '@app/shared/shared.module';
import { ReasoningBranchDetailsChangelogModalComponent } from './reasoning-branch-details-changelog-modal.component';

@NgModule({
  imports: [
    SharedModule,
    ChangelogModule
  ],
  declarations: [
    ReasoningBranchDetailsChangelogModalComponent
  ],
  exports: [
    ReasoningBranchDetailsChangelogModalComponent
  ]
})

export class ReasoningBranchDetailsModalModule {
}

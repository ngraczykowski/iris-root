import { NgModule } from '@angular/core';
import { SharedModule } from '@app/shared/shared.module';
import { BranchBulkSelectorModule } from '@app/templates/decision-tree/branch-change/branch-bulk-selector/branch-bulk-selector.module';
import { BranchRejectModule } from '@app/templates/decision-tree/branch-change/branch-reject/branch-reject.module';
import { BranchUpdaterModule } from '@app/templates/decision-tree/branch-change/branch-updater/branch-updater.module';
import { BranchChangeStateService } from './branch-change-state.service';
import { BranchChangeComponent } from './branch-change.component';

@NgModule({
  imports: [
    SharedModule,
    BranchBulkSelectorModule,
    BranchUpdaterModule,
    BranchRejectModule
  ],
  providers: [
    BranchChangeStateService
  ],
  declarations: [
    BranchChangeComponent
  ],
  exports: [
    BranchChangeComponent
  ]
})
export class BranchChangeModule {}

import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { ApplicationHeaderModule } from '@app/components/application-header/application-header.module';
import { ConfirmChangesComponent } from '@app/reasoning-branch-management/components/confirm-changes/confirm-changes.component';
import { SharedModule } from '@app/shared/shared.module';

import { BranchDetailsComponent } from './components/branch-details/branch-details.component';
import { LoadBranchComponent } from './components/load-branch/load-branch.component';

import { ReasoningBranchManagementRoutingModule } from './reasoning-branch-management-routing.module';
import { ReasoningBranchManagementPageComponent } from './containers/reasoning-branch-management-page/reasoning-branch-management-page.component';
import { BranchEmptyStateComponent } from '@app/reasoning-branch-management/components/branch-empty-state/branch-empty-state.component';

@NgModule({
  declarations: [
    ReasoningBranchManagementPageComponent,
    LoadBranchComponent,
    BranchDetailsComponent,
    BranchEmptyStateComponent,
    ConfirmChangesComponent
  ],
  imports: [
    CommonModule,
    ApplicationHeaderModule,
    ReasoningBranchManagementRoutingModule,
    SharedModule
  ]
})
export class ReasoningBranchManagementModule {}

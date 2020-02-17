import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { ApplicationHeaderModule } from '@app/components/application-header/application-header.module';

import { SharedModule } from '@app/shared/shared.module';
import { BranchDetailsComponent } from './components/branch-details/branch-details.component';
import { LoadBranchComponent } from './components/load-branch/load-branch.component';
import { ReasoningBranchManagementPageComponent } from './containers/reasoning-branch-management-page/reasoning-branch-management-page.component';
import { BranchEmptyStateComponent } from '@app/reasoning-branch-management/components/branch-empty-state/branch-empty-state.component';

import { ReasoningBranchManagementRoutingModule } from './reasoning-branch-management-routing.module';

@NgModule({
  declarations: [
    ReasoningBranchManagementPageComponent,
    LoadBranchComponent,
    BranchDetailsComponent,
    BranchEmptyStateComponent
  ],
  imports: [
    CommonModule,
    ApplicationHeaderModule,
    ReasoningBranchManagementRoutingModule,
    SharedModule
  ]
})
export class ReasoningBranchManagementModule {}

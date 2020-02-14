import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SharedModule } from '@app/shared/shared.module';

import { ReasoningBranchManagementRoutingModule } from './reasoning-branch-management-routing.module';
import { ReasoningBranchManagementPageComponent } from './containers/reasoning-branch-management-page/reasoning-branch-management-page.component';
import { ApplicationHeaderModule } from '@app/components/application-header/application-header.module';
import { LoadBranchComponent } from './components/load-branch/load-branch.component';
import { BranchDetailsComponent } from './components/branch-details/branch-details.component';

@NgModule({
  declarations: [
    ReasoningBranchManagementPageComponent,
    LoadBranchComponent,
    BranchDetailsComponent,
  ],
  imports: [
    CommonModule,
    ApplicationHeaderModule,
    ReasoningBranchManagementRoutingModule,
    SharedModule
  ]
})
export class ReasoningBranchManagementModule { }

import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ReasoningBranchManagementRoutingModule } from './reasoning-branch-management-routing.module';
import { ReasoningBranchManagementPageComponent } from './containers/reasoning-branch-management-page/reasoning-branch-management-page.component';
import { ApplicationHeaderModule } from '@app/components/application-header/application-header.module';

@NgModule({
  declarations: [
    ReasoningBranchManagementPageComponent
  ],
  imports: [
    CommonModule,
    ApplicationHeaderModule,
    ReasoningBranchManagementRoutingModule
  ]
})
export class ReasoningBranchManagementModule { }

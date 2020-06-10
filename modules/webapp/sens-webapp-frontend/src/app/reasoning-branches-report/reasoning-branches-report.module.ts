import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatStepperModule } from '@angular/material/stepper';
import { SharedModule } from '@app/shared/shared.module';
import { UiComponentsModule } from '@app/ui-components/ui-components.module';
import { ReasoningBranchesReportComponent } from './containers/reasoning-branches-report/reasoning-branches-report.component';
import { ConfigureReasoningBranchesReportComponent } from './components/configure-reasoning-branches-report/configure-reasoning-branches-report.component';

@NgModule({
  declarations: [
    ReasoningBranchesReportComponent,
    ConfigureReasoningBranchesReportComponent
  ],
  imports: [
    CommonModule,
    MatStepperModule,
    UiComponentsModule,
    MatButtonModule,
    MatFormFieldModule,
    MatIconModule,
    ReactiveFormsModule,
    MatInputModule,
    SharedModule
  ]
})
export class ReasoningBranchesReportModule {}

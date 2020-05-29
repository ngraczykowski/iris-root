import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatButtonToggleModule } from '@angular/material/button-toggle';
import { MatDialogModule } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatListModule } from '@angular/material/list';
import { MatSelectModule } from '@angular/material/select';
import { MatStepperModule } from '@angular/material/stepper';
import { MatTooltipModule } from '@angular/material/tooltip';
import { UiComponentsModule } from '@app/ui-components/ui-components.module';
import { TranslateModule } from '@ngx-translate/core';
import { ChangeRequestComponent } from './containers/change-request/change-request.component';
import { ChangeRequestFormComponent } from './components/change-request-form/change-request-form.component';
import {
  SelectReasoningBranchesFormComponent
} from './components/select-reasoning-branches-form/select-reasoning-branches-form.component';
import { SelectedReasoningBranchesComponent } from './components/selected-reasoning-branches/selected-reasoning-branches.component';
import {
  SelectReasoningBranchContainerComponent
} from './containers/select-reasoning-branch-container/select-reasoning-branch-container.component';
import {
  ChangeRequestPropertiesContainerComponent
} from './containers/change-request-properties-container/change-request-properties-container.component';
import { RbVerificationDialogComponent } from './components/rb-verification-dialog/rb-verification-dialog.component';
import { ConfirmChangeRequestDialogComponent } from './components/confirm-change-request-dialog/confirm-change-request-dialog.component';
import { SharedModule } from '@app/shared/shared.module';

@NgModule({
  declarations: [
    ChangeRequestComponent,
    ChangeRequestFormComponent,
    SelectReasoningBranchesFormComponent,
    SelectedReasoningBranchesComponent,
    SelectReasoningBranchContainerComponent,
    ChangeRequestPropertiesContainerComponent,
    RbVerificationDialogComponent,
    ConfirmChangeRequestDialogComponent],
  imports: [
    CommonModule,
    MatFormFieldModule,
    MatStepperModule,
    MatInputModule,
    MatButtonModule,
    FormsModule,
    UiComponentsModule,
    MatIconModule,
    MatButtonToggleModule,
    TranslateModule,
    MatListModule,
    MatSelectModule,
    MatTooltipModule,
    MatDialogModule,
    SharedModule
  ],
  entryComponents: [
    RbVerificationDialogComponent,
    ConfirmChangeRequestDialogComponent]
})
export class ChangeRequestModule {}

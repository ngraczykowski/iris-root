import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import {
  MatButtonModule, MatChipsModule, MatDividerModule, MatExpansionModule,
  MatFormFieldModule, MatIconModule,
  MatInputModule, MatListModule,
  MatTableModule, MatTooltipModule
} from '@angular/material';
import { RouterModule } from '@angular/router';
import { userManagementRoutes } from '@app/miniapps/user-management/user-management.routing';
import { UiComponentsModule } from '@app/ui-components/ui-components.module';
import { UserManagemenetModule as UserManagementCoreModule } from '@core/user-managemenet/user-managemenet.module';
import { TranslateModule } from '@ngx-translate/core';
import { AnimationModule } from '@ui/animation/animation.module';
import { BottomSheetModule } from '@ui/bottom-sheet/bottom-sheet.module';
import { ChipsModule } from '@ui/chips/chips.module';
import { FootnoteModule } from '@ui/footnote/footnote.module';
import { DialogModule } from '@ui/dialog/dialog.module';
import { FormFieldModule } from '@ui/form-field/form-field.module';
import { HighlightModule } from '@ui/highlight/highlight.module';
import { LayoutsModule } from '@ui/layouts/layouts.module';
import { UserManagementListComponent } from './containers/user-management-list/user-management-list.component';
import { UserManagementTableComponent } from './components/user-management-table/user-management-table.component';
import { EditUserFormComponent } from './components/edit-user-form/edit-user-form.component';
import { ResetUserPasswordComponent } from './components/reset-user-password/reset-user-password.component';

@NgModule({
  declarations: [UserManagementListComponent, UserManagementTableComponent, EditUserFormComponent, ResetUserPasswordComponent],
  imports: [
      CommonModule,
      RouterModule.forChild(userManagementRoutes),
      UserManagementCoreModule,
      BottomSheetModule,
      FormsModule,
      ReactiveFormsModule,
      UiComponentsModule,
      LayoutsModule,
      MatFormFieldModule,
      MatInputModule,
      MatIconModule,
      MatTableModule,
      MatButtonModule,
      MatChipsModule,
      MatListModule,
      MatDividerModule,
      MatExpansionModule,
      MatTooltipModule,
      HighlightModule,
      TranslateModule,
      AnimationModule,
      FormFieldModule,
      DialogModule,
      ChipsModule
  ],
  entryComponents: [EditUserFormComponent]
})
export class UserManagementModule { }

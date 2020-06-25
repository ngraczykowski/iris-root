import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import {
  MatButtonToggleModule,
  MatFormFieldModule,
  MatInputModule,
  MatLabel, MatSelectModule,
  MatStepperModule, MatTooltipModule
} from '@angular/material';
import { MatButtonModule } from '@angular/material/button';
import { MatDialogModule } from '@angular/material/dialog';
import { MatDividerModule } from '@angular/material/divider';
import { MatIconModule } from '@angular/material/icon';
import { MatListModule } from '@angular/material/list';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatSortModule } from '@angular/material/sort';
import { MatTableModule } from '@angular/material/table';
import { SharedModule } from '@app/shared/shared.module';
import { UiComponentsModule } from '@app/ui-components/ui-components.module';
import { TranslateModule } from '@ngx-translate/core';
import { PendingChangesComponent } from './containers/pending-changes/pending-changes.component';
import { PendingChangesTableComponent } from './components/pending-changes-table/pending-changes-table.component';
import { PendingChangesPreviewContainerComponent } from './containers/pending-changes-preview-container/pending-changes-preview-container.component';
import { ChangeRequestPreviewComponent } from './components/change-request-preview/change-request-preview.component';
import { MatSnackBarModule } from '@angular/material/snack-bar';

@NgModule({
  declarations: [
    PendingChangesComponent,
    PendingChangesTableComponent,
    PendingChangesPreviewContainerComponent,
    ChangeRequestPreviewComponent],
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    MatSidenavModule,
    MatTableModule,
    TranslateModule,
    MatSortModule,
    MatDividerModule,
    MatListModule,
    MatButtonModule,
    MatIconModule,
    MatDialogModule,
    UiComponentsModule,
    MatSnackBarModule,
    MatFormFieldModule,
    MatInputModule,
    SharedModule
  ]
})
export class PendingChangesModule {}

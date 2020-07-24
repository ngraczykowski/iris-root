import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatFormFieldModule, MatInputModule } from '@angular/material';
import { MatButtonModule } from '@angular/material/button';
import { MatDialogModule } from '@angular/material/dialog';
import { MatDividerModule } from '@angular/material/divider';
import { MatIconModule } from '@angular/material/icon';
import { MatListModule } from '@angular/material/list';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { MatSortModule } from '@angular/material/sort';
import { MatTableModule } from '@angular/material/table';
import { MatTabsModule } from '@angular/material/tabs';
import { PendingChangesTabsContainerComponent } from '@app/pending-changes/containers/pending-changes-tabs-container/pending-changes-tabs-container.component';
import { SharedModule } from '@app/shared/shared.module';
import { UiComponentsModule } from '@app/ui-components/ui-components.module';
import { TranslateModule } from '@ngx-translate/core';
import { DialogModule } from '@ui/dialog/dialog.module';
import { ClosedChangeRequestPreviewComponent } from './components/closed-change-request-preview/closed-change-request-preview.component';
import { PendingChangeRequestPreviewComponent } from './components/pending-change-request-preview/pending-change-request-preview.component';
import { PendingChangesTableComponent } from './components/pending-changes-table/pending-changes-table.component';
import { PendingChangesPreviewContainerComponent } from './containers/pending-changes-preview-container/pending-changes-preview-container.component';
import { PendingChangesComponent } from './containers/pending-changes/pending-changes.component';

@NgModule({
  declarations: [
    PendingChangesTabsContainerComponent,
    PendingChangesComponent,
    PendingChangesTableComponent,
    PendingChangesPreviewContainerComponent,
    PendingChangesPreviewContainerComponent,
    PendingChangeRequestPreviewComponent,
    ClosedChangeRequestPreviewComponent],
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
    SharedModule,
    DialogModule,
    MatTabsModule,
  ]
})
export class PendingChangesModule {}

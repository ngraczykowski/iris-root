import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { MatDialogModule } from '@angular/material/dialog';
import { MatDividerModule } from '@angular/material/divider';
import { MatIconModule } from '@angular/material/icon';
import { MatListModule } from '@angular/material/list';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatSortModule } from '@angular/material/sort';
import { MatTableModule } from '@angular/material/table';
import { UiComponentsModule } from '@app/ui-components/ui-components.module';
import { TranslateModule } from '@ngx-translate/core';
import { PendingChangesComponent } from './containers/pending-changes/pending-changes.component';
import { PendingChangesTableComponent } from './components/pending-changes-table/pending-changes-table.component';
import { ChangeRequestDecisionDialogComponent } from './components/change-request-decision-dialog/change-request-decision-dialog.component';
import { PendingChangesPreviewContainerComponent } from './containers/pending-changes-preview-container/pending-changes-preview-container.component';
import { ChangeRequestPreviewComponent } from './components/change-request-preview/change-request-preview.component';
import {MatSnackBarModule} from '@angular/material/snack-bar';

@NgModule({
  declarations: [
    PendingChangesComponent,
    PendingChangesTableComponent,
    ChangeRequestDecisionDialogComponent,
    PendingChangesPreviewContainerComponent,
    ChangeRequestPreviewComponent],
  imports: [
    CommonModule,
    MatSidenavModule,
    MatTableModule,
    TranslateModule,
    MatSortModule,
    MatDividerModule,
    MatListModule,
    MatButtonModule,
    MatIconModule,
    MatDialogModule,
    TranslateModule,
    UiComponentsModule,
    MatSnackBarModule,
  ],
  entryComponents: [
    ChangeRequestDecisionDialogComponent
  ]
})
export class PendingChangesModule {}

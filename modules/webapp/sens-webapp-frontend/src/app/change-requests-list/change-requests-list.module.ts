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
import { ChangeRequestsListComponent } from './containers/change-requests-list/change-requests-list.component';
import { ChangeRequestsTableComponent } from './components/change-requests-table/change-requests-table.component';
import { ChangeRequestDecisionDialogComponent } from './components/change-request-decision-dialog/change-request-decision-dialog.component';
import { ChangeRequestPreviewContainerComponent } from './containers/change-request-preview-container/change-request-preview-container.component';
import { ChangeRequestPreviewComponent } from './components/change-request-preview/change-request-preview.component';

@NgModule({
  declarations: [
    ChangeRequestsListComponent,
    ChangeRequestsTableComponent,
    ChangeRequestDecisionDialogComponent,
    ChangeRequestPreviewContainerComponent,
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
    UiComponentsModule
  ],
  entryComponents: [
    ChangeRequestDecisionDialogComponent
  ]
})
export class ChangeRequestsListModule {}

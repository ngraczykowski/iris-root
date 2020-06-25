import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatBottomSheet, MatBottomSheetModule } from '@angular/material/bottom-sheet';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatDividerModule } from '@angular/material/divider';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatListModule } from '@angular/material/list';
import { MatSelectModule } from '@angular/material/select';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatTableModule } from '@angular/material/table';
import { UiComponentsModule } from '@app/ui-components/ui-components.module';
import { ReasoningBranchBrowserComponent } from './containers/reasoning-branch-browser/reasoning-branch-browser.component';
import { ReasoningBranchesTableComponent } from './components/reasoning-branches-table/reasoning-branches-table.component';
import { ReasoningBranchesListContainerComponent } from './containers/reasoning-branches-list-container/reasoning-branches-list-container.component';
import { ReasoningBranchDetailsContainerComponent } from './containers/reasoning-branch-details-container/reasoning-branch-details-container.component';
import { TranslateModule } from '@ngx-translate/core';
import { CreateChangeRequestComponent } from './components/create-change-request/create-change-request.component';

@NgModule({
  declarations: [
    ReasoningBranchBrowserComponent,
    ReasoningBranchesTableComponent,
    ReasoningBranchesListContainerComponent,
    ReasoningBranchDetailsContainerComponent,
    CreateChangeRequestComponent
  ],
  imports: [
    CommonModule,
    MatSidenavModule,
    UiComponentsModule,
    MatTableModule,
    MatCheckboxModule,
    TranslateModule,
    MatDividerModule,
    MatSelectModule,
    MatInputModule,
    MatListModule,
    MatBottomSheetModule,
    MatIconModule
  ],
  entryComponents: [
    CreateChangeRequestComponent
  ]
})
export class ReasoningBranchBrowserModule {}

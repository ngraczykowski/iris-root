import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatBottomSheetModule } from '@angular/material/bottom-sheet';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatDividerModule } from '@angular/material/divider';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatListModule } from '@angular/material/list';
import { MatSelectModule } from '@angular/material/select';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatTableModule } from '@angular/material/table';
import { UiComponentsModule } from '@app/ui-components/ui-components.module';
import { ReasoningBranchesBrowserComponent } from './containers/reasoning-branches-browser/reasoning-branches-browser.component';
import { ReasoningBranchesTableComponent } from './components/reasoning-branches-table/reasoning-branches-table.component';
import { ReasoningBranchesListContainerComponent } from './containers/reasoning-branches-list-container/reasoning-branches-list-container.component';
import { ReasoningBranchDetailsContainerComponent } from './containers/reasoning-branch-details-container/reasoning-branch-details-container.component';
import { TranslateModule } from '@ngx-translate/core';
import { CreateChangeRequestComponent } from './components/create-change-request/create-change-request.component';
import { ChangeRequestPanelContainerComponent } from './containers/change-request-panel-container/change-request-panel-container.component';

@NgModule({
  declarations: [
    ReasoningBranchesBrowserComponent,
    ReasoningBranchesTableComponent,
    ReasoningBranchesListContainerComponent,
    ReasoningBranchDetailsContainerComponent,
    CreateChangeRequestComponent,
    ChangeRequestPanelContainerComponent
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
export class ReasoningBranchesBrowserModule {}

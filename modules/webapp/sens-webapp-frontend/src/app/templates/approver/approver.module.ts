import { NgModule } from '@angular/core';
import { ChangelogModule } from '@app/components/changelog/changelog.module';
import { InputModule } from '@app/components/input/input.module';
import { PageableDynamicTableModule } from '@app/components/pageable-dynamic-table/pageable-dynamic-table.module';
import { SelectableDynamicTableModule } from '@app/components/selectable-dynamic-table/selectable-dynamic-table.module';
import { SharedModule } from '@app/shared/shared.module';
import { ApproverApiService } from '@app/templates/approver/approver-api.service';
import { ApproverStore } from '@app/templates/approver/approver.store';
import { TranslateModule } from '@ngx-translate/core';
import { ApproverPanelTableComponent } from './approver-panel/approver-panel-table/approver-panel-table.component';
import { ApproverPanelComponent } from './approver-panel/approver-panel.component';
import { AiDecisionChangeViewComponent } from './approver-table-shared/views/ai-decision-change-view/ai-decision-change-view.component';
import { ChangelogViewComponent } from './approver-table-shared/views/changelog-view/changelog-view.component';
import { CommentViewComponent } from './approver-table-shared/views/comment-view/comment-view.component';
import { DecisionTreeLinkViewComponent } from './approver-table-shared/views/decision-tree-link-view/decision-tree-link-view.component';
import { ReasoningBranchLinkViewComponent } from './approver-table-shared/views/reasoning-branch-link-view/reasoning-branch-link-view.component';
import { ScoreViewComponent } from './approver-table-shared/views/score-view/score-view.component';
import { StatusChangeViewComponent } from './approver-table-shared/views/status-change-view/status-change-view.component';
import { TitleViewComponent } from './approver-table-shared/views/title-view/title-view.component';
import { ApproverComponent } from './approver.component';
import { ChangesSelectableTableComponent } from './changes-table/changes-selectable-table.component';

@NgModule({
  imports: [
    SharedModule,
    ChangelogModule,
    SelectableDynamicTableModule,
    PageableDynamicTableModule,
    TranslateModule,
    InputModule,
  ],
  providers: [
    ApproverApiService,
    ApproverStore
  ],
  declarations: [
    ApproverComponent,
    ApproverPanelComponent,
    ChangesSelectableTableComponent,
    TitleViewComponent,
    AiDecisionChangeViewComponent,
    StatusChangeViewComponent,
    ChangelogViewComponent,
    CommentViewComponent,
    ApproverPanelTableComponent,
    DecisionTreeLinkViewComponent,
    ReasoningBranchLinkViewComponent,
    ScoreViewComponent
  ],
  exports: [
    ApproverComponent
  ],
  entryComponents: [
    TitleViewComponent,
    AiDecisionChangeViewComponent,
    StatusChangeViewComponent,
    CommentViewComponent,
    ChangelogViewComponent,
    DecisionTreeLinkViewComponent,
    ReasoningBranchLinkViewComponent,
    ScoreViewComponent
  ]
})
export class ApproverModule {}

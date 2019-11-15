import { EventEmitter } from '@angular/core';
import { Label } from '@app/components/dynamic-view-table/dynamic-view-table.component';
import { View } from '@app/components/dynamic-view/dynamic-view.component';
import { DecisionTreeLinkViewComponent } from '@app/templates/approver/approver-table-shared/views/decision-tree-link-view/decision-tree-link-view.component';
import { ReasoningBranchLinkViewComponent } from '@app/templates/approver/approver-table-shared/views/reasoning-branch-link-view/reasoning-branch-link-view.component';
import { ScoreViewComponent } from '@app/templates/approver/approver-table-shared/views/score-view/score-view.component';
import { ChangeRequest } from '@app/templates/approver/approver.model';
import { OpenChangelogEvent } from '@app/templates/approver/approver.store';
import { AiDecisionChangeViewComponent } from './views/ai-decision-change-view/ai-decision-change-view.component';
import { ChangelogViewComponent } from './views/changelog-view/changelog-view.component';
import { CommentViewComponent } from './views/comment-view/comment-view.component';
import { StatusChangeViewComponent } from './views/status-change-view/status-change-view.component';
import { TitleViewComponent } from './views/title-view/title-view.component';


export const mapItemToViews = (item: ChangeRequest, openChangelog: EventEmitter<OpenChangelogEvent>) => [
  new View(DecisionTreeLinkViewComponent, {
    decisionTreeId: item.reasoningBranchId.decisionTreeId,
    decisionTreeName: item.decisionTreeName
  }),
  new View(ReasoningBranchLinkViewComponent, {
    decisionTreeId: item.reasoningBranchId.decisionTreeId,
    reasoningBranchId: item.reasoningBranchId.matchGroupId
  }),
  new View(ScoreViewComponent, {
    score: item.score
  }),
  new View(AiDecisionChangeViewComponent, {
    change: item.decision
  }),
  new View(StatusChangeViewComponent, {
    change: item.status
  }),
  new View(CommentViewComponent, {
    matchGroupId: item.reasoningBranchId.matchGroupId,
    decisionTreeId: item.decisionTreeId,
    userName: item.description.maker,
    comment: item.description.comment,
    openChangelog
  }),
  new View(ChangelogViewComponent, {
    matchGroupId: item.reasoningBranchId.matchGroupId,
    decisionTreeId: item.decisionTreeId,
    openChangelog
  }),
];

export const LABEL_VIEWS = [
  'Decision Tree',
  'Branch ID',
  'Score',
  'Suggested Solution',
  'Suggested Status',
  'Comment',
  'Changelog',
].map(text => <Label>{
  view: TitleViewComponent.of(text)
});

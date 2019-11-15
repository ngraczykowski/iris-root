import { Component, Input, OnInit } from '@angular/core';
import { CellViewFactory } from '@app/components/dynamic-view-table/table-data-mapper';
import { View } from '@app/components/dynamic-view/dynamic-view.component';
import { DecisionTree } from '@model/decision-tree.model';
import { BatchType, BatchTypeListBuilder } from './batch-type-list-builder';

export interface DecisionTreeAssignmentsViewData {
  assignments: string[];
  activations: string[];
  decisionTreeId: number;
}

@Component({
  selector: 'app-decision-tree-assignments-view',
  templateUrl: './decision-tree-assignments-view.component.html',
  styleUrls: ['./decision-tree-assignments-view.component.scss']
})
export class DecisionTreeAssignmentsViewComponent implements OnInit {

  @Input()
  set data(data: DecisionTreeAssignmentsViewData) {
    this.batchTypes = new BatchTypeListBuilder(data.assignments, data.activations).build();
    this.link = `/decision-tree/${data.decisionTreeId}`;
  }

  batchTypes: BatchType[];
  link: string;

  readonly batchTypesTooltipListLimit = 7;
  readonly showTooltipForListLongerThan = 5;

  constructor() { }

  ngOnInit() {
  }

  shouldShowEllipsisInTooltip() {
    return this.batchTypes.length > this.batchTypesTooltipListLimit;
  }
}

export class DecisionTreeAssignmentsCellViewFactory implements CellViewFactory<DecisionTree> {
  create(entry: DecisionTree): View {
    return {
      component: DecisionTreeAssignmentsViewComponent,
      data: <DecisionTreeAssignmentsViewData> {
        assignments: entry.assignments,
        activations: entry.activations,
        decisionTreeId: entry.id
      }
    };
  }
}

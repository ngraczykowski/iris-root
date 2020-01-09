import { Component, OnInit } from '@angular/core';
import { TableDataMapperConfiguration } from '@app/components/dynamic-view-table/table-data-mapper';
import { DecisionTree } from '@model/decision-tree.model';
import { DecisionTreeTableBaseComponent } from './base/decision-tree-table-base.component';
import { DecisionTreeAssignmentsCellViewFactory } from './views/decision-tree-assignments-view/decision-tree-assignments-view.component';
import { DecisionTreeIdCellViewFactory } from './views/decision-tree-id-view/decision-tree-id-view.component';
import { DecisionTreeLabelViewFactory } from './views/decision-tree-label-view/decision-tree-label-view.component';
import { DecisionTreeNameCellViewFactory } from './views/decision-tree-name-view/decision-tree-name-view.component';
import { DecisionTreeOperationsCellViewFactory } from './views/decision-tree-operations-view/decision-tree-operations-view.component';
import { DecisionTreeStatusCellViewFactory } from './views/decision-tree-status-view/decision-tree-status-view.component';

const config: TableDataMapperConfiguration<DecisionTree> = {
  columnDefinitions: [
    {
      labelFactory: new DecisionTreeLabelViewFactory('decisionTrees.table.labels.id'),
      cellFactory: new DecisionTreeIdCellViewFactory()
    },
    {
      labelFactory: new DecisionTreeLabelViewFactory('decisionTrees.table.labels.name'),
      cellFactory: new DecisionTreeNameCellViewFactory()
    },
    {
      labelFactory: new DecisionTreeLabelViewFactory('decisionTrees.table.labels.status'),
      cellFactory: new DecisionTreeStatusCellViewFactory()
    },
    {
      labelFactory: new DecisionTreeLabelViewFactory('decisionTrees.table.labels.assignedTo'),
      cellFactory: new DecisionTreeAssignmentsCellViewFactory()
    },
    {
      labelFactory: new DecisionTreeLabelViewFactory(''),
      cellFactory: new DecisionTreeOperationsCellViewFactory()
    }
  ]
};

@Component({
  selector: 'app-decision-trees-list-table',
  templateUrl: './base/decision-tree-table-base.component.html',
  styleUrls: ['./base/decision-tree-table-base.component.scss']
})
export class DecisionTreesListTableComponent extends DecisionTreeTableBaseComponent implements OnInit {

  constructor() {
    super(config);
  }

  ngOnInit() {
  }
}

import { Component, Input, OnInit } from '@angular/core';
import { CellViewFactory } from '../../../../../../components/dynamic-view-table/table-data-mapper';
import {
  DynamicComponent,
  View
} from '../../../../../../components/dynamic-view/dynamic-view.component';
import { DecisionTree, DecisionTreeStatus } from '../../../../../model/decision-tree.model';

export interface DecisionTreeStatusViewData {
  status: DecisionTreeStatus;
}

@Component({
  selector: 'app-decision-tree-status-view',
  templateUrl: './decision-tree-status-view.component.html',
  styleUrls: ['./decision-tree-status-view.component.scss']
})
export class DecisionTreeStatusViewComponent implements DynamicComponent, OnInit {

  @Input() data: DecisionTreeStatusViewData;

  constructor() { }

  ngOnInit() {
  }
}

export class DecisionTreeStatusCellViewFactory implements CellViewFactory<DecisionTree> {
  create(entry: DecisionTree): View {
    return {
      component: DecisionTreeStatusViewComponent,
      data: <DecisionTreeStatusViewData> {
        active: entry.active,
        status: entry.status
      }
    };
  }
}

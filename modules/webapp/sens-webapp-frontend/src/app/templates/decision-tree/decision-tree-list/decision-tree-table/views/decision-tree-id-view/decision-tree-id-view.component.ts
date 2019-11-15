import { Component, Input, OnInit } from '@angular/core';
import { CellViewFactory } from '../../../../../../components/dynamic-view-table/table-data-mapper';
import {
  DynamicComponent,
  View
} from '../../../../../../components/dynamic-view/dynamic-view.component';
import { DecisionTree } from '../../../../../model/decision-tree.model';

export interface DecisionTreeIdViewData {
  decisionTreeId: number;
}

@Component({
  selector: 'app-decision-tree-id-view',
  templateUrl: './decision-tree-id-view.component.html',
  styleUrls: ['./decision-tree-id-view.component.scss']
})
export class DecisionTreeIdViewComponent implements DynamicComponent, OnInit {

  @Input() data: DecisionTreeIdViewData;

  constructor() { }

  ngOnInit() {
  }

}

export class DecisionTreeIdCellViewFactory implements CellViewFactory<DecisionTree> {
  create(entry: DecisionTree): View {
    return  {
      component: DecisionTreeIdViewComponent,
      data: <DecisionTreeIdViewData> {
        decisionTreeId: entry.id
      }
    };
  }
}

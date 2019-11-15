import { Component, Input, OnInit } from '@angular/core';
import { CellViewFactory } from '../../../../../../components/dynamic-view-table/table-data-mapper';
import {
  DynamicComponent,
  View
} from '../../../../../../components/dynamic-view/dynamic-view.component';
import { DecisionTree, Model } from '../../../../../model/decision-tree.model';

export interface DecisionTreeModelViewData {
  model: Model;
}

@Component({
  selector: 'app-decision-tree-model-view',
  templateUrl: './decision-tree-model-view.component.html',
  styleUrls: ['./decision-tree-model-view.component.scss']
})
export class DecisionTreeModelViewComponent implements DynamicComponent, OnInit {

  @Input() data: DecisionTreeModelViewData;

  constructor() { }

  ngOnInit() {
  }

}

export class DecisionTreeModelCellViewFactory implements CellViewFactory<DecisionTree> {
  create(entry: DecisionTree): View {
    return {
      component: DecisionTreeModelViewComponent,
      data: <DecisionTreeModelViewData> {
        model: entry.model
      }
    };
  }
}

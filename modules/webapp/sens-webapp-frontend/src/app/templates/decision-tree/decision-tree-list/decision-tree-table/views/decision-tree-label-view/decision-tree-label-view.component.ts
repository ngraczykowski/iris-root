import { Component, Input, OnInit } from '@angular/core';
import { LabelViewFactory } from '../../../../../../components/dynamic-view-table/table-data-mapper';
import {
  DynamicComponent,
  View
} from '../../../../../../components/dynamic-view/dynamic-view.component';

export interface DecisionTreeLabelViewData {
  text: string;
  moreInfo?: string;
}

@Component({
  selector: 'app-decision-tree-label-view',
  templateUrl: './decision-tree-label-view.component.html',
  styleUrls: ['./decision-tree-label-view.component.scss']
})
export class DecisionTreeLabelViewComponent implements DynamicComponent, OnInit {

  @Input() data: DecisionTreeLabelViewData;

  constructor() { }

  ngOnInit() {
  }

}

export class DecisionTreeLabelViewFactory implements LabelViewFactory {

  constructor(private text: string, private moreInfo?: string) {}

  create(): View {
    return {
      component: DecisionTreeLabelViewComponent,
      data: <DecisionTreeLabelViewData> {
        text: this.text,
        moreInfo: this.moreInfo
      }
    };
  }
}

import { Component, Input, OnInit } from '@angular/core';
import { CellViewFactory } from '@app/components/dynamic-view-table/table-data-mapper';
import { View } from '@app/components/dynamic-view/dynamic-view.component';
import { DecisionTree } from '@model/decision-tree.model';
import { DecisionGroup, DecisionGroupListBuilder } from './decision-group-list-builder';

export interface DecisionTreeActivationsViewData {
  activations: string[];
  decisionTreeId: number;
}

@Component({
  selector: 'app-decision-tree-activations-view',
  templateUrl: './decision-tree-activations-view.component.html',
  styleUrls: ['./decision-tree-activations-view.component.scss']
})
export class DecisionTreeActivationsViewComponent implements OnInit {

  @Input()
  set data(data: DecisionTreeActivationsViewData) {
    this.decisionGroups = new DecisionGroupListBuilder(data.activations).build();
    this.link = `/decision-tree/${data.decisionTreeId}`;
  }

  decisionGroups: DecisionGroup[];
  link: string;

  readonly decisionGroupsTooltipListLimit = 7;
  readonly showTooltipForListLongerThan = 5;

  constructor() { }

  ngOnInit() {
  }

  shouldShowEllipsisInTooltip() {
    return this.decisionGroups.length > this.decisionGroupsTooltipListLimit;
  }
}

export class DecisionTreeActivationsCellViewFactory implements CellViewFactory<DecisionTree> {
  create(entry: DecisionTree): View {
    return {
      component: DecisionTreeActivationsViewComponent,
      data: <DecisionTreeActivationsViewData> {
        activations: entry.activations,
        decisionTreeId: entry.id
      }
    };
  }
}

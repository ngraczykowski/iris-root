import { Component } from '@angular/core';
import { DynamicComponent } from '@app/components/dynamic-view/dynamic-view.component';

@Component({
  selector: 'app-decision-tree-link-view',
  templateUrl: './decision-tree-link-view.component.html',
  styleUrls: ['./decision-tree-link-view.component.scss']
})
export class DecisionTreeLinkViewComponent implements DynamicComponent {

  data: InputData;

  constructor() { }
}

interface InputData {
  decisionTreeId: number;
  decisionTreeName: string;
}

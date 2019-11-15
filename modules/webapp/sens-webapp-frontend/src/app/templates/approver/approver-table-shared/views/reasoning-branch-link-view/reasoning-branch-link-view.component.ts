import { Component } from '@angular/core';
import { DynamicComponent } from '@app/components/dynamic-view/dynamic-view.component';

@Component({
  selector: 'app-reasoning-branch-link-view',
  templateUrl: './reasoning-branch-link-view.component.html',
  styleUrls: ['./reasoning-branch-link-view.component.scss']
})
export class ReasoningBranchLinkViewComponent implements DynamicComponent {

  data: InputData;

  constructor() { }

}

interface InputData {
  decisionTreeId: number;
  reasoningBranchId: number;
}

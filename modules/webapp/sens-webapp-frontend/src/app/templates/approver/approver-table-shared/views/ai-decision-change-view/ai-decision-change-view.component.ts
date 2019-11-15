import { Component } from '@angular/core';
import { Change as SolutionChange } from '@app/components/changelog/changelog.model';
import { DynamicComponent } from '@app/components/dynamic-view/dynamic-view.component';
import { Change } from '@app/templates/approver/approver.model';

@Component({
  selector: 'app-ai-decision-change-view',
  templateUrl: './ai-decision-change-view.component.html',
  styleUrls: ['./ai-decision-change-view.component.scss']
})
export class AiDecisionChangeViewComponent implements DynamicComponent {

  data: InputDataFormat;

  changeAsSolutionChange(): SolutionChange<string> {
    return {
      current: this.data.change.from,
      proposed: this.data.change.to
    };
  }

  constructor() { }

}

interface InputDataFormat {
  change: Change<string>;
}

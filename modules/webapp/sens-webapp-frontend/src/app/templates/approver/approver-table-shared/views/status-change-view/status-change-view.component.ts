import { Component } from '@angular/core';
import { Change as SolutionChange } from '@app/components/changelog/changelog.model';
import { DynamicComponent } from '@app/components/dynamic-view/dynamic-view.component';
import { Change } from '@app/templates/approver/approver.model';

@Component({
  selector: 'app-status-change-view',
  templateUrl: './status-change-view.component.html',
  styleUrls: ['./status-change-view.component.scss']
})
export class StatusChangeViewComponent implements DynamicComponent {

  data: InputDataFormat;

  get asAppStatusChangeSolutionView(): SolutionChange<boolean> {
    return {
      current: this.data.change.from,
      proposed: this.data.change.to
    };
  }

  constructor() { }

}

interface InputDataFormat {
  change: Change<boolean>;
}

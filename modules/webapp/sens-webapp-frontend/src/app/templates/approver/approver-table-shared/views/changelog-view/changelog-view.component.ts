import { Component, EventEmitter } from '@angular/core';
import { DynamicComponent } from '@app/components/dynamic-view/dynamic-view.component';
import { OpenChangelogEvent } from '@app/templates/approver/approver.store';

@Component({
  selector: 'app-changelog-view',
  templateUrl: './changelog-view.component.html',
  styleUrls: ['./changelog-view.component.scss']
})
export class ChangelogViewComponent implements DynamicComponent {

  data: InputData;

  constructor() { }

  openChangelog() {
    this.data.openChangelog.emit({
      decisionTreeId: this.data.decisionTreeId,
      matchGroupId: this.data.matchGroupId
    });
  }
}

interface InputData {
  decisionTreeId: number;
  matchGroupId: number;
  openChangelog: EventEmitter<OpenChangelogEvent>;
}

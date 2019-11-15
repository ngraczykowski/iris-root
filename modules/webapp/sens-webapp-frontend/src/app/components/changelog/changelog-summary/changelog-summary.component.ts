import { Component, Input, OnInit } from '@angular/core';
import { ChangelogView } from '../changelog.model';

@Component({
  selector: 'app-changelog-summary',
  templateUrl: './changelog-summary.component.html',
  styleUrls: ['./changelog-summary.component.scss']
})
export class ChangelogSummaryComponent implements OnInit {

  @Input() changelog: ChangelogView;
  @Input() reasoningBranchLink: string;

  constructor() { }

  ngOnInit() {
  }

  makeElementId() {
    return `${this.changelog.reasoningBranchId.decisionTreeId}-${this.changelog.reasoningBranchId.matchGroupId}`;
  }
}

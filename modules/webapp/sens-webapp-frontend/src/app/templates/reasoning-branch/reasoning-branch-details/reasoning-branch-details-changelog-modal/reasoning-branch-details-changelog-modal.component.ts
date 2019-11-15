import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';

@Component({
  selector: 'app-reasoning-branch-details-changelog-modal',
  templateUrl: './reasoning-branch-details-changelog-modal.component.html',
  styleUrls: ['./reasoning-branch-details-changelog-modal.component.scss']
})
export class ReasoningBranchDetailsChangelogModalComponent implements OnInit {

  constructor() { }

  @Input() branchDetails;
  @Input() showChangeLog;

  @Output() closeClick = new EventEmitter();

  decisionTreeId;
  matchGroupId;

  hideChangeLogModal() {
    this.closeClick.emit();
  }

  ngOnInit() {
    this.assignData();
  }

  assignData() {
    if (this.branchDetails) {
      this.decisionTreeId = this.branchDetails.decisionTreeInfo.id;
      this.matchGroupId = this.branchDetails.matchGroupId;
    }
  }
}

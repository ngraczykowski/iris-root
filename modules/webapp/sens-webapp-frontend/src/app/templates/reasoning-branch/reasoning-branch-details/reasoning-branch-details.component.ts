import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';

@Component({
  selector: 'app-reasoning-branch-details',
  templateUrl: './reasoning-branch-details.component.html',
  styleUrls: ['./reasoning-branch-details.component.scss']
})
export class ReasoningBranchDetailsComponent implements OnInit {

  constructor() { }

  @Input() branchDetails;

  @Output() refreshReviewStatus: EventEmitter<any> = new EventEmitter<any>();

  showChangeLog = false;

  ngOnInit() {
  }

  private refreshReview() {
    this.refreshReviewStatus.emit();
  }

  formatFullDate(timestamp): string {
    return new Date(timestamp).toISOString().replace(/[TZ]/gi, ' ').substr(0, 19);
  }

  formatDate(timestamp) {
    return new Date(timestamp).toISOString().substr(0, 10);
  }

  openChangelogModal() {
    this.showChangeLog = true;
  }
}

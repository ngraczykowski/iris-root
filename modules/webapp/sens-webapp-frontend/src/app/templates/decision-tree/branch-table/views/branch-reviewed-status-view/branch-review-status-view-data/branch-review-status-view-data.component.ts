import { Component, Input, OnInit } from '@angular/core';

@Component({
  selector: 'app-branch-review-status-view-data',
  templateUrl: './branch-review-status-view-data.component.html',
  styleUrls: ['./branch-review-status-view-data.component.scss']
})
export class BranchReviewStatusViewDataComponent implements OnInit {

  constructor() { }

  @Input() data;

  ngOnInit() {
  }

  formatDate(timestamp) {
    return new Date(timestamp).toISOString().substr(0, 10);
  }
}

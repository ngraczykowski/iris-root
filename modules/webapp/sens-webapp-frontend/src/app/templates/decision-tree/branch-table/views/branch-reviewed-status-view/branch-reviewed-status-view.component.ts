import { Component, Input, OnInit } from '@angular/core';
import { DynamicComponent } from '@app/components/dynamic-view/dynamic-view.component';

export interface BranchReviewedStatusViewData {
  reviewed: boolean;
  reviewedAt: string;
  reviewedBy: string;
}

@Component({
  selector: 'app-branch-reviewed-status-view',
  templateUrl: './branch-reviewed-status-view.component.html',
  styleUrls: ['./branch-reviewed-status-view.component.scss']
})
export class BranchReviewedStatusViewComponent implements DynamicComponent, OnInit {

  @Input()
  data: BranchReviewedStatusViewData;

  visibleLengthOfReviewerName = 12;

  constructor() { }

  ngOnInit() {
  }

  nameLength() {
    if (this.data.reviewedBy) {
      return this.data.reviewedBy.length;
    }
  }
}

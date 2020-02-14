import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-reasoning-branch-management-page',
  templateUrl: './reasoning-branch-management-page.component.html',
  styleUrls: ['./reasoning-branch-management-page.component.scss']
})
export class ReasoningBranchManagementPageComponent implements OnInit {

  // Temporary
  branchDetails = {
    branchId: '1-546',
    statuses: [
      {label: 'Active', active: false},
      {label: 'Inactive', active: true}
    ],
    aiSolutions: [
      {label: 'False Positive', active: false},
      {label: 'Potential True Positive', active: true},
      {label: 'No Decision', active: false}
    ],
  };

  constructor() { }

  ngOnInit() {}
}

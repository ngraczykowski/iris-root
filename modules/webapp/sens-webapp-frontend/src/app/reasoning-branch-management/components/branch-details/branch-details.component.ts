import { Component, Input, OnInit } from '@angular/core';

@Component({
  selector: 'app-branch-details',
  templateUrl: './branch-details.component.html',
  styleUrls: ['./branch-details.component.scss']
})
export class BranchDetailsComponent implements OnInit {

  @Input() branchDetails;

  constructor() { }

  ngOnInit() {
  }

  reset() {}

  submit() {}
}

import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';

@Component({
  selector: 'app-branch-details',
  templateUrl: './branch-details.component.html',
  styleUrls: ['./branch-details.component.scss']
})
export class BranchDetailsComponent implements OnInit {

  @Input() branchDetails;
  @Output() confirm = new EventEmitter();

  constructor() { }

  ngOnInit() {
  }

  reset() {}

  submit() {
    this.confirm.emit();
  }
}

import { Component, OnInit, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'app-load-branch',
  templateUrl: './load-branch.component.html',
  styleUrls: ['./load-branch.component.scss']
})
export class LoadBranchComponent implements OnInit {
  @Output() loadBranch = new EventEmitter();

  enteredID: string;
  validInput = true;

  basicIdVerification = new RegExp('^[1-9][0-9]*-?([1-9][0-9]*)?$');
  fullIdCheck = new RegExp('^[1-9][0-9]*-[1-9][0-9]*$');

  constructor() { }

  ngOnInit() { }

  onInput(value: string) {
    this.enteredID = value;
    this.validateInput(this.basicIdVerification);
  }

  validateInput(value) {
    if (this.enteredID) {
      this.validInput = value.test(this.enteredID);
    } else {
      this.validInput = true;
    }
  }

  onLoadBranchClicked() {
    if (this.validInput) {
      this.loadBranch.emit(this.enteredID);
    }
  }

}

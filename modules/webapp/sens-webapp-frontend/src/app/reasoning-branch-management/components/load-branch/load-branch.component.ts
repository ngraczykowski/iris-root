import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-load-branch',
  templateUrl: './load-branch.component.html',
  styleUrls: ['./load-branch.component.scss']
})
export class LoadBranchComponent implements OnInit {

  enteredID: string;
  validInput = true;

  basicIdVerification = new RegExp('[1-9]+|[1-9]+-+[0-9]+');
  fullIdCheck = new RegExp('[1-9]\d*-\d+');

  constructor() { }

  onInput(value: string) {
    this.enteredID = value;
    this.validateInput(this.basicIdVerification);
  }

  onPaste(event: ClipboardEvent) {
    this.enteredID = event.clipboardData.toString();
    this.validateInput(this.basicIdVerification);
  }

  validateInput(value) {
    if (this.enteredID) {
      this.validInput = value.test(this.enteredID);
    } else {
      this.validInput = true;
    }
  }

  ngOnInit() {
  }

}

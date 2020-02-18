import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-load-branch',
  templateUrl: './load-branch.component.html',
  styleUrls: ['./load-branch.component.scss']
})
export class LoadBranchComponent implements OnInit {

  enteredID: string;

  constructor() { }

  onInput(value: string) {
    this.enteredID = value;
  }

  onPaste(event: ClipboardEvent) {
    this.enteredID = event.clipboardData.toString();
  }

  ngOnInit() {
  }

}

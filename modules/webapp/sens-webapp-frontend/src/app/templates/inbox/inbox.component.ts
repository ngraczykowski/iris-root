import { Component, OnInit } from '@angular/core';
import { SolvedMessageTableDataProvider } from './solved-message-table-data-provider';
import { UnsolvedMessageTableDataProvider } from './unsolved-message-table-data-provider';

@Component({
  selector: 'app-inbox',
  templateUrl: './inbox.component.html',
  styleUrls: ['./inbox.component.scss']
})
export class InboxComponent implements OnInit {

  constructor(
      public unsolvedMessageTableDataProvider: UnsolvedMessageTableDataProvider,
      public solvedMessageTableDataProvider: SolvedMessageTableDataProvider) { }

  ngOnInit() {
  }
}

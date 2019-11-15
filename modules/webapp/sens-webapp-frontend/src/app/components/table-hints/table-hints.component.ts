import { Component, Input, OnInit } from '@angular/core';

@Component({
  selector: 'app-table-hint',
  templateUrl: './table-hints.component.html',
  styleUrls: ['./table-hints.component.scss']
})
export class TableHintsComponent implements OnInit {

  constructor() { }

  @Input() tableHints: string[];

  ngOnInit() {
  }

}

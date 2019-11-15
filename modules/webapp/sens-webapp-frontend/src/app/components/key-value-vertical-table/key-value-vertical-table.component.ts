import { Component, OnInit, Input } from '@angular/core';
import { TableHelper } from '../../shared/helpers/table-helper';

export interface KeyValueTableEntry {
  name: string;
  value: any;
}

@Component({
  selector: 'app-key-value-vertical-table',
  templateUrl: './key-value-vertical-table.component.html',
  styleUrls: ['./key-value-vertical-table.component.scss'],
  providers: [TableHelper]
})
export class KeyValueVerticalTableComponent implements OnInit {

  constructor(public tableHelper: TableHelper) { }

  @Input() entries: KeyValueTableEntry[];

  ngOnInit() {
  }
}

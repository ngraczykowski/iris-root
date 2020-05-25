import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-values-list',
  templateUrl: './values-list.component.html',
  styleUrls: ['./values-list.component.scss']
})
export class ValuesListComponent {

  @Input() valuesList;

  constructor() { }

}

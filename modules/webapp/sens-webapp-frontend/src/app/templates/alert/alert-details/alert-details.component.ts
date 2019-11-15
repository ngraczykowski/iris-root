import { Component, Input, OnInit } from '@angular/core';

@Component({
  selector: 'app-alert-details',
  templateUrl: './alert-details.component.html',
  styleUrls: ['./alert-details.component.scss']
})
export class AlertDetailsComponent implements OnInit {

  constructor() { }

  @Input() partyFields;

  ngOnInit() {
  }

}

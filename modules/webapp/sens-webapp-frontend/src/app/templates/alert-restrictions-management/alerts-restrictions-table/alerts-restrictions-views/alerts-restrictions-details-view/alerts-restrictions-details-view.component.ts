import { Component, Input, OnInit } from '@angular/core';

@Component({
  selector: 'app-alerts-restrictions-details-view',
  templateUrl: './alerts-restrictions-details-view.component.html',
  styleUrls: ['./alerts-restrictions-details-view.component.scss']
})
export class AlertsRestrictionsDetailsViewComponent implements OnInit {
  @Input() data;

  constructor() { }

  ngOnInit() {
  }

  hasData() {
    return this.data.data.join().length > 0;
  }

}

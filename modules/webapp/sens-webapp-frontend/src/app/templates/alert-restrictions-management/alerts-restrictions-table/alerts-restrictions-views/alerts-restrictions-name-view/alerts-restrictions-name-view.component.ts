import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'app-alerts-restrictions-name-view',
  templateUrl: './alerts-restrictions-name-view.component.html',
  styleUrls: ['./alerts-restrictions-name-view.component.scss']
})
export class AlertsRestrictionsNameViewComponent implements OnInit {
  @Input() data;
  constructor() { }

  ngOnInit() {
  }

}

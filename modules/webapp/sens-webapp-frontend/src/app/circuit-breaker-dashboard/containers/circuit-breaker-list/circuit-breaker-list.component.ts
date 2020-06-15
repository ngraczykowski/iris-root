import { Component, Input, OnInit } from '@angular/core';
import { StateContent } from '@app/ui-components/state/state';

@Component({
  selector: 'app-circuit-breaker-list',
  templateUrl: './circuit-breaker-list.component.html',
  styleUrls: ['./circuit-breaker-list.component.scss']
})
export class CircuitBreakerListComponent implements OnInit {

  translatePrefix = 'circuitBreakerDashboard.element.';
  loadingTranslatePrefix = this.translatePrefix + 'loading.';

  @Input() circuitBreakerList;

  stateLoading: StateContent = {
    title: this.loadingTranslatePrefix + 'title',
    inProgress: true
  };

  loadingDetails = true;
  showDetails = true;

  constructor() { }

  ngOnInit() {
  }

}

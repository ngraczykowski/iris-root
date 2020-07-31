import { Component } from '@angular/core';
import { CircuitBreakerService } from '@app/circuit-breaker-dashboard/services/circuit-breaker.service';
import { Header } from '@app/ui-components/header/header';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-circuit-breaker-dashboard',
  templateUrl: './circuit-breaker-dashboard.component.html',
  styleUrls: ['./circuit-breaker-dashboard.component.scss']
})
export class CircuitBreakerDashboardComponent {

  translatePrefix = 'circuitBreakerDashboard.';
  tabTranslatePrefix = this.translatePrefix + 'tabs.';

  activeDiscrepantBranchesCount: Observable<number>;

  tabNavigationLinks = [
    {
      name: 'active',
      link: './active',
    }, {
      name: 'archived',
      link: './archived',
    }
  ];

  header: Header = {
    title: this.translatePrefix + 'title'
  };

  constructor(private service: CircuitBreakerService) {
    this.activeDiscrepantBranchesCount = service.getActiveDiscrepantBranchesCount();

  }
}

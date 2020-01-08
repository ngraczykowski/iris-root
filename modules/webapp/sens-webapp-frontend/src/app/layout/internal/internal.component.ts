import { Component, OnDestroy, OnInit } from '@angular/core';
import { BackgroundServicesManager } from '@app/shared/background-services-manager';
import { LocalEventService } from '@app/shared/event/local-event.service';
import { SolutionSettingsService } from '@app/shared/solution-settings.service';

@Component({
  selector: 'app-internal',
  templateUrl: './internal.component.html',
  styleUrls: ['./internal.component.scss']
})
export class InternalComponent implements OnInit, OnDestroy {

  constructor(
      private readonly backgroundServicesManager: BackgroundServicesManager,
      private readonly localEventService: LocalEventService,
      private readonly solutionSettingsService: SolutionSettingsService,
  ) { }

  ngOnInit() {
    this.backgroundServicesManager.startService();
    this.solutionSettingsService.getSolutionSettings();
  }

  ngOnDestroy(): void {
    this.backgroundServicesManager.stopService();
  }
}

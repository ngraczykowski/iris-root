import { Component, HostListener, OnDestroy, OnInit } from '@angular/core';
import { BackgroundServicesManager } from '@app/shared/background-services-manager';
import { EventKey } from '@app/shared/event/event.service.model';
import { LocalEventService } from '@app/shared/event/local-event.service';
import { TranslateService } from '@ngx-translate/core';
import { SolutionSettingsService } from './shared/solution-settings.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit, OnDestroy {

  constructor(
      private translate: TranslateService,
      private backgroundServicesManager: BackgroundServicesManager,
      private localEventService: LocalEventService,
      private solutionSettingsService: SolutionSettingsService
  ) {
    translate.setDefaultLang('en');
    translate.use('en');
  }

  ngOnInit() {
    this.backgroundServicesManager.startService();
    this.solutionSettingsService.getSolutionSettings();
  }

  ngOnDestroy() {
    this.backgroundServicesManager.stopService();
  }

  @HostListener('click', ['$event.target'])
  onClickBtn(target) {
    const onButtonClickEvent = {key: EventKey.CLICK, data: {'target': target}};
    this.localEventService.sendEvent(onButtonClickEvent);
  }

  @HostListener('document:keypress', ['$event'])
  handleKeyboardEvent(event: KeyboardEvent) {
    const onKeyboardClickEvent = {key: EventKey.KEY_PRESS, data: {'event': event}};
    this.localEventService.sendEvent(onKeyboardClickEvent);
  }

  onActivate(event) {
    const scrollToTop = window.setInterval(() => {
      const pos = window.pageYOffset;
      if (pos > 0) {
        window.scrollTo(0, pos - 20);
      } else {
        window.clearInterval(scrollToTop);
      }
    }, 16);
  }
}

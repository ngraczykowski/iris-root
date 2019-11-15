import { Component, Input, OnInit } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { FrontendSettings } from '../../../../../shared/frontend-settings/frontend-settings.model';
import { FrontendSettingsService } from '../../../../../shared/frontend-settings/frontend-settings.service';

@Component({
  selector: 'app-branch-limit-info',
  templateUrl: './branch-limit-info.component.html',
  styleUrls: ['./branch-limit-info.component.scss']
})
export class BranchLimitInfoComponent implements OnInit {

  @Input()
  set total(total) {
    this._total = total;
  }

  private _total: number;

  show: boolean;
  message: string;

  constructor(private frontendSettings: FrontendSettingsService, private translateService: TranslateService) { }

  ngOnInit() {
    this.load();
  }

  private load() {
    this.frontendSettings.getSettings()
        .subscribe(settings => this.onLoadSettings(settings));
  }

  private onLoadSettings(settings: FrontendSettings) {
  }
}

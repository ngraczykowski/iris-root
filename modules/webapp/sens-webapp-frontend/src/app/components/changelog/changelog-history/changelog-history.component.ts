import { Component, Input, OnInit } from '@angular/core';
import { SolutionSettingsService } from '@app/shared/solution-settings.service';
import { TranslateService } from '@ngx-translate/core';
import { Changelog, ChangelogView } from '../changelog.model';

@Component({
  selector: 'app-changelog-history',
  templateUrl: './changelog-history.component.html',
  styleUrls: ['./changelog-history.component.scss']
})
export class ChangelogHistoryComponent implements OnInit {

  @Input()
  set changelog(changelog: ChangelogView) {
    this._changelog = changelog;
    this.refreshChanges();
  }

  @Input() solutionsList;

  get changelog() {
    return this._changelog;
  }

  private _changelog: ChangelogView;

  changeStatusHtml: string;
  changeSolutionHtml: string;

  constructor(private translate: TranslateService,
              private solutionSettingsService: SolutionSettingsService) { }

  ngOnInit() {
  }

  formatDate(timestamp: number): string {
    return new Date(timestamp).toLocaleDateString();
  }

  formatOpenedByHtml(change: Changelog) {
    return this.translate.instant('changelog.history.openedBy', {userName: change.userName});
  }

  private refreshChanges() {
    if (this.changelog.statusChange) {
      this.changeStatusHtml = this.formatChangeStatusHtml();
    }
    if (this.changelog.solutionChange) {
      this.changeSolutionHtml = this.formatChangeSolutionHtml();
    }
  }

  private formatChangeStatusHtml() {
    return this.translate.instant('changelog.history.changeStatus',
        {
          current: this.translateStatus(this.changelog.statusChange.current),
          proposed: this.translateStatus(this.changelog.statusChange.proposed)
        });
  }

  private formatChangeSolutionHtml() {
    return this.translate.instant('changelog.history.changeSolution',
        {
          current: this.solutionSettingsService.getSolutionLabel(this.changelog.solutionChange.current),
          proposed: this.solutionSettingsService.getSolutionLabel(this.changelog.solutionChange.proposed)
        });
  }

  private translateStatus(status: boolean): string {
    return this.translate.instant('changelog.history.status.' + (status ? 'true' : 'false'));
  }
}

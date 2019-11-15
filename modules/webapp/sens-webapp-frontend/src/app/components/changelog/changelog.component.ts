import { Component, Input, OnInit } from '@angular/core';
import { finalize } from 'rxjs/operators';
import { ChangelogClient } from './changelog-client';
import { ChangelogView } from './changelog.model';

@Component({
  selector: 'app-changelog',
  templateUrl: './changelog.component.html',
  styleUrls: ['./changelog.component.scss']
})
export class ChangelogComponent implements OnInit {

  @Input()
  decisionTreeId: number;

  @Input()
  matchGroupId: number;

  inProgress = true;
  changelog: ChangelogView;
  reasoningBranchLink: string;

  constructor(private changelogClient: ChangelogClient) { }

  ngOnInit() {
    this.loadChangelog();
  }

  private loadChangelog() {
    this.changelogClient
        .getChangelog(this.decisionTreeId, this.matchGroupId)
        .pipe(finalize(() => this.inProgress = false))
        .subscribe(
            changelog => this.onLoadSuccess(changelog),
            error => this.onLoadError(error)
        );
  }

  private onLoadSuccess(changelog) {
    this.changelog = changelog;
    this.reasoningBranchLink = `/decision-tree/${changelog.reasoningBranchId.decisionTreeId}` +
        `/reasoning-branch/${changelog.reasoningBranchId.matchGroupId}`;
  }

  private onLoadError(error) {
    console.log('Error during fetching changelog', error);
    if (error.status === 404) {
      this.changelog = null;
    }
  }
}

import { Component, OnInit } from '@angular/core';
import { MatBottomSheet } from '@angular/material/bottom-sheet';
import { CreateChangeRequestComponent } from '@app/reasoning-branch-browser/components/create-change-request/create-change-request.component';
import { Header } from '@app/ui-components/header/header';
import { StateContent } from '@app/ui-components/state/state';

@Component({
  selector: 'app-reasoning-branches-list-container',
  templateUrl: './reasoning-branches-list-container.component.html',
  styleUrls: ['./reasoning-branches-list-container.component.scss']
})
export class ReasoningBranchesListContainerComponent implements OnInit {

  translatePrefix = 'reasoningBranchBrowser.';
  loadingTranslatePrefix = this.translatePrefix + 'loading.';
  errorTranslatePrefix = this.translatePrefix + 'error.';

  header: Header = {
    title: this.translatePrefix + 'title'
  };

  stateLoading: StateContent = {
    centered: true,
    title: this.loadingTranslatePrefix + 'title',
    inProgress: true
  };

  stateError: StateContent = {
    centered: true,
    title: this.errorTranslatePrefix + 'title',
    description: this.errorTranslatePrefix + 'description',
    button: this.errorTranslatePrefix + 'button'
  };

  constructor(private _bottomSheet: MatBottomSheet) { }

  ngOnInit() {
    this.openBottomSheet();
  }

  openBottomSheet(): void {
    this._bottomSheet.open(CreateChangeRequestComponent,
        {
          disableClose: true,
          hasBackdrop: false
        }
    );
  }

}

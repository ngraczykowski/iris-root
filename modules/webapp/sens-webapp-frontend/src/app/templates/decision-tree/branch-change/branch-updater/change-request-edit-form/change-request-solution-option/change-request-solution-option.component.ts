import { Component, Input, OnInit } from '@angular/core';
import { AbstractControl } from '@angular/forms';
import { Solution } from '@app/components/solution-tag/solution-tag.component';
import { SolutionSettingsService } from '@app/shared/solution-settings.service';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-change-request-solution-option',
  templateUrl: './change-request-solution-option.component.html',
  styleUrls: ['./change-request-solution-option.component.scss']
})
export class ChangeRequestSolutionOptionComponent implements OnInit {

  @Input() control: AbstractControl;

  solutionsList: Solution[];

  private solutionServiceSubscription: Subscription;

  constructor(
      private solutionSettingsService: SolutionSettingsService
  ) { }

  getSolutionsPresets() {
    this.solutionServiceSubscription = this.solutionSettingsService.settings.subscribe((data: Solution[]) => {
      this.solutionsList = data;
    });
  }

  ngOnInit() {
    this.getSolutionsPresets();
  }
}

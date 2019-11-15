import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { SolutionSettingsService } from '@app/shared/solution-settings.service';
import { Subscription } from 'rxjs';

export interface Solution {
  key: string;
  label: string;
  className: string;
}

@Component({
  selector: 'app-solution-tag',
  templateUrl: './solution-tag.component.html',
  styleUrls: ['./solution-tag.component.scss']
})
export class SolutionTagComponent implements OnInit, OnDestroy {
  @Input() solution;

  label: string;
  className: string;

  private solutionServiceSubscription: Subscription;

  constructor(
    private solutionSettingsService: SolutionSettingsService
  ) { }

  ngOnInit() {
    this.getSolutionsPresets();
  }

  ngOnDestroy() {
    this.solutionServiceSubscription.unsubscribe();
  }

  getSolutionsPresets() {
    this.solutionServiceSubscription = this.solutionSettingsService.settings.subscribe((data: Solution[]) => {
      if (data !== null) {
        const solutionProperties = data.find(i => i.key === this.solution);

        if (solutionProperties) {
          this.label = solutionProperties.label.toString();
          this.className = `${solutionProperties.className}`;
        }
      }
    });
  }
}

import { ChangeDetectionStrategy, Component, Input, OnChanges } from '@angular/core';

@Component({
  selector: 'app-ai-solution',
  templateUrl: './ai-solution.component.html',
  styleUrls: ['./ai-solution.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class AiSolutionComponent implements OnChanges {

  @Input() aiSolution: string;

  aiSolutionStyle: string;

  constructor() { }

  ngOnChanges() {
    this.generateAiSolutionStyle(this.aiSolution);
  }

  generateAiSolutionStyle(aiSolution) {
    const solutionStyle = aiSolution.toLowerCase().replace(/_/g, '-');
    this.aiSolutionStyle = `ai-solution--${solutionStyle}`;
  }

}

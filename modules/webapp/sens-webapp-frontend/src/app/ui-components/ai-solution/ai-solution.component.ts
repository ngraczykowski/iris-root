import { Component, Input, OnChanges } from '@angular/core';
import { environment } from '@env/environment';

@Component({
  selector: 'app-ai-solution',
  templateUrl: './ai-solution.component.html',
  styleUrls: ['./ai-solution.component.scss']
})
export class AiSolutionComponent implements OnChanges {

  @Input() aiSolution: string;

  aiSolutionLabel: string;
  aiSolutionStyle: string;

  constructor() { }

  ngOnChanges() {
    this.convertSolution(this.aiSolution);
    this.generateAiSolutionStyle(this.aiSolution);
  }

  generateAiSolutionStyle(aiSolution) {
    const solutionStyle = aiSolution.toLowerCase().replace(/_/g, '-');
    this.aiSolutionStyle = `ai-solution--${solutionStyle}`;
  }

  convertSolution(solution) {
    const aiSolutions = environment.aiSolutions;

    aiSolutions.forEach(aiSolution => {
      if (aiSolution.value === solution) {
        this.aiSolutionLabel = `${aiSolution.label}`;
      }
    });
  }
}

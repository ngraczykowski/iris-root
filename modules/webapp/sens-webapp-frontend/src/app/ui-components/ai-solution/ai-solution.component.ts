import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-ai-solution',
  templateUrl: './ai-solution.component.html',
  styleUrls: ['./ai-solution.component.scss']
})
export class AiSolutionComponent {

  @Input() aiSolution: string;

  constructor() { }

  generateAiSolutionStyle(aiSolution) {
    const solutionStyle = aiSolution.toLowerCase().split(' ').join('-');
    return `ai-solution--${solutionStyle}`;
  }
}

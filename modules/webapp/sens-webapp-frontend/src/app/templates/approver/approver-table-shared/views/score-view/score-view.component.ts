import { Component } from '@angular/core';
import { DynamicComponent } from '@app/components/dynamic-view/dynamic-view.component';

@Component({
  selector: 'app-score-view',
  templateUrl: './score-view.component.html',
  styleUrls: ['./score-view.component.scss']
})
export class ScoreViewComponent implements DynamicComponent {

  data: InputData;

  constructor() { }

}

interface InputData {
  score: number;
}

import { ChangeDetectionStrategy, Component } from '@angular/core';

@Component({
  selector: 'app-panel-layout',
  templateUrl: './panel-layout.component.html',
  styleUrls: ['./panel-layout.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class PanelLayoutComponent {}

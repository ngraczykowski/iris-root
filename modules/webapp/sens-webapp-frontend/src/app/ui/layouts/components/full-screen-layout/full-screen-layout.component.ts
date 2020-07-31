import { ChangeDetectionStrategy, Component } from '@angular/core';

@Component({
  selector: 'app-full-screen-layout',
  templateUrl: './full-screen-layout.component.html',
  styleUrls: ['./full-screen-layout.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class FullScreenLayoutComponent {}

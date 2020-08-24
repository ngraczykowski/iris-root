import { ChangeDetectionStrategy, Component, Input } from '@angular/core';

@Component({
  selector: 'app-loading-button-layout',
  templateUrl: './loading-button-layout.component.html',
  styleUrls: ['./loading-button-layout.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class LoadingButtonLayoutComponent {
  @Input() spinner: boolean;
}

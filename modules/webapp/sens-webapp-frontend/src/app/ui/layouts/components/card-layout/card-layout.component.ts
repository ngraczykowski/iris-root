import { ChangeDetectionStrategy, Component, HostBinding, Input } from '@angular/core';

@Component({
  selector: 'app-card-layout',
  templateUrl: './card-layout.component.html',
  styleUrls: ['./card-layout.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class CardLayoutComponent {

  @HostBinding('class.card-layout--centered')
  @Input()
  centered: boolean = false;

}

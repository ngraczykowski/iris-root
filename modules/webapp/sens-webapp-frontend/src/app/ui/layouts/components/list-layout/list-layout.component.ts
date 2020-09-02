import { ChangeDetectionStrategy, Component, HostBinding } from '@angular/core';

@Component({
  selector: 'app-list-layout',
  templateUrl: './list-layout.component.html',
  styleUrls: ['./list-layout.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class ListLayoutComponent {
  @HostBinding('class.list-layout') true;
}

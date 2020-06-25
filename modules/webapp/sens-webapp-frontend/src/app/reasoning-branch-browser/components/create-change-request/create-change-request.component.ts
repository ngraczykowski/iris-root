import { Component } from '@angular/core';
import { MatBottomSheetRef } from '@angular/material/bottom-sheet';

@Component({
  selector: 'app-create-change-request',
  templateUrl: './create-change-request.component.html',
  styleUrls: ['./create-change-request.component.scss']
})
export class CreateChangeRequestComponent {
  constructor(private _bottomSheetRef: MatBottomSheetRef<CreateChangeRequestComponent>) {}

  rbCount = 3;

  openLink(event: MouseEvent): void {
    this._bottomSheetRef.dismiss();
    event.preventDefault();
  }
}

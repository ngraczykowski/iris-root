import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatBottomSheetModule } from '@angular/material';
import { BottomSheetService } from '@ui/bottom-sheet/services/bottom-sheet.service';

@NgModule({
  declarations: [],
  imports: [
      CommonModule,
      MatBottomSheetModule,
  ],
  providers: [BottomSheetService]
})
export class BottomSheetModule { }

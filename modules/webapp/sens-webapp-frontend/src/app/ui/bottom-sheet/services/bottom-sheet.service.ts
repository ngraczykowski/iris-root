import { ComponentType } from '@angular/cdk/portal';
import { Injectable } from '@angular/core';
import { MatBottomSheet, MatBottomSheetConfig, MatBottomSheetRef } from '@angular/material';

@Injectable({
  providedIn: 'root'
})
export class BottomSheetService {

  constructor(private matBottomSheet: MatBottomSheet) { }

  public openLarge<T, D = any, R = any>(component: ComponentType<T>, config?: MatBottomSheetConfig<D>): MatBottomSheetRef<T, R> {
    return this.matBottomSheet.open(component, config);
  }
}

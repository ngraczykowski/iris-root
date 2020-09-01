import { Injectable } from '@angular/core';
import { MatSnackBar, MatSnackBarConfig, MatSnackBarRef, SimpleSnackBar } from '@angular/material';

@Injectable({
  providedIn: 'root'
})
export class SnackbarService {

  private defaultConfig: MatSnackBarConfig = {
    duration: 5000,
  };

  constructor(private matSnackbar: MatSnackBar) { }

  public open(message: string, action?: string, config?: MatSnackBarConfig):
      MatSnackBarRef<SimpleSnackBar> {
    return this.matSnackbar.open(message, action, config || this.defaultConfig);
  }
}

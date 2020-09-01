import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatSnackBarModule } from '@angular/material';
import { SnackbarService } from '@ui/snackbar/services/snackbar.service';

@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    MatSnackBarModule
  ],
  providers: [SnackbarService]
})
export class SnackbarModule { }

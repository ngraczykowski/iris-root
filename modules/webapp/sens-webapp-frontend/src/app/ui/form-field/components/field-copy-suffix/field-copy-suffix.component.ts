import { ChangeDetectionStrategy, Component, Input, OnInit } from '@angular/core';
import { MatFormField } from '@angular/material';
import { SnackbarService } from '@ui/snackbar/services/snackbar.service';

@Component({
  selector: 'app-field-copy-suffix',
  templateUrl: './field-copy-suffix.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class FieldCopySuffixComponent implements OnInit {

  @Input() public copyOnInit: boolean = false;

  constructor(private matFormField: MatFormField, private snackbarService: SnackbarService) {}

  ngOnInit(): void {
    if (this.copyOnInit && this.matFormField._control.value) {
      this.copy();
    }
  }

  copy(): void {
    if (this.matFormField && this.matFormField._control && this.matFormField._control.value
      && navigator && navigator.clipboard && navigator.clipboard.writeText) {
      navigator.clipboard.writeText(this.matFormField._control.value).then(() => {
        this.snackbarService.open('Password copied.');
      });
    }
  }
}

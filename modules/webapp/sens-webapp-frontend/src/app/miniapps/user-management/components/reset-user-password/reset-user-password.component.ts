import { ChangeDetectorRef, Component, Input } from '@angular/core';
import { UserManagementService } from '@core/user-managemenet/services/user-management.service';
import { ResetUserPasswordResponse } from '@endpoint/user-management/model/reset-user-password-response';
import { UserManagementListItem } from '@endpoint/user-management/model/user-management-list-item';
import { SkidAnimation } from '@ui/animation/triggers/skid.animation';
import { finalize } from 'rxjs/operators';

@Component({
  selector: 'app-reset-user-password',
  templateUrl: './reset-user-password.component.html',
  styleUrls: ['./reset-user-password.component.scss'],
  animations: [SkidAnimation.inOut(), SkidAnimation.hide()]
})
export class ResetUserPasswordComponent {

  @Input() user: UserManagementListItem;

  confirmation: boolean;
  loading: boolean;
  response: ResetUserPasswordResponse;

  constructor(private userManagementService: UserManagementService,
              private cdr: ChangeDetectorRef) { }

  reset(): void {
    this.loading = true;
    this.response = null;
    this.userManagementService.resetPassword(this.user.userName).pipe(
        finalize(() => {
          this.loading = false;
          this.confirmation = false;
        }))
      .subscribe((response: ResetUserPasswordResponse) => {
        this.response = response;
        this.cdr.markForCheck();
      });
  }

}

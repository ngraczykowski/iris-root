import { animate, state, style, transition, trigger } from '@angular/animations';
import { Component, Inject } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { MAT_BOTTOM_SHEET_DATA, MatBottomSheetRef } from '@angular/material';
import {
  EditUserForm,
  EditUserFormService
} from '@app/miniapps/user-management/services/edit-user-form.service';
import { Authority } from '@core/authorities/model/authority.enum';
import { UserManagementService } from '@core/user-managemenet/services/user-management.service';
import { UpdateUserRequest } from '@endpoint/user-management/model/update-user-request';
import { ExpanseAnimations } from '@ui/animation/triggers/expanse.animations';
import { FadeAnimation } from '@ui/animation/triggers/fade.animation';
import { SkidAnimation } from '@ui/animation/triggers/skid.animation';
import { Observable, of } from 'rxjs';
import { finalize, map, switchMap } from 'rxjs/operators';

@Component({
  selector: 'app-edit-user-form',
  templateUrl: './edit-user-form.component.html',
  styleUrls: ['./edit-user-form.component.scss'],
  providers: [EditUserFormService],
  animations: [ExpanseAnimations.hide(), ExpanseAnimations.inOut(), ExpanseAnimations.passing(),
    SkidAnimation.inOut(), SkidAnimation.hide(), FadeAnimation.passing(), FadeAnimation.inOut()]
})
export class EditUserFormComponent {

  public authorityKeys: any = {
    [Authority.ADMIN]: 'ADMIN',
    [Authority.ADMINISTRATOR]: 'ADMINISTRATOR',
    [Authority.ANALYST]: 'ANALYST',
    [Authority.BUSINESS_OPERATOR]: 'BUSINESS_OPERATOR',
    [Authority.AUDITOR]: 'AUDITOR',
    [Authority.APPROVER]: 'APPROVER',
  };

  public form: FormGroup;

  public authority: typeof Authority = Authority;

  public authorities: string[] = this.editUserFormService.roles
      .filter((role: string) => role !== Authority.ADMIN);
  public showAllRoles: Observable<boolean>;

  public sending: boolean;

  constructor(private editUserFormService: EditUserFormService,
              private userManagementService: UserManagementService,
              private matBottomSheetRef: MatBottomSheetRef,
              @Inject(MAT_BOTTOM_SHEET_DATA) public data: EditUserForm) {
      this.form = this.editUserFormService.build(data);
      this.showAllRoles = this.editUserFormService.allRolesSelected().pipe(
          map((allSelected: boolean) => !allSelected )
      );
  }

  close(): void {
    this.matBottomSheetRef.dismiss();
  }

  accept(): void {
    if (this.form.valid) {
      this.sending = true;
      of(this.form.value).pipe(
          switchMap((payload: UpdateUserRequest) => this.data ?
              this.userManagementService.updateUser(payload) :
              this.userManagementService.createUser(payload)),
          finalize(() => this.sending = false))
        .subscribe(() => this.matBottomSheetRef.dismiss(true)
      );
    } else {
      this.form.markAllAsTouched();
    }
  }

  generatePassword(): void {
    this.editUserFormService.generatePassword();
  }

}

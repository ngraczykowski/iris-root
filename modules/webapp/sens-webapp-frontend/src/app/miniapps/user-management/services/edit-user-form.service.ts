import { Injectable, OnDestroy } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { LifecycleHelper } from '@app/shared/helpers/lifecycle-helper';
import { Authority } from '@core/authorities/model/authority.enum';
import { UserManagementListService } from '@core/user-managemenet/services/user-management-list.service';
import { UniqueUserNameValidator } from '@core/user-managemenet/validators/unique-username.validator';
import { Observable } from 'rxjs';
import { map, pairwise, startWith, takeUntil } from 'rxjs/operators';

export interface EditUserForm {
  userName: string;
  displayName: string;
  password: string;
  origin?: string;
  roles: Authority[];
}

@Injectable()
export class EditUserFormService implements OnDestroy {

  private static formModel = {
    userName: [null, Validators.compose([
      Validators.required,
      Validators.minLength(3),
      Validators.maxLength(30),
      Validators.pattern(/^[a-zA-Z0-9\-\_\@]*$/)])],
    displayName: [null, Validators.compose([
      Validators.minLength(3),
      Validators.maxLength(50),
    ])],
    password: [null, Validators.compose([
      Validators.required,
      Validators.minLength(8),
      Validators.pattern(/\d/),
      Validators.pattern(/[a-zA-Z]/)
    ])],
    roles: [[]]
  };

  public roles: string[] = [Authority.ADMIN, Authority.ADMINISTRATOR, Authority.ANALYST,
    Authority.BUSINESS_OPERATOR, Authority.AUDITOR, Authority.APPROVER];

  private lifecycle: LifecycleHelper = new LifecycleHelper();

  private form: FormGroup;

  constructor(private formBuilder: FormBuilder,
              private userManagementListService: UserManagementListService) { }

  public build(initData: EditUserForm = null): FormGroup {
    this.form = this.formBuilder.group(EditUserFormService.formModel);

    if (initData) {
      this.form.get('password').clearValidators();
      this.form.patchValue(initData as any);
    }

    const initUserName: string = initData && initData.userName ? initData.userName : null;
    this.form.get('userName').setAsyncValidators(
        [UniqueUserNameValidator(this.userManagementListService, initUserName).bind(this)]);

    this.acquireRolesManagement();
    return this.form;
  }

  public allRolesSelected(): Observable<boolean> {
    return this.getRolesSelected().pipe(
        map((selections: string[]) => selections.length === this.roles.length)
    );
  }

  public ngOnDestroy(): void {
    this.lifecycle.destroy();
  }

  private getRolesSelected(): Observable<string[]> {
    const rolesControl: AbstractControl = this.form.get('roles');
    return rolesControl.valueChanges.pipe(startWith(rolesControl.value as string[]));
  }

  private acquireRolesManagement(): void {
    this.getRolesSelected().pipe(
        pairwise(),
        takeUntil(this.lifecycle.destroyed)
    ).subscribe(([prev, next]) => {
      const allRolesSelected: boolean = next.length === this.roles.length;
        if (next.includes(Authority.ADMIN) && !prev.includes(Authority.ADMIN) && !allRolesSelected) {
          setTimeout(() => {
            this.form.get('roles').setValue([...this.roles]);
          });
        }
    });
  }

}

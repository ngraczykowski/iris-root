import { AbstractControl, AsyncValidatorFn } from '@angular/forms';
import { UserManagementListService } from '@core/user-managemenet/services/user-management-list.service';
import { UserManagementListItem } from '@endpoint/user-management/model/user-management-list-item';
import { of, timer } from 'rxjs';
import { filter, first, map, switchMap } from 'rxjs/operators';

export function UniqueUserNameValidator(userManagementListService: UserManagementListService,
                                        currentUserName: string): AsyncValidatorFn {
  return (control: AbstractControl) => {
    const value = control && control.value && control && control.value !== currentUserName
        ? control.value : null;

    if (!value) {
      return of(value);
    }

    return timer(500).pipe(
        switchMap(() => userManagementListService.getUsers().pipe(
            filter(Boolean),
            map((users: UserManagementListItem[]) => {
              const match: UserManagementListItem = users.find(
                  (listItem: UserManagementListItem) => value === listItem.userName);
              return !match;
            }),
            map((unique: boolean) => unique ? null : {usernameInUsage: true}),
            first(),
        )),

    );
  };
}

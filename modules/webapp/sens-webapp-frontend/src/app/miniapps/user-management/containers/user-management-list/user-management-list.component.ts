import { ChangeDetectionStrategy, Component, OnDestroy } from '@angular/core';
import { ActivatedRoute, Params, Router } from '@angular/router';
import { EditUserFormComponent } from '@app/miniapps/user-management/components/edit-user-form/edit-user-form.component';
import { LifecycleHelper } from '@app/shared/helpers/lifecycle-helper';
import { StateContent } from '@app/ui-components/state/state';
import { UserManagementSearchService } from '@core/user-managemenet/services/user-management-search.service';
import { UserManagementListItem } from '@endpoint/user-management/model/user-management-list-item';
import { BottomSheetService } from '@ui/bottom-sheet/services/bottom-sheet.service';
import { Observable } from 'rxjs';
import {
  distinctUntilChanged,
  first,
  map, switchMap,
  takeUntil,
  withLatestFrom
} from 'rxjs/operators';

@Component({
  selector: 'app-user-management-list',
  templateUrl: './user-management-list.component.html',
  styleUrls: ['./user-management-list.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class UserManagementListComponent implements OnDestroy {

  public results: Observable<UserManagementListItem[]> =
      this.userManagementSearchService.getResults();

  public loading: Observable<boolean> = this.userManagementSearchService.getLoading();

  public phrase: Observable<string> = this.activatedRoute.params.pipe(
      map((params: Params) => params['phrase'] || ''),
      distinctUntilChanged(),
  );

  public empty: Observable<boolean> = this.loading.pipe(
      switchMap((loading: boolean) => {
        return this.results.pipe(
            map((results: UserManagementListItem[]) =>
                !loading && results && !results.length));
      })
  );

  loadingState: StateContent = {
    centered: true,
    title: 'usersManagement.list.loading',
    inProgress: true
  };

  noResultsState: StateContent = {
    centered: true,
    title: 'usersManagement.list.noResults.title',
    description: 'usersManagement.list.noResults.description'
  };

  private lifecycle: LifecycleHelper = new LifecycleHelper();

  constructor(
      private userManagementSearchService: UserManagementSearchService,
      private bottomSheetService: BottomSheetService,
      private activatedRoute: ActivatedRoute,
      private router: Router) {

    this.phrase.pipe(
        takeUntil(this.lifecycle.destroyed)
      ).subscribe((phrase: string) => this.userManagementSearchService.search(phrase));
  }

  public setPhrase(phrase: string): void {
    this.router.navigate(['/user-management/users', phrase], {relativeTo: this.activatedRoute});
  }

  public addUser(): void {
    this.editUser(null);
  }

  public editUser(user: UserManagementListItem): void {
    this.bottomSheetService.openLarge(EditUserFormComponent, {
      data: user ? {...user} : null
    }).afterDismissed().pipe(
        withLatestFrom(this.phrase),
        takeUntil(this.lifecycle.destroyed),
        first())
      .subscribe(([result, phrase]) => {
        if (result) {
          this.userManagementSearchService.search(phrase);
        }
    });
  }

  public ngOnDestroy(): void {
    this.lifecycle.destroy();
  }
}

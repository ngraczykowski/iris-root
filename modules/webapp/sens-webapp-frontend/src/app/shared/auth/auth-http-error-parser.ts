import { HttpErrorResponse } from '@angular/common/http';

export enum AuthHttpError {
    INVALID_USER_CREDENTIALS,
    EXPIRED_USER_CREDENTIALS,
    DISABLED_USER,
    LOCKED_USER,
    EXPIRED_USER,
    CORRUPTED_TOKEN,
    NOT_ACCESS_TOKEN,
    NOT_REFRESH_TOKEN,
    EXPIRED_ACCESS_TOKEN,
    EXPIRED_REFRESH_TOKEN,
    INVALID_OAUTH_CLIENT_CREDENTIALS,
    MISSING_OAUTH_CLIENT_CREDENTIALS
}

export class AuthHttpErrorParser {

    private predicatesMap: Map<(e: HttpErrorResponse) => boolean, AuthHttpError> = new Map();

    constructor() {
        this.predicatesMap.set(
            e => this.hasStatus(e, 400)
                && this.hasError(e, 'invalid_grant')
                && this.hasErrorDescription(e, 'Bad credentials'),
            AuthHttpError.INVALID_USER_CREDENTIALS
        );
        this.predicatesMap.set(
            e => this.hasStatus(e, 400)
                && this.hasError(e, 'invalid_grant')
                && this.hasErrorDescription(e, 'User credentials have expired'),
            AuthHttpError.EXPIRED_USER_CREDENTIALS
        );
        this.predicatesMap.set(
            e => this.hasStatus(e, 400)
                && this.hasError(e, 'invalid_grant')
                && this.hasErrorDescription(e, 'User is disabled'),
            AuthHttpError.DISABLED_USER
        );
        this.predicatesMap.set(
            e => this.hasStatus(e, 400)
                && this.hasError(e, 'invalid_grant')
                && this.hasErrorDescription(e, 'User account is locked'),
            AuthHttpError.LOCKED_USER
        );
        this.predicatesMap.set(
            e => this.hasStatus(e, 400)
                && this.hasError(e, 'invalid_grant')
                && this.hasErrorDescription(e, 'User account has expired'),
            AuthHttpError.EXPIRED_USER
        );
        this.predicatesMap.set(
            e => this.hasStatus(e, 401)
                && this.hasError(e, 'invalid_token')
                && this.hasErrorDescription(e, 'Cannot convert access token to JSON'),
            AuthHttpError.CORRUPTED_TOKEN
        );
        this.predicatesMap.set(
            e => this.hasStatus(e, 401)
                && this.hasError(e, 'invalid_token')
                && this.hasErrorDescription(e, 'Encoded token is a refresh token'),
            AuthHttpError.NOT_ACCESS_TOKEN
        );
        this.predicatesMap.set(
            e => this.hasStatus(e, 401)
                && this.hasError(e, 'invalid_token')
                && this.hasErrorDescription(e, 'Encoded token is not a refresh token'),
            AuthHttpError.NOT_REFRESH_TOKEN
        );
        this.predicatesMap.set(
            e => this.hasStatus(e, 401)
                && this.hasError(e, 'invalid_token')
                && this.hasErrorDescriptionSatisfying(e, desc => desc.startsWith('Access token expired:')),
            AuthHttpError.EXPIRED_ACCESS_TOKEN
        );
        this.predicatesMap.set(
            e => this.hasStatus(e, 401)
                && this.hasError(e, 'invalid_token')
                && this.hasErrorDescriptionSatisfying(e, desc => desc.startsWith('Invalid refresh token (expired):')),
            AuthHttpError.EXPIRED_REFRESH_TOKEN
        );
        this.predicatesMap.set(
            e => this.hasStatus(e, 401)
                && this.hasError(e, 'Unauthorized')
                && this.hasErrorMessage(e, 'Bad credentials'),
            AuthHttpError.INVALID_OAUTH_CLIENT_CREDENTIALS
        );
        this.predicatesMap.set(
            e => this.hasStatus(e, 401)
                && this.hasError(e, 'Unauthorized')
                && this.hasErrorMessage(e, 'Full authentication is required to access this resource'),
            AuthHttpError.MISSING_OAUTH_CLIENT_CREDENTIALS
        );
    }

    private hasStatus(e: HttpErrorResponse, expected: number) {
        return e && e.status === expected;
    }

    private hasError(e: HttpErrorResponse, expected: String) {
        return e && e.error && e.error.error === expected;
    }

    private hasErrorDescription(e: HttpErrorResponse, expected: String) {
        return e && e.error && e.error.error_description === expected;
    }

    private hasErrorDescriptionSatisfying(e: HttpErrorResponse, predicate: (String) => boolean) {
        return e && e.error && e.error.error_description && predicate(e.error.error_description);
    }

    private hasErrorMessage(e: HttpErrorResponse, expected: string) {
        return e && e.error && e.error.message === expected;
    }

    parse(error: HttpErrorResponse): AuthHttpError {
        if (error) {
            for (const e of Array.from(this.predicatesMap.entries())) {
                if (e[0](error)) {
                    return e[1];
                }
            }
        }
        return null;
    }
}

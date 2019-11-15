import { HttpErrorResponse } from '@angular/common/http';
import { AuthHttpError, AuthHttpErrorParser } from '@app/shared/auth/auth-http-error-parser';

describe('AuthHttpErrorParser', () => {
    const parser: AuthHttpErrorParser = new AuthHttpErrorParser();

    function assertError(status, error, expectedError: AuthHttpError) {
        const errorResponse = new HttpErrorResponse({
            error: error,
            status: status
        });
        expect(parser.parse(errorResponse)).toEqual(expectedError);
    }

    it('should not call consumer, when error is not parsed', () => {
        assertError(null, null, null);
        assertError({}, 200, null);
        assertError({}, 500, null);
    });

    it('should parse errors correctly', () => {
        assertError(400, {
            'error': 'invalid_grant',
            'error_description': 'Bad credentials'
        }, AuthHttpError.INVALID_USER_CREDENTIALS);
        assertError(400, {
            'error': 'invalid_grant',
            'error_description': 'User is disabled'
        }, AuthHttpError.DISABLED_USER);
        assertError(401, {
            'error': 'invalid_token',
            'error_description': 'Cannot convert access token to JSON'
        }, AuthHttpError.CORRUPTED_TOKEN);
        assertError(401, {
            'error': 'invalid_token',
            'error_description': 'Access token expired: <TOKEN>'
        }, AuthHttpError.EXPIRED_ACCESS_TOKEN);
        assertError(401, {
            'error': 'invalid_token',
            'error_description': 'Invalid refresh token (expired): <TOKEN>'
        }, AuthHttpError.EXPIRED_REFRESH_TOKEN);
        assertError(401, {
            'timestamp': 1526469263332,
            'status': 401, 'error': 'Unauthorized',
            'message': 'Bad credentials',
            'path': '/oauth/token'
        }, AuthHttpError.INVALID_OAUTH_CLIENT_CREDENTIALS);
        assertError(401, {
            'timestamp': 1526469331391,
            'status': 401,
            'error': 'Unauthorized',
            'message': 'Full authentication is required to access this resource',
            'path': '/oauth/token'
        }, AuthHttpError.MISSING_OAUTH_CLIENT_CREDENTIALS);
        assertError(401, {
            'error': 'invalid_token',
            'error_description': 'Encoded token is a refresh token'
        }, AuthHttpError.NOT_ACCESS_TOKEN);
        assertError(401, {
            'error': 'invalid_token',
            'error_description': 'Encoded token is not a refresh token'
        }, AuthHttpError.NOT_REFRESH_TOKEN);
    });
});

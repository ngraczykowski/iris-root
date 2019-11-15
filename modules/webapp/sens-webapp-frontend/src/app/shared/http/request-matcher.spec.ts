import { RequestMatcher } from '@app/shared/http/request-matcher';

describe('RequestMatcher', () => {

  const requestMatcher: RequestMatcher = new RequestMatcher(/^api.*/, ['GET']);

  it('given request with matching method accept it', () => {
    expect(requestMatcher.matches('api/path', 'GET')).toBe(true);
  });

  it('given request witch not matching method deny it', () => {
    expect(requestMatcher.matches('api/path', 'OPTIONS')).toBe(false);
  });

  it('given request witch not matching path deny it', () => {
    expect(requestMatcher.matches('decision-trees/1', 'GET')).toBe(false);
  });

  it('given request witch not matching path and method deny it', () => {
    expect(requestMatcher.matches('decision-trees/1', 'OPTIONS')).toBe(false);
  });
});

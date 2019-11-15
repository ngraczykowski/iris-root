import { ErrorMapper } from '@app/shared/http/error-mapper';

describe('ErrorMapper', () => {
  let mapper: ErrorMapper;

  beforeEach(() => {
    mapper = new ErrorMapper({
      'SERVER_ERROR_1': 'MAPPED_ERROR_1',
      'SERVER_ERROR_2': 'MAPPED_ERROR_2'
    }, 'prefix.');
  });

  it('should get prefix.UNKNOWN when tried to map invalid error', () => {
    expect(mapper.get(null)).toEqual('prefix.UNKNOWN');
    expect(mapper.get({})).toEqual('prefix.UNKNOWN');
    expect(mapper.get({error: null})).toEqual('prefix.UNKNOWN');
    expect(mapper.get({error: {key: null}})).toEqual('prefix.UNKNOWN');
  });

  it('should get prefix.UNKNOWN when tried to map unknown error', () => {
    expect(mapper.get({error: {key: 'unknown server error'}})).toEqual('prefix.UNKNOWN');
  });

  it('should get prefix.MAPPED_ERROR_1 when tried to map SERVER_ERROR_1', () => {
    expect(mapper.get({error: {key: 'SERVER_ERROR_1'}})).toEqual('prefix.MAPPED_ERROR_1');
  });

  it('should get prefix.MAPPED_ERROR_1 when tried to map SERVER_ERROR_2', () => {
    expect(mapper.get({error: {key: 'SERVER_ERROR_2'}})).toEqual('prefix.MAPPED_ERROR_2');
  });
});

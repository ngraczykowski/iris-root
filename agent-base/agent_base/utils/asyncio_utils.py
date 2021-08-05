import functools
from typing import Callable


# https://stackoverflow.com/questions/66490598/how-to-transform-a-function-into-a-coroutine-in-python-3-8
def awaitify(sync_func: Callable):
    """Wrap a synchronous callable to allow ``await``'ing it"""

    @functools.wraps(sync_func)
    async def async_func(*args, **kwargs):
        return sync_func(*args, **kwargs)

    return async_func

# HTTP tests

The [api-tests.http](./api-tests.http) file contains test requests to use with IntelliJ HTTP Client.

## Configuring the tests

Before running requests you have to configure the secrets.
To do so, copy the `http-client.private.env.json.example` file to `http-client.private.env.json`:

```bash
cp http-client.private.env.json.example http-client.private.env.json
```

Then edit the  `http-client.private.env.json` file, replacing places marked using `<<...>>` with correct client secrets.

# stormpath-test

Right now, Spring Security and Stormpath OAuth do not play nicely together :(. An upcoming release will resolve this. In the meantime, here's what you can do:

1. Disable Spring Security

    Add the following to `application.properties`:

    ```
    stormpath.spring.security.enabled = false
    security.basic.enabled = false
    ```

2. Add a check in your controller for basic authentication or authorizaton rules

    ```
    @RequestMapping("/greeting")
    public Greeting greeting(HttpServletRequest req) {
        Account account = AccountResolver.INSTANCE.getAccount(req);

        if (account == null) { throw new ForbiddenException(); }

        return new Greeting(counter.incrementAndGet(), String.format(template, account.getFullName()));
    }
    ```

With this setup, Stormpath OAuth behaves as expected:

```
curl -X POST \
-H "Origin: http://localhost:8080" \
-H "Content-Type: application/x-www-form-urlencoded" \
-d "grant_type=password&username=<username>&password=<password>" \
http://localhost:8080/oauth/token
```

This will return an access token:

```
{"access_token":"eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI1MGMzMGQ4NC04YjE...","token_type":"Bearer","expires_in":259200}
```

You can then hit your protected endpoint using the access token:

```
curl -H "Authorization: Bearer <access token from previous step>" \
http://localhost:8080/greeting
```

This will return the response you are expecting:

```
{"id":1,"content":"Hello, m s!"}
```

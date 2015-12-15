package com.stormpath.tutorial.controller;

import com.stormpath.sdk.account.Account;
import com.stormpath.sdk.servlet.account.AccountResolver;
import com.stormpath.tutorial.exception.ForbiddenException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.atomic.AtomicLong;

@RestController
public class GreetingController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping("/greeting")
    public Greeting greeting(HttpServletRequest req) {
        Account account = AccountResolver.INSTANCE.getAccount(req);

        if (account == null) { throw new ForbiddenException(); }

        return new Greeting(counter.incrementAndGet(), String.format(template, account.getFullName()));
    }
}

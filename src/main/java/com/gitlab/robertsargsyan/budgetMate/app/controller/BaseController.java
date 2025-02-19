package com.gitlab.robertsargsyan.budgetMate.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class BaseController {

    @GetMapping("/")
    public RedirectView root(){ return new RedirectView("/dashboard"); }

    @GetMapping("/account/{id}")
    public String account() { return "account"; }

    @GetMapping("/dashboard")
    public String dashboard(){ return "index"; }

    @GetMapping("/profile")
    public String profile() { return "profile"; }

    @GetMapping("/records-history")
    public String recordsHistory() { return "records_history"; }

    @GetMapping("/record/{id}")
    public String record() { return "record"; }

    @GetMapping("/budgets")
    public String budgets() { return "budgets"; }

    @GetMapping("/budget/{id}")
    public String budget() { return "budget"; }

    @GetMapping("/analytics")
    public String analytics() { return "analytics"; }

    @GetMapping("/500-error")
    public String serverError() { return "500-error"; }
}

package com.bank.loan.application.web;

import com.bank.loan.application.repository.LoanApplicationRepository;
import com.bank.loan.application.service.LoanApplicationCommandService;
import com.bank.loan.common.api.LoanApplicationResponse;
import com.bank.loan.common.api.SubmitLoanApplicationRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/applications")
public class LoanApplicationController {
    private final LoanApplicationCommandService commandService;
    private final LoanApplicationRepository repository;

    public LoanApplicationController(LoanApplicationCommandService commandService, LoanApplicationRepository repository) {
        this.commandService = commandService;
        this.repository = repository;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public LoanApplicationResponse submit(@Valid @RequestBody SubmitLoanApplicationRequest request) {
        return commandService.submit(request);
    }

    @GetMapping("/{applicationId}")
    public Object get(@PathVariable String applicationId) {
        return repository.findById(applicationId).orElseThrow();
    }
}

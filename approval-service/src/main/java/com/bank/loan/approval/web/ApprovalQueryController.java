package com.bank.loan.approval.web;

import com.bank.loan.approval.repository.ApprovalDecisionRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/approvals")
public class ApprovalQueryController {
    private final ApprovalDecisionRepository repository;

    public ApprovalQueryController(ApprovalDecisionRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/{applicationId}")
    public Object get(@PathVariable String applicationId) {
        return repository.findById(applicationId).orElseThrow();
    }
}

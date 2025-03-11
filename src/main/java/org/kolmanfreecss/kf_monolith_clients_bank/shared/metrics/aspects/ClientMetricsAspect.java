package org.kolmanfreecss.kf_monolith_clients_bank.shared.metrics.aspects;

import java.util.List;
import java.util.Optional;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.kolmanfreecss.kf_monolith_clients_bank.domain.client.Client;
import org.kolmanfreecss.kf_monolith_clients_bank.shared.metrics.ClientMetrics;
import org.springframework.stereotype.Component;

import io.micrometer.core.instrument.Timer;
import lombok.RequiredArgsConstructor;

/**
 * Aspect for handling client operation metrics.
 * This aspect separates metrics collection logic from business logic using AOP.
 */
@Aspect
@Component
@RequiredArgsConstructor
public class ClientMetricsAspect {

    private final ClientMetrics metrics;

    /**
     * Measures execution time and updates metrics for getAllClients operation
     */
    @Around("execution(* org.kolmanfreecss.kf_monolith_clients_bank.application.client.services.ClientService.getAllClients())")
    public Object measureGetAllClients(ProceedingJoinPoint joinPoint) throws Throwable {
        Timer.Sample sample = metrics.startTimer();
        try {
            List<Client> result = (List<Client>) joinPoint.proceed();
            metrics.setActiveClients(result.size());
            return result;
        } finally {
            metrics.stopTimer(sample, "get_all_clients");
        }
    }

    /**
     * Measures execution time for getClientById operation
     */
    @Around("execution(* org.kolmanfreecss.kf_monolith_clients_bank.application.client.services.ClientService.getClientById(..))")
    public Object measureGetClientById(ProceedingJoinPoint joinPoint) throws Throwable {
        Timer.Sample sample = metrics.startTimer();
        try {
            return joinPoint.proceed();
        } finally {
            metrics.stopTimer(sample, "get_client_by_id");
        }
    }

    /**
     * Measures execution time and counts client creation
     */
    @Around("execution(* org.kolmanfreecss.kf_monolith_clients_bank.application.client.services.ClientService.createClient(..))")
    public Object measureCreateClient(ProceedingJoinPoint joinPoint) throws Throwable {
        Timer.Sample sample = metrics.startTimer();
        try {
            Object result = joinPoint.proceed();
            metrics.incrementClientCreation();
            return result;
        } finally {
            metrics.stopTimer(sample, "create_client");
        }
    }

    /**
     * Measures execution time and counts client updates
     */
    @Around("execution(* org.kolmanfreecss.kf_monolith_clients_bank.application.client.services.ClientService.updateClient(..))")
    public Object measureUpdateClient(ProceedingJoinPoint joinPoint) throws Throwable {
        Timer.Sample sample = metrics.startTimer();
        try {
            Optional<?> result = (Optional<?>) joinPoint.proceed();
            if (result.isPresent()) {
                metrics.incrementClientUpdate();
            }
            return result;
        } finally {
            metrics.stopTimer(sample, "update_client");
        }
    }

    /**
     * Measures execution time and counts client deletions
     */
    @Around("execution(* org.kolmanfreecss.kf_monolith_clients_bank.application.client.services.ClientService.deleteClient(..))")
    public Object measureDeleteClient(ProceedingJoinPoint joinPoint) throws Throwable {
        Timer.Sample sample = metrics.startTimer();
        try {
            Object result = joinPoint.proceed();
            metrics.incrementClientDeletion();
            return result;
        } finally {
            metrics.stopTimer(sample, "delete_client");
        }
    }

    /**
     * Measures execution time for getting client rights
     */
    @Around("execution(* org.kolmanfreecss.kf_monolith_clients_bank.application.client.services.ClientService.getClientRights(..))")
    public Object measureGetClientRights(ProceedingJoinPoint joinPoint) throws Throwable {
        Timer.Sample sample = metrics.startTimer();
        try {
            return joinPoint.proceed();
        } finally {
            metrics.stopTimer(sample, "get_client_rights");
        }
    }

    /**
     * Measures execution time and counts client rights updates
     */
    @Around("execution(* org.kolmanfreecss.kf_monolith_clients_bank.application.client.services.ClientService.updateClientRights(..))")
    public Object measureUpdateClientRights(ProceedingJoinPoint joinPoint) throws Throwable {
        Timer.Sample sample = metrics.startTimer();
        try {
            Object result = joinPoint.proceed();
            metrics.incrementClientRightsUpdate();
            return result;
        } finally {
            metrics.stopTimer(sample, "update_client_rights");
        }
    }
}
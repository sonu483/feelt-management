package com.feelt.fleet.controller;

import com.feelt.fleet.dto.DispatchRequest;
import com.feelt.fleet.dto.RouteOptimizationRequest;
import com.feelt.fleet.dto.RouteOptimizationResponse;
import com.feelt.fleet.exception.ResourceNotFoundException;
import com.feelt.fleet.model.Route;
import com.feelt.fleet.repository.RouteRepository;
import com.feelt.fleet.service.DispatchService;
import com.feelt.fleet.service.RouteOptimizationService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/routes")
public class RouteController {

    private final RouteRepository routeRepository;
    private final RouteOptimizationService routeOptimizationService;
    private final DispatchService dispatchService;

    public RouteController(
            RouteRepository routeRepository,
            RouteOptimizationService routeOptimizationService,
            DispatchService dispatchService
    ) {
        this.routeRepository = routeRepository;
        this.routeOptimizationService = routeOptimizationService;
        this.dispatchService = dispatchService;
    }

    @GetMapping
    public List<Route> getRoutes() {
        return routeRepository.findAll();
    }

    @GetMapping("/page")
    public Page<Route> getRoutesPage(Pageable pageable) {
        return routeRepository.findAll(pageable);
    }

    @GetMapping("/{id}")
    public Route getRoute(@PathVariable Long id) {
        return routeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Route not found: " + id));
    }

    @PostMapping("/optimize")
    public RouteOptimizationResponse optimize(@Valid @RequestBody RouteOptimizationRequest request) {
        return routeOptimizationService.optimize(request);
    }

    @PostMapping("/dispatch")
    @ResponseStatus(HttpStatus.CREATED)
    public Route dispatch(@Valid @RequestBody DispatchRequest request) {
        return dispatchService.dispatch(request);
    }
}

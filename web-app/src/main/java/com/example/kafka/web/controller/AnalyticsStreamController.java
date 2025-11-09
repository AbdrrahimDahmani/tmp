package com.example.kafka.web.controller;

import com.example.kafka.common.AnalyticsResult;
import com.example.kafka.web.service.AnalyticsStreamService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsStreamController {

    private final AnalyticsStreamService analyticsStreamService;

    public AnalyticsStreamController(AnalyticsStreamService analyticsStreamService) {
        this.analyticsStreamService = analyticsStreamService;
    }

    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<AnalyticsResult> streamResults() {
        return analyticsStreamService.streamAnalytics();
    }
}

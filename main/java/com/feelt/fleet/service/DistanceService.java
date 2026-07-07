package com.feelt.fleet.service;

import com.feelt.fleet.dto.CoordinateRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class DistanceService {

    private final WebClient webClient;
    private final boolean useExternalApi;

    public DistanceService(
            WebClient.Builder webClientBuilder,
            @Value("${app.routing.osrm-base-url}") String osrmBaseUrl,
            @Value("${app.routing.use-external-api:false}") boolean useExternalApi
    ) {
        this.webClient = webClientBuilder.baseUrl(osrmBaseUrl).build();
        this.useExternalApi = useExternalApi;
    }

    public double distanceKm(CoordinateRequest from, CoordinateRequest to) {
        if (!useExternalApi) {
            return haversineKm(from.latitude(), from.longitude(), to.latitude(), to.longitude());
        }

        String uri = "/route/v1/driving/%f,%f;%f,%f?overview=false"
                .formatted(from.longitude(), from.latitude(), to.longitude(), to.latitude());

        try {
            OsrmRouteResponse response = webClient.get()
                    .uri(uri)
                    .retrieve()
                    .bodyToMono(OsrmRouteResponse.class)
                    .block();

            if (response != null && response.routes() != null && response.routes().length > 0) {
                return response.routes()[0].distance() / 1000.0;
            }
        } catch (RuntimeException ignored) {
            return haversineKm(from.latitude(), from.longitude(), to.latitude(), to.longitude());
        }

        return haversineKm(from.latitude(), from.longitude(), to.latitude(), to.longitude());
    }

    private double haversineKm(double lat1, double lon1, double lat2, double lon2) {
        double earthRadiusKm = 6371.0;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return round(earthRadiusKm * c);
    }

    public double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }

    public record OsrmRouteResponse(OsrmRoute[] routes) {
    }

    public record OsrmRoute(double distance) {
    }
}

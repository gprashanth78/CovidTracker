package com.covidTracker.controller;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.covidTracker.modal.LocationStatus;
import com.covidTracker.service.CovidVirusDataService;

@Controller
public class CovidTrackerController {

    @Autowired
    CovidVirusDataService virusDataService;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("LocationStatus", Collections.emptyList());
        model.addAttribute("totalLatestCases", 0);
        return "home";
    }

    @GetMapping("/search")
    public String search(@RequestParam(required = false) String country, Model model) {
        List<LocationStatus> allStates = virusDataService.getAllStates();
        List<LocationStatus> filteredStates;

        if (country == null || country.isEmpty()) {
            filteredStates = allStates;
        } else {
            filteredStates = allStates.stream()
                    .filter(s -> s.getCountry().equalsIgnoreCase(country))
                    .collect(Collectors.toList());
        }

        int totalLatestCases = filteredStates.stream().mapToInt(LocationStatus::getLatestTotalCases).sum();
        model.addAttribute("LocationStatus", filteredStates);
        model.addAttribute("totalLatestCases", totalLatestCases);

        return "home";
    }
}

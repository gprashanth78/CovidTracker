package com.covidTracker.service;

import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.covidTracker.modal.LocationStatus;

@Service
public class CovidVirusDataService {

	private static String COVID_DATA = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_global.csv";

	private List<LocationStatus> allStates = new ArrayList<>();

	@PostConstruct
	@Scheduled(cron = "* * 1 * * *")
	public void getVirusData() throws IOException, InterruptedException {

		List<LocationStatus> newStates = new ArrayList<>();

		HttpClient httpClient = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder().uri(URI.create(COVID_DATA)).build();
		HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

		StringReader csvBodyReader = new StringReader(response.body());

		Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(csvBodyReader);
		for (CSVRecord record : records) {
			LocationStatus locationStatus = new LocationStatus();
			locationStatus.setState(record.get("Province/State"));
			locationStatus.setCountry(record.get("Country/Region"));
			locationStatus.setLatestTotalCases(Integer.parseInt(record.get(record.size() - 1)));
			System.out.println(locationStatus);
			newStates.add(locationStatus);
		}
		this.allStates = newStates;
	}

	public List<LocationStatus> getAllStates() {
		return allStates;
	}
}

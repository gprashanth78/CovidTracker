package com.covidTracker.modal;

import lombok.Data;

@Data
public class LocationStatus {

	private String state;
	private String country;
	private int latestTotalCases;

}

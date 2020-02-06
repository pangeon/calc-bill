package pl.strefakursow.swamp_of_pox.model;

/* Kod źródłowy autorstwa Michała Wojtyny,
Projektowanie RESTful API – dobre praktyki (videokurs).
portal Strefa Kursów:
https://strefakursow.pl/kursy/programowanie/projektowanie_restful_api_-_dobre_praktyki.html */

import java.util.List;

public class Document {
	private long number;
	private String title;
	private List<String> tags;

	public long getNumber() {
		return number;
	}

	public void setNumber(long number) {
		this.number = number;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	public Document(String title) {
		this.title = title;
	}

	public Document() {
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}

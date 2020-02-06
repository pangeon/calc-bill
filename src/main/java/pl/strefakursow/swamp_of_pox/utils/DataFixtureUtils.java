package pl.strefakursow.swamp_of_pox.utils;

/* Kod źródłowy autorstwa Michała Wojtyny,
Projektowanie RESTful API – dobre praktyki (videokurs).
portal Strefa Kursów:
https://strefakursow.pl/kursy/programowanie/projektowanie_restful_api_-_dobre_praktyki.html */

import pl.strefakursow.swamp_of_pox.model.Document;
import pl.strefakursow.swamp_of_pox.model.Profile;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DataFixtureUtils {
	private static Document documentWithTitle(String documentTitle) {
		Document document = new Document(documentTitle);
		document.setNumber(new Random().nextLong());
		document.setTags(List.of("tag0", "tag1", "tag2"));
		return document;
	}

	public static List<Document> initDocuments() {
		ArrayList<Document> documents = new ArrayList<>();
		documents.add(documentWithTitle("doc0"));
		documents.add(documentWithTitle("doc1"));
		documents.add(documentWithTitle("doc2"));
		return documents;
	}

	public static List<Profile> allProfiles() {
		return List
			.of(new Profile("goobar"), new Profile("foobar"),
				new Profile("hoobar"));
	}
}

package pl.strefakursow.swamp_of_pox.model;

/* Kod źródłowy autorstwa Michała Wojtyny,
Projektowanie RESTful API – dobre praktyki (videokurs).
portal Strefa Kursów:
https://strefakursow.pl/kursy/programowanie/projektowanie_restful_api_-_dobre_praktyki.html */

import java.util.List;
import java.util.Objects;

public class Command {
	private long numberToRemove;
	private String action;
	private Document documentToAdd;
	private List<Document> documents;

	@Override
	public String toString() {
		return "Command{" + "numberToRemove=" + numberToRemove + ", action='" + action + '\'' + ", documentToAdd=" + documentToAdd + ", documents=" + documents + '}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Command command = (Command) o;
		return numberToRemove == command.numberToRemove && Objects
			.equals(action, command.action) && Objects
			.equals(documentToAdd, command.documentToAdd) && Objects
			.equals(documents, command.documents);
	}

	@Override
	public int hashCode() {
		return Objects
			.hash(numberToRemove, action, documentToAdd,
				documents);
	}

	public long getNumberToRemove() {
		return numberToRemove;
	}

	public void setNumberToRemove(long numberToRemove) {
		this.numberToRemove = numberToRemove;
	}

	public Document getDocumentToAdd() {
		return documentToAdd;
	}

	public void setDocumentToAdd(Document documentToAdd) {
		this.documentToAdd = documentToAdd;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public List<Document> getDocuments() {
		return documents;
	}

	public void setDocuments(List<Document> documents) {
		this.documents = documents;
	}

}

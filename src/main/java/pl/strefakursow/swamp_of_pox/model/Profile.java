package pl.strefakursow.swamp_of_pox.model;

/* Kod źródłowy autorstwa Michała Wojtyny,
Projektowanie RESTful API – dobre praktyki (videokurs).
portal Strefa Kursów:
https://strefakursow.pl/kursy/programowanie/projektowanie_restful_api_-_dobre_praktyki.html */

import java.util.Objects;

public class Profile {
	private String name;

	public Profile() {
	}

	public Profile(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Profile{" + "name='" + name + '\'' + '}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Profile profile = (Profile) o;
		return Objects.equals(name, profile.name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}

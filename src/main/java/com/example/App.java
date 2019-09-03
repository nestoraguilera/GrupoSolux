package com.example;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.methods.HttpGet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.pojos.Vehicle;
import com.example.services.DataAcquired;
import com.example.starwars.API;
import com.example.starwars.GetRequestRepository;
import com.example.starwars.Printer;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * Hello world!
 *
 */
@SpringBootApplication
public class App implements CommandLineRunner {
	private static final Logger log = LoggerFactory.getLogger(App.class);

	private API apiCalls;
	private GetRequestRepository repository;
	private Printer printer;
	private String exercise1StringResult, exercise2StringResult;

	@Autowired
	DataAcquired dataAcquired;

	public App() {
		super();
		apiCalls = new API();
		repository = new GetRequestRepository(apiCalls);
		printer = new Printer();
		exercise1StringResult = exercise2StringResult = "";
	}

	public static void main(String[] args) {
		SpringApplication.run(App.class, args);

		// -------------------------------------------
		// Ejemplo: Imprimir los detalles de la pelicula "The Phantom Menace"
		//
		// JsonObject jsonObject = repository.getAll("films", "Phantom");
		// JsonArray filmresults = jsonObject.getAsJsonArray("results");
		// printer.printDetailsFilms(filmresults);
	}

	/**
	 * Hereda de CommandLineRunner, para dar punto de entrada de ejecucion
	 * 
	 * @author nestor
	 */
	@Override
	public void run(String... args) throws Exception {
		log.info("--[Inicia la clase desde metodo CommandLineRunner.run]--");
		exercise1();
		exercise2();
	}

	/**
	 * Ejercicio 1: Imprimir en consola el Nombre del personaje, y el nombre de su
	 * especie, el cual haya aparecido en la mayor cantidad de peliculas. Ejemplo:
	 * Anakin Skywalker - Human
	 * 
	 * @author nestor
	 */
	private void exercise1() {
		log.info("--[Ejecutando ejercicio 1]--");
		JsonObject jsonObject = repository.getAll("people", null);
		JsonArray results = jsonObject.getAsJsonArray("results");
		String name = "", specie = "";
		int filmsQty = 0;
		for (Object o : results) {
			JsonObject aPeople = (JsonObject) o;
			JsonArray films = aPeople.get("films").getAsJsonArray();
			JsonArray species = aPeople.get("species").getAsJsonArray();
			if (films.size() > filmsQty) {
				name = aPeople.get("name").getAsString();
				filmsQty = films.size();
				for (JsonElement specieUrlObj : species) {
					String specieUrl = specieUrlObj.getAsString();
					try {
						HttpGet httpGet = new HttpGet(specieUrl);
						JsonObject specieClass = apiCalls.getRequest(httpGet);
						specie = specieClass.get("name").getAsString();
					} catch (Exception e) {
						specie = "";
						log.error("No fue posible obtener la especie de " + name);
					}
				}
			}
		}
		log.info("El que mas apareció fue:" + name + ", cantidad:" + filmsQty + ", especie:" + specie);
		dataAcquired.setMfaName(name);
		dataAcquired.setMfaSpecie(specie);
	}

	/**
	 * Ejercicio 2: Imprimir en consola una tabla resumen de todas las naves que ha
	 * piloteado "Obi-Wan Kenobi" en cada pelicula
	 * 
	 * @author nestor
	 * @throws Exception
	 */
	private void exercise2() throws Exception {
		log.info("--[Ejecutando ejercicio 2]--");
		log.warn("La solicitud no es posible de completar...");
		log.warn(
				"no existe una relacion entre tres entidades donde se pueda filtrar por people+film+(vehicle|starship)");
		// TODO esto debería estar mejor manejado
		JsonObject jsonObject = apiCalls.getRequest(new HttpGet("https://swapi.co/api/people/10/"));
		JsonArray vehicles = jsonObject.getAsJsonArray("vehicles");
		JsonArray starships = jsonObject.getAsJsonArray("starships");
		List<Vehicle> vehiclesAndstarships = new ArrayList<Vehicle>();
		log.info("--[Vehiculos y naves de kenobi]--");
		if (vehicles != null)
			for (JsonElement elem : vehicles) {
				String url = elem.getAsString();
				try {
					HttpGet httpGet = new HttpGet(url);
					JsonObject retvalClass = apiCalls.getRequest(httpGet);
					vehiclesAndstarships.add(new Vehicle(retvalClass.get("name").getAsString(), "vehicle"));
					log.info("\t* " + retvalClass.get("name").getAsString() + "|vehicle");
				} catch (Exception e) {
					log.error("No fue posible obtener el vehiculo de kenobi");
				}
			}
		if (starships != null)
			for (JsonElement elem : starships) {
				String url = elem.getAsString();
				try {
					HttpGet httpGet = new HttpGet(url);
					JsonObject retvalClass = apiCalls.getRequest(httpGet);
					vehiclesAndstarships.add(new Vehicle(retvalClass.get("name").getAsString(), "starship"));
					log.info("\t* " + retvalClass.get("name").getAsString() + "|starship");
				} catch (Exception e) {
					log.error("No fue posible obtener el vehiculo de kenobi");
				}
			}
		dataAcquired.setVehicles(vehiclesAndstarships);
	}
}

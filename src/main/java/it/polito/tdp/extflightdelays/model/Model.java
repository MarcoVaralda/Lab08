package it.polito.tdp.extflightdelays.model;

import java.util.HashMap;
import java.util.Map;
import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import it.polito.tdp.extflightdelays.db.ExtFlightDelaysDAO;

public class Model {
	
	ExtFlightDelaysDAO dao;
	
	Graph<Airport, DefaultWeightedEdge> grafo ;
	
	Map<Integer,Airport> mappaAirport;
	
	Map<String,Rotta> mappaRotte;
	
	String rotteValide;
	
	public Model() {
		dao = new ExtFlightDelaysDAO();
		mappaAirport = new HashMap<>();
		dao.loadAllAirports(mappaAirport);	
	}
	
	public Graph<Airport,DefaultWeightedEdge> creaGrafo(int distanza) {
		
		// Aggiungo i vertici
		grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);	
		Graphs.addAllVertices(this.grafo, mappaAirport.values());
		
		// Aggiungo gli archi
		mappaRotte = new HashMap<>();
		dao.loadAllRotte(mappaRotte);
		rotteValide="\nELENCO ROTTE:\n";
		
		
		
		for(Rotta r : mappaRotte.values()) {			
			if(calcolaDistanzaMedia(r)>=distanza) {
				Airport partenza = mappaAirport.get(r.getIdPartenza());
				Airport destinazione = mappaAirport.get(r.getIdDestinazione());
				double distanzaMedia = calcolaDistanzaMedia(r);
				rotteValide = rotteValide +partenza.getAirportName() +"-" +destinazione.getAirportName()+": "+distanzaMedia +"\n";
				Graphs.addEdge(this.grafo, partenza, destinazione, distanzaMedia);
			}
		}
		
		return this.grafo;
	}
	
	public double calcolaDistanzaMedia(Rotta r) {
		return (r.getDistanza()/r.getNumVoli());
	}
	
	public String getRotteValide() {
		return this.rotteValide;
	}

}

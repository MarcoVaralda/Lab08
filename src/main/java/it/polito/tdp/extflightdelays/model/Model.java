package it.polito.tdp.extflightdelays.model;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import it.polito.tdp.extflightdelays.db.ExtFlightDelaysDAO;

public class Model {
	
	ExtFlightDelaysDAO dao = new ExtFlightDelaysDAO();
	
	Graph<Airport, DefaultWeightedEdge> grafo ;
	
	Map<Integer,Airport> mappaAirport;
	
	Map<String,Rotta> mappaRotte;
	
	String rotteValide;
	
	public Graph<Airport,DefaultWeightedEdge> creaGrafo(int distanza) {
		
		// Aggiungo i vertici
		grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		mappaAirport = new TreeMap<>();
		dao.loadAllAirports(mappaAirport);		
		Graphs.addAllVertices(this.grafo, mappaAirport.values());
		
		// Aggiungo gli archi
		mappaRotte = new TreeMap<>();
		dao.loadAllRotte(mappaRotte);
		rotteValide="";
		
		for(Rotta r : mappaRotte.values())
			if(calcolaDistanzaMedia(r)>=distanza) {
				Airport partenza = mappaAirport.get(r.getIdPartenza());
				Airport destinazione = mappaAirport.get(r.getIdDestinazione());
				double distanzaMedia = calcolaDistanzaMedia(r);
				rotteValide = rotteValide +partenza.getAirportName() +"-" +destinazione.getAirportName()+": "+distanzaMedia +"\n";
				Graphs.addEdge(this.grafo, partenza, destinazione, distanzaMedia);
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

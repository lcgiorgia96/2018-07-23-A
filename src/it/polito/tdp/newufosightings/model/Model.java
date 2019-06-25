package it.polito.tdp.newufosightings.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.newufosightings.db.NewUfoSightingsDAO;

public class Model {

	NewUfoSightingsDAO dao;
	Graph<State,DefaultWeightedEdge> grafo;
	List<State> stati;
	Map<String,State> idMap;
	public Model () {
		
		dao = new NewUfoSightingsDAO();
		stati = new ArrayList<>();
	}
	
	public List<String> getForme(int anno) {
		
		return dao.getForme(anno);
	}

	public void creaGrafo(int anno, String forma) {
	
		grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		stati = dao.getStatiAnno(anno);
		
		Graphs.addAllVertices(this.grafo, stati);
		
		System.out.println(grafo.vertexSet().size());
		idMap = new HashMap<>();
		for (State s: stati) {
			idMap.put(s.getId(), s);
		}
		
		List<Vicini> vicini = dao.getVicini(idMap,forma);
		
		for (Vicini v: vicini) {
			State s1 = v.getS1();
			State s2 = v.getS2();
			int peso = v.getNumero();
			Graphs.addEdge(this.grafo, s1, s2, peso);
		}
		
		System.out.println(grafo.edgeSet().size());
	}

	public List<State> getStati() {
		
		return  stati;
	}

	public int getSommaPesi(State s) {
		int somma = 0;
		List<State> adiacenti = Graphs.neighborListOf(this.grafo, s);
		for (State s1: adiacenti) {
		somma+= grafo.getEdgeWeight(grafo.getEdge(s, s1));
		}
		return somma;
	}

}

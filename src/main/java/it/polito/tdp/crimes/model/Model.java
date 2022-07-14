package it.polito.tdp.crimes.model;

import java.time.Month;
import java.time.Year;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;

import it.polito.tdp.crimes.db.EventsDao;

public class Model {
	
	private EventsDao dao;
	private Graph<Integer, DefaultWeightedEdge> grafo;

	public Model( ) {
		super();
		this.dao = new EventsDao();
	}
	
	
	public List<Year> getAnni ()
	{
		return dao.getAnni();
	}
	
	public void creaGrafo(Year anno)
	{
		this.grafo= new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		Graphs.addAllVertices(this.grafo, dao.getVertici());
		for(Distretto d1: dao.getArchi(anno))
		{
			for(Distretto d2: dao.getArchi(anno))
			{	
				    
				if(!d1.equals(d2))
				{  
					if(this.grafo.getEdge(d1.getDistretto(), d2.getDistretto()) == null)
				{
					double distanza = LatLngTool.distance(d1.getPosizione(), d2.getPosizione(), LengthUnit.KILOMETER  );
					Graphs.addEdgeWithVertices(this.grafo, d1.getDistretto(), d2.getDistretto(), distanza);
				}
					
				}
			}
		}
		
	}
	
	


	public Graph<Integer, DefaultWeightedEdge> getGrafo() {
		return grafo;
	}
	
	public List<Vicino> getVicini(int d)
	{
		List<Integer> dVicini = new ArrayList<Integer>();
		List<Vicino> vicini = new ArrayList<Vicino>();
		dVicini= Graphs.neighborListOf(this.grafo, d);
		for(Integer v: dVicini)
		{
			vicini.add(new Vicino(v, this.grafo.getEdgeWeight(this.grafo.getEdge(d, v))));   
		}
		Collections.sort(vicini);
		return vicini;
	}


	public List <Integer> getVertici(Year anno) {
		List <Integer> vertici = new ArrayList<>();
		for(Integer i: this.grafo.vertexSet())
		{
			vertici.add(i);
		}
		return vertici;
	}

	public List<Month> getMesi(Year anno)
	{
		return dao.getMesi(anno);
	}
	
	public List<Integer> getGiorno()
	{
		return dao.getGiorno();
	}


		public int simula(Integer anno, Integer mese, Integer giorno, Integer N) {
			
		
			Simulatore sim = new Simulatore();
			sim.init(N, anno, mese, giorno, this.getGrafo());
			return sim.run();
			
		}
	}

	
	
	




package it.polito.tdp.crimes.model;

import java.time.Year;

import org.jgrapht.graph.DefaultWeightedEdge;

import it.polito.tdp.crimes.db.EventsDao;

public class TestModel {

	public static void main(String[] args) {
		
		Model m = new Model();
		m.creaGrafo(Year.of(2014));
		System.out.println(m.getGrafo().edgeSet().size()+ "archi\n");
		System.out.println(m.getGrafo().vertexSet().size()+ "vertici\n");
		EventsDao dao = new EventsDao();
		
		/*for(DefaultWeightedEdge e : m.getGrafo().edgeSet())
		{
			System.out.println(e.toString()+ "\n"+ " Distanza: " +m.getGrafo().getEdgeWeight(e));
		}*/
		
		for(Distretto d: dao.getArchi(Year.of(2014)))
		{
			System.out.println(d.toString());
		}
		
		
	}

}

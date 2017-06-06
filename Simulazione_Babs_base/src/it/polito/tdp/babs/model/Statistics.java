package it.polito.tdp.babs.model;

public class Statistics implements Comparable<Statistics>{

	private Station stazione;
	private int pick;
	private int drop;
	
	public Statistics(Station stazione, int pick, int drop) {
		super();
		this.stazione = stazione;
		this.pick = pick;
		this.drop = drop;
	}
	public Station getStazione() {
		return stazione;
	}
	public void setStazione(Station stazione) {
		this.stazione = stazione;
	}
	public int getPick() {
		return pick;
	}
	public void setPick(int pick) {
		this.pick = pick;
	}
	public int getDrop() {
		return drop;
	}
	public void setDrop(int drop) {
		this.drop = drop;
	}
	
	//visto che devo solo controllare la latitudine delle stazioni e ordinarle in base ad essa
	@Override 
	public int compareTo(Statistics o) {
		return Double.compare(this.stazione.getLat(), o.getStazione().getLat());    //METODO STATICO DELLA CLASSE DOUBLE
	}															// la differenza come comparatore funziona solo tra interi
}

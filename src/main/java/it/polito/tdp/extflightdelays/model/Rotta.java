package it.polito.tdp.extflightdelays.model;

public class Rotta {
	private int idPartenza;
	private int idDestinazione;
	private double distanza;
	private int numVoli;
	
	public Rotta(int idPartenza, int idDestinazione, double distanza, int numVoli) {
		this.idPartenza = idPartenza;
		this.idDestinazione = idDestinazione;
		this.distanza = distanza;
		this.numVoli = numVoli;
	}

	public int getIdPartenza() {
		return idPartenza;
	}

	public void setIdPartenza(int idPartenza) {
		this.idPartenza = idPartenza;
	}

	public int getIdDestinazione() {
		return idDestinazione;
	}

	public void setIdDestinazione(int idDestinazione) {
		this.idDestinazione = idDestinazione;
	}

	public double getDistanza() {
		return distanza;
	}

	public void setDistanza(double distanza) {
		this.distanza = distanza;
	}

	public int getNumVoli() {
		return numVoli;
	}

	public void setNumVoli(int numVoli) {
		this.numVoli = numVoli;
	}
	
	
}

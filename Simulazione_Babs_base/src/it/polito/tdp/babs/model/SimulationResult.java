package it.polito.tdp.babs.model;

public class SimulationResult {
	
	private int numberOfPickMissed;
	private int numberOfDropMissed;
	
	public SimulationResult(){
		this.numberOfDropMissed =0;
		this.numberOfPickMissed =0;
	}
	
	public void increaseNumberOfPickMissed(){
		this.numberOfPickMissed++;
	}
	public void increaseNumberOfDropMissed(){
		this.numberOfDropMissed++;
	}


	public int getNumberOfPickMissed() {
		return numberOfPickMissed;
	}


	public int getNumberOfDropMissed() {
		return numberOfDropMissed;
	}
	
	
	
	
		

}

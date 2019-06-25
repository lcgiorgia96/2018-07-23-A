package it.polito.tdp.newufosightings.model;

public class Vicini {

	private State s1;
	private State s2;
	private int numero;
	public State getS1() {
		return s1;
	}
	public void setS1(State s1) {
		this.s1 = s1;
	}
	public State getS2() {
		return s2;
	}
	public void setS2(State s2) {
		this.s2 = s2;
	}
	public int getNumero() {
		return numero;
	}
	public void setNumero(int numero) {
		this.numero = numero;
	}
	public Vicini(State s1, State s2, int numero) {
		super();
		this.s1 = s1;
		this.s2 = s2;
		this.numero = numero;
	}
	@Override
	public String toString() {
		return s1+" "+s2+" "+numero;
	}
	
	
}

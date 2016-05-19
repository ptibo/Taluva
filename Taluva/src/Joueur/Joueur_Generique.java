package Joueur;

import terrain.Case.Couleur_Joueur;

//import main.Moteur;

/*
 * Un joueur est défini par :
 *  Son nombre de pions (Tour, Temple , Hutte)
 * 	Sa Couleur
 * 
 * 
 */



public abstract class Joueur_Generique {
	private int temple, tour, hutte,hutteDetruite;
	private Couleur_Joueur c;
	
	public Joueur_Generique(Couleur_Joueur c)
	{
		temple = 3;
		tour = 2;
		hutte = 20;
		hutteDetruite = 0;
		this.c = c;
		
	}
	// -------------- Fonction Get ---------------------
	
	public int getTemple()
	{
		return temple;
	}
	
	public int getTour()
	{
		return tour;
	}
	
	public int getHutte()
	{
		return hutte;
	}
	
	public int getHutteDetruite()
	{
		return hutteDetruite;
	}
	
	public Couleur_Joueur getCouleur()
	{
		return c;
	}
	
	// -------------- Fonction Set ---------------------
	
	public void setTemple(int temple)
	{
		this.temple = temple;
	}
	
	public void setTour(int tour)
	{
		this.tour = tour;
	}
	
	public void setHutte(int hutte)
	{
		this.hutte = hutte;
	}
	
	public void setHutteDetruite(int hutteDetruite)
	{
		this.hutteDetruite = hutteDetruite;
	}
	
	public void decrementeTemple()
	{
		if(temple > 0)
		{
			this.temple = temple-1;
		}
	}

	
	public void decrementeTour()
	{
		if(tour > 0)
		{
			this.tour = tour-1;
		}
		
	}
	public void decrementeHutte(int n)
	{
		if(hutte - n > 0)
		{
			this.hutte = hutte - n;
		}
	}
	
	public void incrementeHutteDetruite(int n)
	{
		this.hutteDetruite = hutteDetruite + n;
	}
	
	
	
}
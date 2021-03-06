package Joueur;

import javax.swing.event.EventListenerList;

import Action.Actions_Tour;
import Moteur.Moteur;
import terrain.Case.Couleur_Joueur;
import terrain.Tuile;

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
	private String nomFaction;
	private final EventListenerList listeners = new EventListenerList();
	
	public Joueur_Generique(Couleur_Joueur c)
	{
		temple = Moteur.nb_max_Temples;
		tour = Moteur.nb_max_Tours;
		hutte = Moteur.nb_max_Huttes;
		hutteDetruite = 0;
		this.c = c;
		
	}
	
	public Joueur_Generique(Couleur_Joueur c,String nomFaction)
	{
		temple = Moteur.nb_max_Temples;
		tour = Moteur.nb_max_Tours;
		hutte = Moteur.nb_max_Huttes;
		hutteDetruite = 0;
		this.nomFaction = nomFaction;
		this.c = c;
		
	}
	
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
	
	public String getnomFaction()
	{
		return nomFaction;
	}
	
	public void setNomFaction(String nom)
	{
		nomFaction = nom;
	}
	
	// -------------- Fonction Set ---------------------
	
	public void setTemple(int temple)
	{
		this.temple = temple;
		MajListeners();
	}
	
	public void setTour(int tour)
	{
		this.tour = tour;
		MajListeners();
	}
	
	public void setHutte(int hutte)
	{
		this.hutte = hutte;
		MajListeners();
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
		MajListeners();
	}

	
	public void decrementeTour()
	{
		if(tour > 0)
		{
			this.tour = tour-1;
		}
		MajListeners();
		
	}
	public void decrementeHutte(int n)
	{
		if(hutte - n >= 0)
		{
			this.hutte = hutte - n;
		}
		MajListeners();
	}
	
	public void incrementeHutteDetruite(int n)
	{
		this.hutteDetruite = hutteDetruite + n;
	}
	
	// Clone un Joueur_Generique avec Moteur
	//TODO
	//Rajouter les IA en plus
	public Joueur_Generique clone(Moteur m)
	{
		// Instancie le clone en fonction du type de la source
		Joueur_Generique clone = new Joueur_Humain(this.getCouleur());
		
		if(this instanceof IA_Random)
		{
			clone = new IA_Random(this.getCouleur(),m);
		}
		else if(this instanceof IA_Heuristique)
		{
			clone = new IA_Heuristique(this.getCouleur(),m);
		}
		else if(this instanceof IA_Moyenne)
		{
			clone = new IA_Moyenne(this.getCouleur(),m);
		}
		
		clone.setHutte(getHutte());
		clone.setTemple(getTemple());
		clone.setTour(getTour());
		clone.setHutteDetruite(getHutteDetruite());
		clone.setNomFaction(getnomFaction());
		for(BatimentCountListener listener : getBatimentCountListeners()) 
		{
			clone.addBatimentCountListener(listener);
		}
		
		
		
		return clone;
	}
	// Clone un Joueur_Generique sans Moteur
	//TODO
	//Rajouter les IA en plus
	public Joueur_Generique clone()
	{
		// Instancie le clone en fonction du type de la source
		Joueur_Generique clone = new Joueur_Humain(this.getCouleur());
		
		if(this instanceof IA_Random)
		{
			clone = new IA_Random(this.getCouleur(),((IA_Generique) this).m);
		}
		else if(this instanceof IA_Heuristique)
		{
			clone = new IA_Heuristique(this.getCouleur(),((IA_Generique) this).m);
		}
		else if(this instanceof IA_Moyenne)
		{
			clone = new IA_Moyenne(this.getCouleur(),((IA_Generique) this).m);
		}
		clone.setHutte(getHutte());
		clone.setTemple(getTemple());
		clone.setTour(getTour());
		clone.setHutteDetruite(getHutteDetruite());
		clone.setNomFaction(getnomFaction());
		for(BatimentCountListener listener : getBatimentCountListeners()) 
		{
			clone.addBatimentCountListener(listener);
		}
		
		
		return clone;
	}
	
	
	// Listener
	

	//Ajoute un ecouteur d'Etat listener
	public void addBatimentCountListener(BatimentCountListener listener)
	{
		
		listeners.add(BatimentCountListener.class, listener);
	}
	
	//Supprime un ecouteur d'Etat listener
	public void removeBatimentCountListener(BatimentCountListener listener)
	{
		listeners.remove(BatimentCountListener.class, listener);
	}
	//Appel tous les listeners pour les mettre à jour
	public void MajListeners()
	{
		for(BatimentCountListener listener : getBatimentCountListeners()) 
		{
			listener.MajBatimentCount(this,this.hutte,this.tour,this.temple);
		}
	}
	

	//Renvoi la liste des ecouteurs d'Etat
	public BatimentCountListener[] getBatimentCountListeners()
	{
		 return listeners.getListeners(BatimentCountListener.class);
	}
	
	public void CleanListeners()
	{
		for(BatimentCountListener listener : getBatimentCountListeners()) 
		{
			listeners.remove(BatimentCountListener.class, listener);
		}
	}
	
	public void afficher_Joueur()
	{
		System.out.println("#####JOUEUR#####");
		System.out.println("");
		System.out.println("Couleur :" + c);
		System.out.println("Hutte :"+ hutte);
		System.out.println("Tour :" + tour);
		System.out.println("Temple : "+ temple);
	}

	public Actions_Tour get_coup_tour(Tuile tuile) {
		return null;
	}
	
}

package main;

public class Case {
	
	public enum Couleur_Joueur{
		ROUGE,
		JAUNE,
		BLANC,
		MARRON,
		NEUTRE;
	}
	
	public enum Type{
		VOLCAN,
		MONTAGNE,
		PLAINE,
		LAC,
		SABLE,
		FORET,
		VIDE; // <=> niveau = 0
	}
	
	public enum Orientation{
		N,
		N_E,
		N_O,
		S,
		S_O,
		S_E,
		E,
		O,
		NONE;
	}
	
	public enum Type_Batiment{
		HUTTE,
		TOUR,
		TEMPLE,
		VIDE;
	}
	
	private Type type;	// type = VIDE ssi niveau = 0
	private Orientation orientation;
	private Type_Batiment bt;
	private int nb_b;
	private Couleur_Joueur c;
	private int niveau; // niveau = 0 ssi type = VIDE
	
	public Case(Type type){
		this.type = type;
		orientation = Orientation.NONE;
		nb_b = 0;
		bt = Type_Batiment.VIDE;
		c = Couleur_Joueur.NEUTRE;
		niveau = (type == Type.VIDE) ? 0 : 1;
	}
	
	public Case(Type type, Orientation orientation){
		this.type = type;
		this.orientation = (type == Type.VOLCAN) ? orientation : Orientation.NONE;
		nb_b = 0;
		bt = Type_Batiment.VIDE;
		c = Couleur_Joueur.NEUTRE;
		niveau = (type == Type.VIDE) ? 0 : 1;
	}
	
	public Case clone(){
		Case tmp = new Case(this.type, this.orientation);
		tmp.bt = this.bt;
		tmp.nb_b = this.nb_b;
		tmp.niveau = this.niveau;
		tmp.c = this.c;
		return tmp;
	}
	
	// Renvoie le type de la case
	public Type getType(){
		return type;
	}
	
	public void setType(Type t){
		if(t != Type.VIDE || niveau > 0) type = t;
	}
	
	// Renvoie vrai ssi la case est vide (c'est un trou)
	public boolean est_Vide(){
		return niveau==0;
	}
	
	// Renvoie vrai ssi la case est libre (pas de batiments dessus)
	public boolean est_Libre(){
		return nb_b==0;
	}
	
	// Renvoie l'orientation de la case. N'a de sens que si getType()=OrientationCase.VOLCAN
	public Orientation getOrientation(){
		return orientation;
	}
	
	public void setOrientation(Orientation o){
		orientation = o;
	}
	
	// Renvoie le niveau de la case
	public int getNiveau(){
		return niveau;
	}
	
	// Augmente le niveau de la case de 1
	public void incrNiveau(){
		niveau ++ ;
	}
	
	// Renvoie le nombre de batiments sur la case.
	public int getBNb(){
		return nb_b;
	}
	
	// Renvoie le type de batiment sur la case.
	public Type_Batiment getBType(){
		return bt;
	}
	
	// Renvoie la couleur des batiments sur la case.
	public Couleur_Joueur getCouleur(){
		return c;
	}

	// Ajoute n batiments bt de couleur c sur la case.
	// Renvoie 0 si le placement était autorisé et a réussi, 1 sinon.
	public int ajouter_batiment(Type_Batiment bt, int n, Couleur_Joueur c){
		if(n>0 && ajout_batiment_autorise(bt,n)){
			nb_b = n;
			this.bt = bt;
			this.c = c;
			return 0;
		}
		else return 1;
	}
	
	// Renvoie vrai ssi le placement de n batiments de type bt est autorise sur cette case.
	public boolean ajout_batiment_autorise(Type_Batiment bt, int n){
		if(type != Type.VOLCAN && nb_b == 0 && n>=0){
			if(bt==Type_Batiment.HUTTE){
				return (n == niveau);
			}
			else{
				return (n == 1);
			}
		}
		else return false;
	}
	
	// Retire tous les batiments de la case. Renvoie 1 si aucun batiment n'était présent, 0 sinon.
	public int retirer_batiments(){
		if(nb_b > 0){
			nb_b = 0;
			bt = Type_Batiment.VIDE;
			c = Couleur_Joueur.NEUTRE;
			return 0;
		}
		else return 1;
	}
}

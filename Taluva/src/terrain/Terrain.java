package terrain;

import java.awt.Point;
import java.util.ArrayList;
import main.Action_Construction;
import main.Action_Tuile;

public class Terrain {
	
	public class Coord{
		public int xmin,ymin,xmax,ymax;
		public Coord(int xmin, int ymin, int xmax, int ymax){
			this.xmin = xmin;
			this.ymin = ymin;
			this.xmax = xmax;
			this.ymax = ymax;
		}
		public Coord clone(){
			return new Coord(xmin,ymin,xmax,ymax);
		}
	}
	
	public final static int TAILLE = 10;
	public final static Point CENTRE = new Point(TAILLE/2,TAILLE/2);
	
	private Case [][] t;
	private boolean empty;
	private Coord limites;
	
	private ArrayList<Action_Tuile> histo_tuiles;
	
	public Terrain(){
		t = new Case[TAILLE][TAILLE];
		for(int i=0;i<TAILLE;i++){
			for(int j=0;j<TAILLE;j++){
				t[i][j] = new Case(Case.Type.VIDE);
			}
		}
		limites  = new Coord(CENTRE.x,CENTRE.y,CENTRE.x,CENTRE.y);
		empty = true;
		histo_tuiles = new ArrayList<Action_Tuile>();
	}
	
	@SuppressWarnings("unchecked")
	public Terrain clone(){
		Terrain tmp = new Terrain();
		for(int i=0;i<TAILLE;i++){
			for(int j=0;j<TAILLE;j++){
				tmp.t[i][j] = this.t[i][j].clone();
			}
		}
		tmp.limites  = this.limites.clone();
		tmp.empty = this.empty;
		tmp.histo_tuiles = (ArrayList<Action_Tuile>) this.histo_tuiles.clone();
		if(!tmp.histo_tuiles.equals(this.histo_tuiles)) System.out.println("Erreur clone Terrain");
		return tmp;
	}
	
	public Case getCase(Point P){
		return getCase(P.x,P.y);
	}
	
	public Case getCase(int i, int j){
		if(i>=0 && j>=0 && i<TAILLE && j<TAILLE)
			return t[i][j];
		else
			System.out.println("Erreur : Terrain.get(" + i + "," + j + ")");
			return new Case(Case.Type.VIDE);
	}
	
	public boolean isEmpty(){
		return empty;
	}
	
	public ArrayList<Action_Tuile> getHistoTuiles(){
		return histo_tuiles;
	}
	
	// Renvoie les coordonnees limites du terrain : toutes les tuiles sont comprises dans
	// (xmin,ymin)--------|
	//      |             |
	//      |--------(xmax,ymax)
	public Coord getLimites(){
		return limites;
	}
	
	//			    ___/ 3,0 \
	//			   /   \     /
	//		   ___/ 2,0 \___/
	//		  /   \     /   \
	//	  ___/ 1,0 \___/ 3,1 \
	//	 /	 \     /   \     /
	//	/ 0,0 \___/ 2,1 \___/
	//  \     /   \     /   \
	//   \___/ 1,1 \___/ 3,2 \
	//   /   \     /   \     /
	//  / 0,1 \___/ 2,2 \___/
	//  \     /   \     /   \
	//   \___/ 1,2 \___/ 3,3 \
	

/*	
	// Renvoie les 6 voisins de la case au Point P
	private Case [] getVoisins(Point P){
		Case [] res = new Case[6];
		Point [] pts = getPtsVoisins(P);
		for(int i=0; i<6; i++){
			res[i]=getCase(pts[i]);
		}
		return res;
	}
*/
	
	private Point [] getPtsVoisins(Point P){
		Point [] res = new Point[6];
		int x = P.x;
		int y = P.y;
		res[0] = new Point(x-1,y-1);
		res[1] = new Point(x-1,y);
		res[2] = new Point(x,y-1);
		res[3] = new Point(x+1,y);
		res[4] = new Point(x,y+1);
		res[5] = new Point(x+1,y+1);
		return res;
	}
	
	// Renvoie la liste des cases calculee depuis une liste de points
	private ArrayList<Case> getCases(ArrayList<Point> pts){
		ArrayList<Case> res = new ArrayList<Case>();
		for(int i=0;i<pts.size();i++){
			res.add(getCase(pts.get(i)));
		}
		return res;
	}
	
	///////////////////////////////////////////////////////////////////////////
	///////////////////////////// PLACEMENT TUILE /////////////////////////////
	///////////////////////////////////////////////////////////////////////////
	
	//	Position pour le placement :
	//		     _	    _
	//		   _/X\    /X\_
	// GAUCHE / \_/    \_/ \ DROITE
	//	 	  \_/ \    / \_/
	//		    \_/    \_/
	
	// Renvoie le Terrain apres placement de tuile au point P. Ne modifie pas la structure actuelle.
	public Terrain consulter_coup_tuile(Tuile tuile, Point P){
		Terrain T = this.clone();
		T.placer_tuile(tuile, P);
		return T;
	}
	
	// Place la tuile donnee au point P. Renvoie 0 si la tuile a pu etre placee, 1 sinon.
	public int placer_tuile(Tuile tuile, Point P){
		int x;
		int y;
		if(placement_tuile_autorise(tuile,P)){
			if(empty){
				empty = false;
				x = CENTRE.x;
				y = CENTRE.y;
			}
			else{
				x = P.x;
				y = P.y;
			}
			t[x][y].setType(tuile.get_type_case(Case.Orientation.N));
			t[x][y].setOrientation(tuile.get_Orientation_Volcan());
			t[x][y].incrNiveau();
			t[x][y].retirer_batiments();
			t[x][y+1].setType(tuile.get_type_case(Case.Orientation.S));
			t[x][y+1].setOrientation(tuile.get_Orientation_Volcan());
			t[x][y+1].incrNiveau();
			t[x][y+1].retirer_batiments();
			if(y+1>limites.ymax) limites.ymax = y+1;
			if(y<limites.ymin) limites.ymin = y;
			if(tuile.getOrientation()==Tuile.Orientation.GAUCHE){
				if(x>limites.xmax) limites.xmax = x;
				if(x-1<limites.xmin) limites.xmin = x-1;
				t[x-1][y].setType(tuile.get_type_case(Case.Orientation.O));
				t[x-1][y].setOrientation(tuile.get_Orientation_Volcan());
				t[x-1][y].incrNiveau();
				t[x-1][y].retirer_batiments();
			}
			else{
				if(x+1>limites.xmax) limites.xmax = x+1;
				if(x<limites.xmin) limites.xmin = x;
				t[x+1][y+1].setType((tuile.get_type_case(Case.Orientation.E)));
				t[x+1][y+1].setOrientation(tuile.get_Orientation_Volcan());
				t[x+1][y+1].incrNiveau();
				t[x+1][y+1].retirer_batiments();
			}
			histo_tuiles.add(new Action_Tuile(tuile,new Point(x,y),t[x][y].getNiveau()));
			return 0;
		}
		else{
			return 1;
		}
	}
	
	// Renvoie vrai ssi la tuile est dans le terrain
	private boolean dans_terrain(Tuile.Orientation o, Point P){
		if(o == Tuile.Orientation.GAUCHE)
			return P.x > 1 && P.y >= 1 && P.x < TAILLE-1 && P.y < TAILLE-2;
		else
			return P.x >= 1 && P.y >= 1 && P.x < TAILLE-2 && P.y < TAILLE-2;
	}

	// Renvoie les 3 cases de la tuile
	private Case [] cases_tuile(Tuile.Orientation o, Point P){
		int x = P.x;
		int y = P.y;
		Case [] res = new Case[3];
		res[0] = t[x][y];
		res[1] = t[x][y+1];
		if(o == Tuile.Orientation.GAUCHE){
			res[2] = t[x-1][y];
		}
		else{
			res[2] = t[x+1][y+1];
		}
		return res;
	}
	
	// Renvoie la liste des cases en contact avec la tuile
	private ArrayList<Point> contact(Tuile.Orientation o, Point P){
		int x = P.x;
		int y = P.y;
		ArrayList<Point> res = new ArrayList<Point>();
		res.add(new Point(x,y-1));
		res.add(new Point(x,y+2));
		res.add(new Point(x+1,y));
		res.add(new Point(x-1,y+1));
		if(o == Tuile.Orientation.GAUCHE){
			res.add(new Point(x-2,y));
			res.add(new Point(x+1,y+1));
			res.add(new Point(x-1,y-1));
		}
		else{
			res.add(new Point(x+1,y+2));
			res.add(new Point(x+2,y+1));
			res.add(new Point(x-1,y));
		}
		return res;
	}
	
	// Renvoie vrai ssi la tuile est en contact avec au moins une autre tuile
	private boolean en_contact(Tuile.Orientation o, Point P){
		boolean trouve = false;
		ArrayList<Point> voisins = contact(o,P);
		int i = 0;
		while(!trouve && i<voisins.size()){
			trouve = !getCase(voisins.get(i)).est_Vide();
			i++;
		}
		return trouve;
	}
	
	// Renvoie vrai ssi le placement de cette tuile est autorisé au point P.
	public boolean placement_tuile_autorise(Tuile tuile, Point P){
		//TODO on ne gere pas le cas d'ecraser une cite entiere
		if(empty) return true;
		if(!dans_terrain(tuile.getOrientation(),P)) return false;
		else{
			// Teste si la tuile est posée sur d'autres tuiles
			int x = P.x;
			int y = P.y;
			int n0,n1,n2;
			Case [] cases_t = cases_tuile(tuile.getOrientation(),P);	// Les cases de la tuile
			n0 = cases_t[0].getNiveau();
			n1 = cases_t[1].getNiveau();		// On regarde les niveaux en-dessous de la tuile
			n2 = cases_t[2].getNiveau();
			if(n0>0 || n1>0 || n2>0){
				// Si on tente de jouer sur au moins une tuile
				if(n0==n1 && n1==n2){
					// Si les 3 cases dessous sont au même niveau
					// On joue alors sur des tuiles, on vérifie la disposition des volcans
					if(tuile.get_type_case(Case.Orientation.N)==Case.Type.VOLCAN){
						// Si le Volcan est au Nord
						if(t[x][y].getType()==Case.Type.VOLCAN){
							return t[x][y].getOrientation() != tuile.get_Orientation_Volcan();
						}
						else return false;
					}
					else if(tuile.get_type_case(Case.Orientation.S)==Case.Type.VOLCAN){
						// Si le Volcan est au Sud
						if(t[x][y+1].getType()==Case.Type.VOLCAN){
							return t[x][y+1].getOrientation() != tuile.get_Orientation_Volcan();
						}
						else return false;
					}
					else{
						// Si le Volcan est sur le coté
						if(tuile.getOrientation()==Tuile.Orientation.GAUCHE){
							if(tuile.get_type_case(Case.Orientation.O)==Case.Type.VOLCAN){
								if(t[x-1][y].getType()==Case.Type.VOLCAN){
									return t[x-1][y].getOrientation() != tuile.get_Orientation_Volcan();
								}
								else return false;
							}
							else{
								System.out.println("Erreur : pas de Volcan sur cette tuile !");
								return false;
							}
						}
						else{
							if(tuile.get_type_case(Case.Orientation.E)==Case.Type.VOLCAN){
								if(t[x+1][y+1].getType()==Case.Type.VOLCAN){
									return t[x+1][y+1].getOrientation() != tuile.get_Orientation_Volcan();
								}
								else return false;
							}
							else{
								System.out.println("Erreur : Pas de volcan sur cette tuile !");
								return false;
							}
						}
					}
				}
				else{
					// On essaye de jouer partiellement sur de tuiles ou de niveaux différents : interdit
					return false;
				}
			}
			else{
				// On joue au niveau 1, il faut que ce soit en contact
				return en_contact(tuile.getOrientation(),P);
			}
		}
	}
	
	///////////////////////////////////////////////////////////////////////////
	///////////////////////////// EXTENSION CITE //////////////////////////////
	///////////////////////////////////////////////////////////////////////////
	
	// Renvoie le nombre de huttes necessaires a l'extension de la cite presente au point P sur les cases de Type type.
	public int nb_huttes_extension(Point P, Case.Type type){
		ArrayList<Point> ptsCite = getPtsCite(P);
		return getCases_extension_cite(ptsCite,type).size();
	}
	
	// Renvoie le Terrain apres extension d'une cite presente au point P sur les cases de Type type.
	// Ne modifie pas la structure actuelle.
	public Terrain consulter_extension_cite(Point P, Case.Type type){
		Terrain T = this.clone();
		T.etendre_cite(P,type);
		return T;
	}
	
	// Etend la cite presente au point P sur les cases de Type type.
	// Renvoie 0 si l'extension a reussi, 1 sinon
	public int etendre_cite(Point P, Case.Type type){
		Case.Couleur_Joueur c = getCase(P).getCouleur();
		ArrayList<Point> ptsCite = getPtsCite(P);
		ArrayList<Case> cases_extension = getCases_extension_cite(ptsCite,type);
		if(cases_extension.size()>0){
			for(int i=0;i<cases_extension.size();i++){
				cases_extension.get(i).ajouter_batiment(Case.Type_Batiment.HUTTE, c);
			}
			return 0;
		}
		else return 1;
	}
	
	// Renvoie l'ensemble des cases concernees par l'extension de la cite sur les cases de Type type.
	private ArrayList<Case> getCases_extension_cite(ArrayList<Point> ptsCite, Case.Type type){
		ArrayList<Case> res = new ArrayList<Case>();
		boolean [][] appartient_res = new boolean[TAILLE][TAILLE];
		for(int i=0;i<TAILLE;i++){
			for(int j=0;j<TAILLE;j++){
				appartient_res[i][j] = false;
			}
		}
		if(type != Case.Type.VIDE){
			for(int i=0;i<ptsCite.size();i++){
				Point [] voisins = getPtsVoisins(ptsCite.get(i));
				for(int j=0;j<6;j++){
					if(getCase(voisins[j]).getType() == type && !appartient_res[voisins[j].x][voisins[j].y] && getCase(voisins[j]).getBNb() == 0){
						res.add(getCase(voisins[j]));
					}
				}
			}
		}
		return res;
	}
	
	// Renvoie vrai ssi la cite donnee contient au moins un batiment de type b
	private boolean cite_contient(ArrayList<Case> cite, Case.Type_Batiment b){
		boolean trouve = false;
		int i = 0;
		while(!trouve && i<cite.size()){
			trouve = cite.get(i).getBType() == b;
			i++;
		}
		return trouve;
	}
	
	// Renvoie vrai ssi la cite donnee est de taille >=3
	private boolean cite_taille_3(ArrayList<Case> cite){
		return cite.size()>=3;
	}

	// Renvoie la liste des cases associees a la cite en P
	private ArrayList<Case> getCite(Point P){
		return getCases(getPtsCite(P));
	}
	
	// Calcule dans res l'ensemble des points de la cite liee a P
	private ArrayList<Point> getPtsCite(Point P){
		ArrayList<Point> ptsCite = new ArrayList<Point>();
		boolean [][] appartient_cite = new boolean[TAILLE][TAILLE];
		for(int i=0;i<TAILLE;i++){
			for(int j=0;j<TAILLE;j++){
				appartient_cite[i][j] = false;
			}
		}
		getPtsCite_rec(P,ptsCite,appartient_cite);
		return ptsCite;
	}
	
	// Recursion de la fonction ci-dessus
	private void getPtsCite_rec(Point P, ArrayList<Point> res, boolean [][] appartient_cite){
		Point [] voisins = getPtsVoisins(P);
		res.add(P);
		appartient_cite[P.x][P.y] = true;
		Case.Couleur_Joueur c = getCase(P).getCouleur();
		for(int i=0;i<6;i++){
			if(getCase(voisins[i]).getCouleur() == c && !appartient_cite[voisins[i].x][voisins[i].y]){
				getPtsCite_rec(voisins[i],res,appartient_cite);
			}
		}
	}
	
	///////////////////////////////////////////////////////////////////////////
	/////////////////////////// PLACEMENT BATIMENT ////////////////////////////
	///////////////////////////////////////////////////////////////////////////
	
	// Renvoie le Terrain apres placement direct d'un batiment b de couleur c au point P.
	// Ne modifie pas la structure actuelle.
	public Terrain consulter_coup_batiment(Case.Type_Batiment b, Case.Couleur_Joueur c, Point P){
		Terrain T = this.clone();
		T.placer_batiment(b,c,P);
		return T;
	}
	
	// Place directement un batiment de type b au point P (hors extension de cite).
	// Renvoie 0 si le placement a reussi, 1 sinon.
	public int placer_batiment(Case.Type_Batiment b, Case.Couleur_Joueur c, Point P){
		if(placement_batiment_autorise(b,c,P)){
			return getCase(P).ajouter_batiment(b,c);
		}
		else return 1;
	}
	
	// Renvoie vrai ssi le placement direct d'un batiment de type b au point P est autorise.
	public boolean placement_batiment_autorise(Case.Type_Batiment b, Case.Couleur_Joueur c, Point P){
		if(getCase(P).ajout_batiment_autorise(b)){
			// Si le placement est autorise sur la case (independemment du reste du terrain)
			Point [] ptsVoisins = getPtsVoisins(P);
			int i = 0;
			boolean cite_trouvee = false;
			Point coord_cite = new Point();
			while(!cite_trouvee && i<6){
				if(!getCase(ptsVoisins[i]).est_Libre() && getCase(ptsVoisins[i]).getCouleur()==c){
					cite_trouvee = true;
					coord_cite = ptsVoisins[i];
				}
				// TODO : on ne gere pas le cas de connexion de deux cites
				i++;
			}
			if(cite_trouvee){
				// C'est l'ajout d'une tour ou d'un temple (sinon c'est interdit)
				ArrayList<Case> cite = getCite(coord_cite);
				if(b == Case.Type_Batiment.TOUR)
					return (getCase(P).getNiveau() >= 3 && !cite_contient(cite,Case.Type_Batiment.TOUR));
				if(b == Case.Type_Batiment.TEMPLE)
					return (cite_taille_3(cite) && !cite_contient(cite,Case.Type_Batiment.TEMPLE));
				return false;
			}
			else{
				// C'est une nouvelle cite : ce doit etre une hutte au niveau 1
				return (b == Case.Type_Batiment.HUTTE) && getCase(P).getNiveau()==1;
			}
		}
		else return false;
	}
	
	public ArrayList<Action_Construction> liste_coups_construction_possibles(Case.Couleur_Joueur c){
		ArrayList<Action_Construction> res = new  ArrayList<Action_Construction>();
		Point P;
		int nb;
		for(int i=limites.xmin;i<limites.xmax;i++){
			for(int j=limites.ymin;j<limites.ymax;j++){
				P = new Point(i,j);
				if(placement_batiment_autorise(Case.Type_Batiment.HUTTE, c, P))
					res.add(new Action_Construction(Action_Construction.Type.HUTTE, P));
				if(placement_batiment_autorise(Case.Type_Batiment.TOUR, c, P))
					res.add(new Action_Construction(Action_Construction.Type.TOUR, P));
				if(placement_batiment_autorise(Case.Type_Batiment.TEMPLE, c, P))
					res.add(new Action_Construction(Action_Construction.Type.TEMPLE, P));
				if(getCase(P).getCouleur() == c){
					if((nb = nb_huttes_extension(P,Case.Type.FORET))>0)
						res.add(new Action_Construction(P,Case.Type.FORET,nb));
					if((nb = nb_huttes_extension(P,Case.Type.LAC))>0)
						res.add(new Action_Construction(P,Case.Type.LAC,nb));
					if((nb = nb_huttes_extension(P,Case.Type.PLAINE))>0)
						res.add(new Action_Construction(P,Case.Type.PLAINE,nb));
					if((nb = nb_huttes_extension(P,Case.Type.SABLE))>0)
						res.add(new Action_Construction(P,Case.Type.SABLE,nb));
					if((nb = nb_huttes_extension(P,Case.Type.MONTAGNE))>0)
						res.add(new Action_Construction(P,Case.Type.MONTAGNE,nb));
				}
			}
		}
		return res;
	}
	
	// Renvoie la liste des emplacements possibles pour la Tuile tuile
	public ArrayList<Action_Tuile> liste_coups_tuile_possibles(Tuile tuile){
		ArrayList<Action_Tuile> res = new  ArrayList<Action_Tuile>();
		for(int o=0;o<6;o++){
			for(int i=limites.xmin-2;i<limites.xmax+2;i++){
				for(int j=limites.ymin-2;j<limites.ymax+1;j++){
					if(placement_tuile_autorise(tuile, new Point(i,j)))
						res.add(new Action_Tuile(tuile.clone(),new Point(i,j),getCase(i,j).getNiveau()+1));
				}
			}
			tuile.Tourner_horaire();
		}
		return res;
	}
	
	// Affiche le terrain dans la console
	public void afficher(){
		System.out.println("  " + limites.xmin + " - " + limites.xmax);
		System.out.println(limites.ymin);
		System.out.println("-");
		System.out.println(limites.ymax);
		for(int i=limites.ymin;i<=limites.ymax;i++){
			for(int j=limites.xmin;j<=limites.xmax;j++){
				switch (t[j][i].getType()){
				case FORET:
					System.out.print("F");
					break;
				case LAC:
					System.out.print("L");
					break;
				case MONTAGNE:
					System.out.print("M");
					break;
				case PLAINE:
					System.out.print("P");
					break;
				case SABLE:
					System.out.print("S");
					break;
				case VIDE:
					System.out.print(" ");
					break;
				case VOLCAN:
					System.out.print("V");
					break;
				default:
					break;
				}
			}
			System.out.print("  ");
			for(int j=limites.xmin;j<=limites.xmax;j++){
				System.out.print(t[j][i].getNiveau());
			}
			System.out.print("  ");
			for(int j=limites.xmin;j<=limites.xmax;j++){
				switch (t[j][i].getBType()){
				case HUTTE: 
					System.out.print("H");
					break;
				case TEMPLE:
					System.out.print("A");
					break;
				case TOUR:
					System.out.print("T");
					break;
				case VIDE:
					System.out.print(" ");
					break;
				default:
					break;
				}
			}
			System.out.print("  ");
			for(int j=limites.xmin;j<=limites.xmax;j++){
				System.out.print(t[j][i].getBNb());
			}
			System.out.println("");
		}
		System.out.println("");
	}
}
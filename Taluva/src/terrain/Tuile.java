package terrain;

public class Tuile {
	public enum Orientation{
		GAUCHE,
		DROITE;
	}
	
	private Case.Type[] type_case;
	private Case.Orientation o;
	
//  	Indices :
//		     _	    _
//		   _/N\    /N\_
// GAUCHE /O\_/    \_/E\ DROITE
//	 	  \_/S\    /S\_/
//		    \_/    \_/

// Crée une tuile (GAUCHE) avec le volcan à OUEST, le type 2 au NORD, et le type3 au SUD.
	public Tuile (Case.Type case2, Case.Type case3)
	{
		type_case = new Case.Type[3];
		type_case[0] = Case.Type.VOLCAN;
		type_case[1] = case2;
		type_case[2] = case3;
		o = Case.Orientation.O;
	}
	
	public Tuile clone(){
		Tuile tmp = new Tuile(type_case[1],type_case[2]);
		tmp.o = this.o;
		return tmp;
	}

	// Retourne l'orientation Gauche/Droite de la tuile.
	public Orientation getOrientation()
	{
		if(OrienteDroite())
		{
			return Orientation.DROITE;
		}
		return Orientation.GAUCHE;
	}
	
	// Tourne la tuile dans le sens Horaire. Renvoie 1 si opération réussie.
	public int Tourner_horaire() throws Exception
	{
		switch (o)
		{
			case O:
				o = Case.Orientation.N_O;
				return 0;
			case N_O:
				o = Case.Orientation.N_E;
				return 0;
			case N_E:
				o = Case.Orientation.E;
				return 0;
			case E:
				o = Case.Orientation.S_E;
				return 0;
			case S_E:
				o = Case.Orientation.S_O;
				return 0;
			case S_O:
				o = Case.Orientation.O;
				return 0;
			default:
				throw new Exception("L'orientation n'est pas correct");
		}
	}
	
	// Tourne la tuile dans le sens anti-Horaire. Renvoie 1 si opération réussie.
	public int Tourner_anti_horaire() throws Exception 
	{
		switch (o)
		{
			case O:
				o = Case.Orientation.S_O;
				return 0;
			case S_O:
				o = Case.Orientation.S_E;
				return 0;
			case S_E:
				o = Case.Orientation.E;
				return 0;
			case E:
				o = Case.Orientation.N_E;
				return 0;
			case N_E:
				o = Case.Orientation.N_O;
				return 0;
			case N_O:
				o = Case.Orientation.O;
				return 0;
			default:
				throw new Exception("L'orientation n'est pas correct");
		}
	}
	
	// Retourne l'orientation du volcan
	public Case.Orientation get_Orientation_Volcan()
	{
		return o;
	}
	
	// Renvoie vrai si la pointe est vers la droite.
	private boolean OrienteDroite()
	{
		return o == Case.Orientation.E || o == Case.Orientation.N_O || o == Case.Orientation.S_O;
	}
	
	// Renvoie le type de la case désigné par la direction indiqué.
	public Case.Type get_type_case(Case.Orientation orientation) throws Exception
	{
		if(OrienteDroite())
		{
			switch(orientation)
			{
				case N:
					switch(o){
					case E: return type_case[2];
					case N_O: return type_case[0];
					case S_O: return type_case[1];
					default:
					}
				case E:
					switch(o){
					case E: return type_case[0];
					case N_O: return type_case[1];
					case S_O: return type_case[2];
					default:
					}
				case S:
					switch(o){
					case E: return type_case[1];
					case N_O: return type_case[2];
					case S_O: return type_case[0];
					default:
					}
				default:
					throw new Exception("L'orientation demandée n'est pas correct");
			}
		}
		else{
			switch(orientation)
			{
				case O:
					switch(o){
					case O: return type_case[0];
					case N_E: return type_case[2];
					case S_E: return type_case[1];
					default:
					}
				case N:
					switch(o){
					case O: return type_case[1];
					case N_E: return type_case[0];
					case S_E: return type_case[2];
					default:
					}
				case S:
					switch(o){
					case O: return type_case[2];
					case N_E: return type_case[1];
					case S_E: return type_case[0];
					default:
					}
				default:
					throw new Exception("L'orientation demandée n'est pas correct");
			}
		}
	}
}
/*
public class Tuile {
	
	public enum Orientation{
		GAUCHE,
		DROITE;
	}
	private Case.Type [] t;
	private Orientation o;
	
	//		  	Indices :
	//		     _	    _
	//		   _/2\    /1\_
	// GAUCHE /0\_/    \_/3\ DROITE
	//	 	  \_/4\    /5\_/
	//		    \_/    \_/
	
	// Crée une tuile (GAUCHE) avec le volcan à gauche, typeb en bas et typeh en haut.
	Tuile(Case.Type typeb, Case.Type typeh){
		t = new Case.Type[6];
		o = Orientation.GAUCHE;
		t[0] = Case.Type.VOLCAN;
		t[1] = Case.Type.VOLCAN;
		t[2] = typeh;
		t[3] = typeh;
		t[4] = typeb;
		t[5] = typeb;
	}
	
	private void swap_Orientation(){
		o = (o == Orientation.GAUCHE) ? Orientation.DROITE : Orientation.GAUCHE;
	}
	
	public Orientation getOrientation(){
		return o;
	}
	
	// Tourne la tuile d'1/6 de tour en sens horaire
	public void tourner_horaire(){
		int i = (o == Orientation.GAUCHE) ? 0 : 1;
		for(;i<5;i+=2){
			t[i+1]=t[i];
		}
		if(o == Orientation.DROITE) t[0] = t[5];
		swap_Orientation();
	}

	// Tourne la tuile d'1/6 de tour en sens anti-horaire
	public void tourner_anti_horaire(){
		swap_Orientation();
		int i = (o == Orientation.GAUCHE) ? 0 : 1;
		for(;i<5;i+=2){
			t[i]=t[i+1];
		}
		if(o == Orientation.DROITE) t[5] = t[0];
	}
	
	public Case.Type get(int i)
	{
		if(0<=i && i <3)
		{
			if(o == Orientation.GAUCHE)
				return t[2*i];
			else
				return t[2*i +1];
		}
		else
		{
			System.out.println("Tuile: Erreur l'index est inccorect. (get)");
			return Case.Type.VIDE;
		}
	}
}
*/
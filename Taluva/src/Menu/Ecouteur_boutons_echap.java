package Menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Ecouteur_boutons_echap implements ActionListener{
	String action;
	Echap echap;
	
	Ecouteur_boutons_echap(String action,Echap echap){
		this.action = action;
		this.echap = echap;
	}
	
	public void actionPerformed(ActionEvent e) {
		switch (action){
			case "Reprendre" :
				echap.reprendre();		
				break;
			case "Nouveau" :
				echap.nouveau();
				break;
			case "Comment jouer" :
				echap.comment_jouer();
				break;
			case "Sauvegarder" :
				echap.sauvegarder();
				break;
			case "Charger" :
				echap.charger();
				break;
			case "Menu Principal" :
				echap.menu_principal();
				break;
			case "Quitter" :
				echap.quitter();
				break;
			default :
				break;
		}
	}
}

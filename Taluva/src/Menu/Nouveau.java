package Menu;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import IHM.Avancement;
import IHM.IHM;
import Joueur.IA_Alpha_Beta;
import Joueur.IA_Heuristique;
import Joueur.IA_Random;
import Joueur.Joueur_Generique;
import Joueur.Joueur_Humain;
import Moteur.Moteur;
import terrain.Terrain;
import terrain.Case.Couleur_Joueur;
import test.Game;

@SuppressWarnings("serial")
public class Nouveau extends JComponent {

	//private Image backgroundImage;
	
	private JFrame m_fenetre;
	private JFrame principal;
	
	private JLabel joueur_1;
	private JComboBox<String> humain_j1;
	private JComboBox<String> faction_j1;
	
	private JLabel joueur_2;
	private JComboBox<String> humain_j2;
	private JComboBox<String> faction_j2;
	
	private JButton avance;
	
	private JButton accueil;
	private JButton lancer;
	
	//POUR LANCER UNE PARTIE
	private JFrame gameF;
	private Game game;
	private Moteur moteur;
	private Terrain terrain;
	private IHM ihm;
	private Avancement avancement;
	private Joueur_Generique j1,j2;
	
	// L'INSTANCIATION
	
	public Nouveau(JFrame frame,JFrame gameF,Game game,Moteur moteur,Terrain terrain,IHM ihm,Avancement avancement){//, Moteur moteur){
		
		// Initialisation des paramètres necessaires pour le lancement d'une partie
		this.gameF = gameF;
		this.game = game;
		this.moteur = moteur;
		this.terrain = terrain;
		this.ihm = ihm;
		this.avancement = avancement;
		
		
		
		// Initialisation de l'écran de sélection pour une nouvelle partie
		
		init_m_fenetre(frame);
		
		int width = m_fenetre.getWidth();
		int height = m_fenetre.getHeight();
		int width_b = width/9;
		int height_b = height/15;
		
		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		panel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		
		c.insets = new Insets(height_b*2,-width_b,0,0);
		JLabel type = new JLabel("Type de joueur");
		type.setFont(new Font("", Font.BOLD+Font.ITALIC,15));
		c.weightx = 0.5;
		c.weighty = 0.5;
		c.gridheight=1;
		c.gridwidth=1;
		c.gridx = 1;
		c.gridy = 0;
		c.ipadx = 0;
		c.ipady = 0;
		panel.add(type,c);
		c.insets = new Insets(0,0,0,0);
		
		c.insets = new Insets(height_b*2,-width_b*2,0,0);
		JLabel faction = new JLabel("Factions");
		faction.setFont(new Font("", Font.BOLD+Font.ITALIC,15));
		c.gridx = 2;
		c.gridy = 0;
		panel.add(faction,c);
		c.insets = new Insets(0,0,0,0);
		
		c.insets = new Insets(0,width_b,0,0);
		joueur_1 = new JLabel("Joueur 1");
		joueur_1.setFont(new Font("", Font.BOLD+Font.ITALIC,15));
		c.gridx = 0;
		c.gridy = 1;
		panel.add(joueur_1,c);
		c.insets = new Insets(0,0,0,0);
		
		
		String[] type_joueur = {" Humain", " IA_Facile", " IA_Moyenne", " IA_Difficile"};
		String[] factions = {" Orientaux", " Babyloniens", " A définir", " A définir"};
		
		
		c.insets = new Insets(0,-width_b,0,0);
		c.gridx = 1;
		c.gridy = 1;
		humain_j1 = new JComboBox<String>(type_joueur);
		humain_j1.setSelectedIndex(0);
		humain_j1.setFont(new Font("", Font.BOLD+Font.ITALIC,15));
		humain_j1.setPreferredSize( new Dimension(width_b*3/2, height_b) );
		panel.add(humain_j1,c);
		c.insets = new Insets(0,0,0,0);
		
		c.insets = new Insets(0,-width_b*2,0,0);
		c.gridx = 2;
		c.gridy = 1;
		faction_j1 = new JComboBox<String>(factions);
		faction_j1.setSelectedIndex(0);
		faction_j1.setFont(new Font("", Font.BOLD+Font.ITALIC,15));
		faction_j1.setPreferredSize( new Dimension(width_b*3/2, height_b) );
		panel.add(faction_j1,c);
		c.insets = new Insets(0,0,0,0);
		
		//JOUEUR 2
		c.insets = new Insets(0,width_b,0,0);
		c.gridx = 0;
		c.gridy = 3;
		joueur_2 = new JLabel("Joueur 2");
		joueur_2.setFont(new Font("", Font.BOLD+Font.ITALIC,15));
		panel.add(joueur_2,c);
		c.insets = new Insets(0,0,0,0);
		
		c.insets = new Insets(0,-width_b,0,0);
		c.gridx = 1;
		c.gridy = 3;
		humain_j2 = new JComboBox<String>(type_joueur);
		humain_j2.setSelectedIndex(0);
		humain_j2.setFont(new Font("", Font.BOLD+Font.ITALIC,15));
		humain_j2.setPreferredSize( new Dimension(width_b*3/2, height_b) );
		panel.add(humain_j2,c);
		c.insets = new Insets(0,0,0,0);
		
		c.insets = new Insets(0,-width_b*2,0,0);
		c.gridx = 2;
		c.gridy = 3;
		faction_j2 = new JComboBox<String>(factions);
		faction_j2.setSelectedIndex(1);
		faction_j2.setFont(new Font("", Font.BOLD+Font.ITALIC,15));
		faction_j2.setPreferredSize( new Dimension(width_b*3/2, height_b) );
		panel.add(faction_j2,c);
		c.insets = new Insets(0,0,0,0);
		
		c.insets = new Insets(0,width_b,0,0);
		c.gridx = 0;
		c.gridy = 4;
		avance = new JButton("Parametres Avancés");
		avance.addActionListener(new Ecouteur_boutons_nouveau("Paramètres avancés",this));
		avance.setFont(new Font("", Font.BOLD+Font.ITALIC,15));
		avance.setPreferredSize( new Dimension(width_b*2, height_b) );
		panel.add(avance,c);
		c.insets = new Insets(0,0,0,0);
		
		c.insets = new Insets(-height_b,width_b*2,0,0);
		c.gridx = 1;
		c.gridy = 5;
		accueil = new JButton("Accueil");
		accueil.addActionListener(new Ecouteur_boutons_nouveau("Accueil",this));
		accueil.setFont(new Font("", Font.BOLD+Font.ITALIC,15));
		accueil.setPreferredSize( new Dimension(width_b, height_b) );
		panel.add(accueil,c);
		c.insets = new Insets(0,0,0,0);
		
		c.insets = new Insets(-height_b,0,0,0);
		c.gridx = 2;
		c.gridy = 5;
		lancer = new JButton("Lancer partie");
		lancer.addActionListener(new Ecouteur_boutons_nouveau("Lancer",this));
		lancer.setFont(new Font("", Font.BOLD+Font.ITALIC,15));
		lancer.setPreferredSize( new Dimension(width_b*2, height_b) );
		panel.add(lancer,c);
		c.insets = new Insets(0,0,0,0);
		
		m_fenetre.add(panel);
	
	}
	
	private void init_m_fenetre(JFrame frame){
		principal = frame;
		m_fenetre = new JFrame("Nouveau");
		m_fenetre.setLayout(new BorderLayout());
		m_fenetre.setVisible(true);
		m_fenetre.setLocation(frame.getWidth()*5/22, frame.getHeight()*5/22);
		m_fenetre.setSize(frame.getWidth()*6/11,frame.getHeight()*6/11);
		m_fenetre.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		m_fenetre.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent we){
            	principal.setEnabled(true);
	        }
        });
	}
	
	// Actions des boutons
	public void accueil(){
		principal.remove(m_fenetre);
		m_fenetre.dispose();
		principal.setEnabled(true);
	}
	
	// Paremetres Avancés
	// TODO
	public void avance(){
		System.out.println("[Nouveau/Avance] Bouton non défini");
	}
	
	// Vérifie si tout s'est bien passé dans la sélection des paramètres
	// Affiche un message d'erreur détaillé en cas de problème rencontré (même faction des 2 joueurs par exemple)
	// TODO
	//Jaune : Orientaux
	//Bleux : Francais
	//Babyloniens : gris
	//Last : Vert
	
	// Renvoie la couleur correspondant à la faction choisie
	private Couleur_Joueur init_faction(JComboBox<String> faction){
		switch (faction.getSelectedIndex()){
		case 0 : 
			System.out.println("[Nouveau/Init_joueurs] Orientaux");
			return Couleur_Joueur.JAUNE;
		case 1 :
			System.out.println("[Nouveau/Init_joueurs] Bayloniens");
			return Couleur_Joueur.BLEU;
		case 2 :
			System.out.println("[Nouveau/Init_joueurs] Français");
			return Couleur_Joueur.BLANC;
		case 3 :
			System.out.println("[Nouveau/Init_joueurs] Autres");
			return Couleur_Joueur.ROSE;
		default :
			System.out.println("[Nouveau/Init_Faction] switch case default");
			return null;
		}
	}
	
	// Initialise le joueur générique en fonction du type choisi et de la faction choisie
	private Joueur_Generique init_joueurs(Couleur_Joueur couleur,Moteur moteur,JComboBox<String> humain){
		switch (humain.getSelectedIndex()){
			case 0 :
				System.out.println("[Nouveau/Init_joueurs] HUMAIN");
				return new Joueur_Humain(couleur);
			case 1 :
				System.out.println("[Nouveau/Init_joueurs] IA_Random");
				return new IA_Random(couleur,moteur);
			case 2 :
				System.out.println("[Nouveau/Init_joueurs] IA_Heuristique");
				return new IA_Heuristique(couleur,moteur);
			case 3 :
				// TODO
				return null;
			default :
				System.out.println("[Nouveau/Init_joueurs] switch case default");
				return null;
		}
	}
	
	public void lancer(){
		if(faction_j1.getSelectedIndex() != faction_j2.getSelectedIndex()){
			terrain = new Terrain();
			moteur = new Moteur(terrain);
	        j1=null;
	        j2=null;
	        j1 = init_joueurs(init_faction(faction_j1),moteur,humain_j1);
	        j2 = init_joueurs(init_faction(faction_j2),moteur,humain_j2);
	        if(j1==null)System.out.println("[Nouveau/Init_joueurs] J1 Toujours null");
	        if(j2==null)System.out.println("[Nouveau/Init_joueurs] J2 Toujours null");
			
	        //On ferme l'ecran de selection
	        principal.remove(m_fenetre);
			m_fenetre.dispose();
			principal.setEnabled(true);
			principal.setVisible(false);
	        
	        lancer_jeu(j1,j2);
		}
		else 
			JOptionPane.showMessageDialog(m_fenetre, "Les 2 joueurs doivent appartenir à différentes factions!");
		
	}
	
	private void lancer_jeu(Joueur_Generique j1,Joueur_Generique j2){
		moteur.add_j1(j1);
        moteur.add_j2(j2);
        ihm = new IHM(moteur, gameF);
        ihm.run();
        avancement = new Avancement(ihm);
        moteur.addPhaseListener(avancement);
        if(moteur.getJ1()==null)System.out.println("NOOOOPE");
        else{
        moteur.getJ1().addBatimentCountListener(avancement);
        moteur.getJ2().addBatimentCountListener(avancement);
        moteur.MajListeners();
        ihm.getCanvas().setFocusable(false);
        
        gameF.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        
        game = new Game();
        game.init(principal,gameF,moteur,ihm.getCanvas(),ihm.getPioche_Tuile());
        
        gameF.addKeyListener(game);
        gameF.setFocusable(true);
        gameF.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent we){
                int result = JOptionPane.showConfirmDialog(gameF, "Voulez-vous vraiment quitter ?", "Confirmation", JOptionPane.CANCEL_OPTION);
                if(result == JOptionPane.OK_OPTION){
                	game.cleanUp();
                	gameF.setVisible(false);
                	gameF.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            		gameF.dispose();
                }
            }
        });}
	}
	
	
	// TODO
	// Pour rajouter une image en fond
	/*
	public void paintComponent(Graphics g) {
	    super.paintComponent(g);	
	
	    // Draw the background image.
	    g.drawImage(backgroundImage, 0, 0, this.getWidth(),this.getHeight(),this);
	}
	*/
}

package teste;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JButton;

import java.awt.Color;
//import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import teste.Draggable;

public class ink extends JFrame{
	private JFrame main;
	private JFrame managerprofile;
	private JFrame clubmenu;
	private JFrame squad;
	private JFrame tactics;
	private JFrame transfers;
	private JFrame table;
	private JFrame managerdetails;
	private JFrame clubdetails;
	private JFrame play;
	private JLabel background;
	private JLabel title;
	private JButton newgamebutton;
	private JButton loadbutton;
	private JButton quitbutton;
	private JButton backbutton;
	private JButton nextbutton;
	//private JButton squadbutton;
	private JButton tacticsbutton;
	private JButton transfersbutton;
	private JButton tablebutton;
	//private JButton managerbutton;
	//private JButton clubdetailsbutton;
	private JButton savebutton;
	private JButton playbutton;
	//private JButton exitsquadbutton;
	private JButton exittacticsbutton;
	private JButton exittransfersbutton;
	private JButton exittablebutton;
	//private JButton exitmanagerbutton;
	//private JButton exitclubdetailsbutton;
	private JButton okbutton;
	private JButton mainmenubutton;
	
	private static String[] country = {"Select a Country: ","Portugal", "England"}; 
	private static String[] club = {"Select a Club: ", "Real Madrid", "Borussia Dortmund"};

	
	public void startMenu(){
		main = new JFrame("Football Management");
		main.setSize(600, 500);
		
		createContents();
		
		main.setVisible(true);
	}

	protected void createContents() {
		main.getContentPane().setLayout(null);
		
		newgamebutton = new JButton("New Game");
		newgamebutton.setBounds(356, 70, 181, 23);
		main.add(newgamebutton);
		
		loadbutton = new JButton("Continue Game");
		loadbutton.setBounds(356, 130, 181, 23);
		main.add(loadbutton);
		
		quitbutton = new JButton("Quit");
		quitbutton.setBounds(356, 190, 181, 23);
		main.add(quitbutton);
		
		title = new JLabel("Football Management");
		title.setFont(new Font("Arial", Font.PLAIN, 20));
		title.setForeground(Color.WHITE);
		title.setBounds(80, 30, 200, 23);
		main.add(title);
		
		background = new JLabel();
		background.setIcon(new ImageIcon("stadium.jpg"));
		background.setBounds(0, 0, 984, 462);
		main.add(background);
			
		backbutton = new JButton("Back");
		backbutton.setBounds(5, 5, 90, 23);
		
		nextbutton = new JButton("Next");
		nextbutton.setBounds(100, 5, 90, 23);
		
		/*squadbutton = new JButton("Squad");
		squadbutton.setBounds(100, 100, 110, 35);*/
		
		tacticsbutton = new JButton("Tactics");
		tacticsbutton.setBounds(100, 100, 110, 35);
		
		transfersbutton = new JButton("Transfers");
		transfersbutton.setBounds(100, 200, 110, 35);
		
		tablebutton = new JButton("Table");
		tablebutton.setBounds(380, 100, 110, 35);
		
		/*managerbutton = new JButton("Manager");
		managerbutton.setBounds(100, 300, 110, 35);
		
		clubdetailsbutton = new JButton("Club Details");
		clubdetailsbutton.setBounds(380, 300, 110, 35);*/
		
		savebutton = new JButton("Save");
		savebutton.setBounds(240, 300, 110, 35);
		
		playbutton = new JButton("Play");
		playbutton.setBounds(380, 200, 110, 35);
		
		/*exitsquadbutton = new JButton("Exit");
		exitsquadbutton.setBounds(5, 5, 90, 23);*/
		
		exittacticsbutton = new JButton("Exit");
		exittacticsbutton.setBounds(5, 5, 90, 23);
		
		exittransfersbutton = new JButton("Exit");
		exittransfersbutton.setBounds(5, 5, 90, 23);
		
		exittablebutton = new JButton("Exit");
		exittablebutton.setBounds(5, 5, 90, 23);
		
		/*exitmanagerbutton = new JButton("Exit");
		exitmanagerbutton.setBounds(5, 5, 90, 23);
		
		exitclubdetailsbutton = new JButton("Exit");
		exitclubdetailsbutton.setBounds(5, 5, 90, 23);*/
		
		okbutton = new JButton("Ok");
		okbutton.setBounds(5, 5, 90, 23);
		
		mainmenubutton =  new JButton("Back to Main Menu");
		mainmenubutton.setBounds(5, 5, 150, 23);
			
		newgamebutton.addActionListener(new newGameListener());
		loadbutton.addActionListener(new loadListener());
		quitbutton.addActionListener(new quitListener());
		backbutton.addActionListener(new backListener());
		nextbutton.addActionListener(new nextListener());
		//squadbutton.addActionListener(new squadListener());
		tacticsbutton.addActionListener(new tacticsListener());
		transfersbutton.addActionListener(new transfersListener());
		tablebutton.addActionListener(new tableListener());
		//managerbutton.addActionListener(new managerListener());
		//clubdetailsbutton.addActionListener(new clubdetailsListener());
		savebutton.addActionListener(new saveListener());
		playbutton.addActionListener(new playListener());
		//exitsquadbutton.addActionListener(new exitsquadListener());
		exittacticsbutton.addActionListener(new exittacticsListener());
		exittransfersbutton.addActionListener(new exittransfersListener());
		exittablebutton.addActionListener(new exittableListener());
		//exitmanagerbutton.addActionListener(new exitmanagerListener());
		//exitclubdetailsbutton.addActionListener(new exitclubdetailsListener());
		okbutton.addActionListener(new okListener());
		mainmenubutton.addActionListener(new mainmenuListener());

	}
	

	private class newGameListener implements ActionListener{
		public void actionPerformed(ActionEvent arg0){
			main.setVisible(false);
			managerprofile = new JFrame("Manager Profile");
			managerprofile.setSize(600, 500);
			managerprofile.setVisible(true);
			
			JLabel managerlabel = new JLabel("Manager Profile:");
			managerlabel.setFont(new Font("Arial", Font.PLAIN, 18));
			managerlabel.setForeground(Color.WHITE);
			managerlabel.setBounds(5, 60, 180, 23);
			
			JLabel managername = new JLabel("Manager Name: ");
			managername.setFont(new Font("Arial", Font.PLAIN, 14));
			managername.setForeground(Color.WHITE);
			managername.setBounds(5, 100, 200, 23);
			
			JTextField name = new JTextField();
			name.setBounds(115, 100, 200, 23);
			
			JLabel birthdate = new JLabel("Birth Date: ");
			birthdate.setFont(new Font("Arial", Font.PLAIN, 14));
			birthdate.setForeground(Color.WHITE);
			birthdate.setBounds(5, 150, 200, 23);
			
			JTextField birthdatefield = new JTextField();
			birthdatefield.setBounds(115, 150, 200, 23);
			
			JLabel countrylabel = new JLabel("Country: ");
			countrylabel.setFont(new Font("Arial", Font.PLAIN, 14));
			countrylabel.setForeground(Color.WHITE);
			countrylabel.setBounds(5, 200, 200, 23);
			
			JComboBox countrybox = new JComboBox(country);
			countrybox.setBounds(115, 200, 200, 23);
			
			JLabel clubselectionlabel = new JLabel("Club Selection: ");
			clubselectionlabel.setFont(new Font("Arial", Font.PLAIN, 14));
			clubselectionlabel.setForeground(Color.WHITE);
			clubselectionlabel.setBounds(5, 250, 200, 23);
			
			JComboBox clubbox = new JComboBox(club);
			clubbox.setBounds(115, 250, 200, 23);
			
			managerprofile.add(nextbutton);
			managerprofile.add(backbutton);
			managerprofile.add(managerlabel);
			managerprofile.add(managername);
			managerprofile.add(name);
			managerprofile.add(birthdate);
			managerprofile.add(birthdatefield);
			managerprofile.add(countrylabel);
			managerprofile.add(countrybox);
			managerprofile.add(clubselectionlabel);
			managerprofile.add(clubbox);
			
			JLabel background2 = new JLabel();
			background2.setIcon(new ImageIcon("goal.jpg"));
			background2.setBounds(0, 0, 1000, 462);
			managerprofile.getContentPane().add(background2);
			
				
		}
	}
	
	private class loadListener implements ActionListener{
		public void actionPerformed(ActionEvent arg0){
			JTextField filename = new JTextField(), dir = new JTextField();
			  JFileChooser c = new JFileChooser();
		      // Demonstrate "Open" dialog:
		      int rVal = c.showOpenDialog(ink.this);
		      if (rVal == JFileChooser.APPROVE_OPTION) {
		        filename.setText(c.getSelectedFile().getName());
		        dir.setText(c.getCurrentDirectory().toString());
		      }
		      if (rVal == JFileChooser.CANCEL_OPTION) {
		        filename.setText("You pressed cancel");
		        dir.setText("");
		}
		
	}
	}
	private class quitListener implements ActionListener{
		public void actionPerformed(ActionEvent arg0){
			System.exit(0);
		}
	}

	private class backListener implements ActionListener{
		public void actionPerformed(ActionEvent arg0){
			int option;
			option = JOptionPane.showOptionDialog(backbutton, "Are you sure?", "Back to main menu?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
			if(option == JOptionPane.YES_OPTION){
			managerprofile.setVisible(false);
			main.setVisible(true);
		}
			}
	}
	
	private class nextListener implements ActionListener{
		public void actionPerformed(ActionEvent arg0){
			int option;
			option = JOptionPane.showOptionDialog(nextbutton, "Are you sure?", "Club?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
			if(option == JOptionPane.YES_OPTION){
				managerprofile.setVisible(false);
				clubmenu = new JFrame("Club Menu: ");
				clubmenu.setSize(600,500);
				clubmenu.setVisible(true);
				
				JLabel clublabel = new JLabel("Club Menu");
				clublabel.setFont(new Font("Arial", Font.PLAIN, 20));
				clublabel.setForeground(Color.WHITE);
				clublabel.setBounds(240, 50, 200, 23);
				
				//clubmenu.add(squadbutton);
				clubmenu.add(tacticsbutton);
				clubmenu.add(transfersbutton);
				clubmenu.add(tablebutton);
				//clubmenu.add(managerbutton);
				//clubmenu.add(clubdetailsbutton);
				clubmenu.add(savebutton);
				clubmenu.add(playbutton);
				clubmenu.add(mainmenubutton);
				clubmenu.add(clublabel);
			
				JLabel background2 = new JLabel();
				background2.setIcon(new ImageIcon("pitch.jpg"));
				background2.setBounds(0, 0, 1000, 462);
				clubmenu.getContentPane().add(background2);
			}
		}
	}
	
	/*private class squadListener implements ActionListener{
		public void actionPerformed(ActionEvent arg0){
			clubmenu.setVisible(false);
			squad = new JFrame("Squad");
			squad.setSize(600,500);
			squad.setVisible(true);
			squad.add(exitsquadbutton);
			
			JLabel background2 = new JLabel();
			background2.setIcon(new ImageIcon("pitch.jpg"));
			background2.setBounds(0, 0, 1000, 462);
			squad.getContentPane().add(background2);
		}
	}*/
	
	private class tacticsListener implements ActionListener{
		public void actionPerformed(ActionEvent arg0){
			clubmenu.setVisible(false);
			tactics = new JFrame("Tactics");
			tactics.setSize(600,500);
			tactics.setVisible(true);
			tactics.add(exittacticsbutton);
			JPanel tacticspanel =  new JPanel();
			tacticspanel.setBounds(150, 80, 300, 300);
			tacticspanel.setLayout(null);
			tactics.add(tacticspanel);
			
			JButton grbutton = new JButton("GR");
			grbutton.setBounds(125, 280, 50, 15);
			tacticspanel.add(grbutton);
			Draggable d = new Draggable(grbutton);
			//EventQueue.invokeLater(runnable);
			
			JLabel background2 = new JLabel();
			background2.setIcon(new ImageIcon("pitch.jpg"));
			background2.setBounds(0, 0, 1000, 462);
			tactics.getContentPane().add(background2);
		}
		
		
		
		
		
	}
	
	private class transfersListener implements ActionListener{
		public void actionPerformed(ActionEvent arg0){
			clubmenu.setVisible(false);
			transfers = new JFrame("Transfers");
			transfers.setSize(600,500);
			transfers.setVisible(true);
			transfers.add(exittransfersbutton);
			
			JLabel background2 = new JLabel();
			background2.setIcon(new ImageIcon("pitch.jpg"));
			background2.setBounds(0, 0, 1000, 462);
			transfers.getContentPane().add(background2);
		}
	}
	
	private class tableListener implements ActionListener{
		public void actionPerformed(ActionEvent arg0){
			clubmenu.setVisible(false);
			table = new JFrame("Table");
			table.setSize(600,500);
			table.setVisible(true);
			table.add(exittablebutton);
			
			JLabel background2 = new JLabel();
			background2.setIcon(new ImageIcon("pitch.jpg"));
			background2.setBounds(0, 0, 1000, 462);
			table.getContentPane().add(background2);
		}
	}
	
	/*private class managerListener implements ActionListener{
		public void actionPerformed(ActionEvent arg0){
			clubmenu.setVisible(false);
			managerdetails = new JFrame("Manager");
			managerdetails.setSize(600,500);
			managerdetails.setVisible(true);
			managerdetails.add(exitmanagerbutton);
			
			JLabel background2 = new JLabel();
			background2.setIcon(new ImageIcon("pitch.jpg"));
			background2.setBounds(0, 0, 1000, 462);
			managerdetails.getContentPane().add(background2);
		}
	}
	
	private class clubdetailsListener implements ActionListener{
		public void actionPerformed(ActionEvent arg0){
			clubmenu.setVisible(false);
			clubdetails = new JFrame("Club Details");
			clubdetails.setSize(600,500);
			clubdetails.setVisible(true);
			clubdetails.add(exitclubdetailsbutton);
			
			JLabel background2 = new JLabel();
			background2.setIcon(new ImageIcon("pitch.jpg"));
			background2.setBounds(0, 0, 1000, 462);
			clubdetails.getContentPane().add(background2);
		}
	}*/
	
	private class saveListener implements ActionListener{
		public void actionPerformed(ActionEvent arg0){
			JTextField filename = new JTextField(), dir = new JTextField();
			JFileChooser c = new JFileChooser();
		      // Demonstrate "Save" dialog:
		      int rVal = c.showSaveDialog(ink.this);
		      if (rVal == JFileChooser.APPROVE_OPTION) {
		        filename.setText(c.getSelectedFile().getName());
		        dir.setText(c.getCurrentDirectory().toString());
		      }
		      if (rVal == JFileChooser.CANCEL_OPTION) {
		        filename.setText("You pressed cancel");
		        dir.setText("");
		      }
			/*Writer writer = null;

			try {
			    writer = new BufferedWriter(new OutputStreamWriter(
			          new FileOutputStream("save.txt"), "utf-8"));
			    writer.write("Something");
			} catch (IOException ex) {
			  // report
			} finally {
			   try {writer.close();} catch (Exception ex) {}
			}*/
		}
	}
	
	private class playListener implements ActionListener{
		public void actionPerformed(ActionEvent arg0){
			clubmenu.setVisible(false);
			play = new JFrame("Game");
			play.setSize(600,500);
			play.setVisible(true);
			play.add(okbutton);
			
			JLabel background2 = new JLabel();
			background2.setIcon(new ImageIcon("pitch.jpg"));
			background2.setBounds(0, 0, 1000, 462);
			play.getContentPane().add(background2);
		}
	}
	
	private class exitsquadListener implements ActionListener{
		public void actionPerformed(ActionEvent arg0){
			clubmenu.setVisible(true);
			squad.setVisible(false);}
	}
	
	private class exittacticsListener implements ActionListener{
		public void actionPerformed(ActionEvent arg0){
			clubmenu.setVisible(true);
			tactics.setVisible(false);}
	}
	
	private class exittransfersListener implements ActionListener{
		public void actionPerformed(ActionEvent arg0){
			clubmenu.setVisible(true);
			transfers.setVisible(false);}
	}
	
	private class exittableListener implements ActionListener{
		public void actionPerformed(ActionEvent arg0){
			clubmenu.setVisible(true);
			table.setVisible(false);}
	}
	
	private class exitmanagerListener implements ActionListener{
		public void actionPerformed(ActionEvent arg0){
			clubmenu.setVisible(true);
			managerdetails.setVisible(false);}
	}
	
	private class exitclubdetailsListener implements ActionListener{
		public void actionPerformed(ActionEvent arg0){
			clubmenu.setVisible(true);
			clubdetails.setVisible(false);}
	}
	
	private class okListener implements ActionListener{
		public void actionPerformed(ActionEvent arg0){
			clubmenu.setVisible(true);
			play.setVisible(false);}
	}
	
	private class mainmenuListener implements ActionListener{
		public void actionPerformed(ActionEvent arg0){
			int option;
			option = JOptionPane.showOptionDialog(mainmenubutton, "Are you sure?", "Main Menu?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
			if(option == JOptionPane.YES_OPTION){
			clubmenu.setVisible(false);
			main.setVisible(true);}
		}
	}

public static void main(String[] args) {
	try {
		ink window = new ink();
		window.open();
	} catch (Exception e) {
		e.printStackTrace();
	}
}

public void open() {
	
	startMenu();
}
}

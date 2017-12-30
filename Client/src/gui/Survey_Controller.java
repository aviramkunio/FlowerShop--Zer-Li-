package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import action.Msg;
import action.Payment_Account;
import action.Survey;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.Label;

public class Survey_Controller implements Initializable, ControllerI{

    public TextField q6_text;
    public Button addvertise_B;
    public TextField q1_text;
    public TextField q3_text;
    public TextField q4_text;
    public TextField q2_text;
    public TextField q5_text;
    public Label invalid_detailsL_q1;
    public Label invalid_detailsL_q2;
    public Label invalid_detailsL_q3;
    public Label invalid_detailsL_q4;
    public Label invalid_detailsL_q5;
    public Label invalid_detailsL_q6;
    public Button back_B;
	public static ActionEvent event_log;

    /**
	 * return to the previous window
	 * @param event
	 * @throws IOException
	 */
    public void back(ActionEvent event) throws IOException {

    	move(event,main.fxmlDir+ "Managment_F.fxml");
    }

    public void addvertise_Survey(ActionEvent event) 
    {
    	/*get the details from input fields*/
    	String q1 = q1_text.getText();
    	String q2 = q2_text.getText();
    	String q3 = q3_text.getText();
    	String q4 = q4_text.getText();
    	String q5 = q5_text.getText();
    	String q6 = q6_text.getText();
    	
    	/*initialize the error label to be no visible*/
    	invalid_detailsL_q1.setVisible(false);
    	invalid_detailsL_q2.setVisible(false);
    	invalid_detailsL_q3.setVisible(false);
    	invalid_detailsL_q4.setVisible(false);
    	invalid_detailsL_q5.setVisible(false);
    	invalid_detailsL_q6.setVisible(false);
    	
    	/*save the event*/
    	event_log =new ActionEvent();		 
		event_log=event.copyFor(event.getSource(), event.getTarget());
		 
    	/*check the inputs*/
		if(q1.isEmpty()) 
    	{
			invalid_detailsL_q1.setVisible(true);
    		return;
    	}
		if(q2.isEmpty()) 
    	{
			invalid_detailsL_q2.setVisible(true);
    		return;
    	}
		if(q3.isEmpty()) 
    	{
			invalid_detailsL_q3.setVisible(true);
    		return;
    	}
		if(q4.isEmpty()) 
    	{
			invalid_detailsL_q4.setVisible(true);
    		return;
    	}
		if(q5.isEmpty()) 
    	{
			invalid_detailsL_q5.setVisible(true);
    		return;
    	}
		if(q6.isEmpty()) 
    	{
			invalid_detailsL_q6.setVisible(true);
    		return;
    	}
		
		/*insert the input from user to instance of Survey"*/
    	Survey new_survey = new Survey(q1, q2, q3, q4, q5, q6);
		Msg check_user_details= new Msg();
		check_user_details.setInsert();
		check_user_details.setTableName("survey");
		check_user_details.oldO=new_survey;
		check_user_details.setRole("insert survey");
		check_user_details.event=event;
		Login_win.to_Client.accept((Object)check_user_details);
    }

    
    public void create_survey_success(Object message)
    {
    	/*the creating was successful -> run in new thread the new window*/
    	Platform.runLater(new Runnable() {
			
			@Override
			public void run() {
				 	try {
						move(event_log , main.fxmlDir+ "Managment_F.fxml");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}  
				
			}
		}); 
    }
    
    /**
     * General function for the movement between the different windows
     * @param event
     * @param next_fxml = string of the specific fxml
     * @throws IOException
     */
    public void move(ActionEvent event, String next_fxml)throws IOException 
	{
		 Parent menu;
	     menu = FXMLLoader.load(getClass().getResource(next_fxml));
		 Scene win1= new Scene(menu);
		 Stage win_1= (Stage) ((Node) (event.getSource())).getScene().getWindow();
		 win_1.setScene(win1);
		 win_1.show();
	}
    
    @Override
	public void initialize(URL location, ResourceBundle resources) {
	    
	    /*update the current controller to be this controller in general ClientConsole instance*/
    	Login_win.to_Client.setController(this);
    }
}

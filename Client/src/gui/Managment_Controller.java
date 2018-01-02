package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import action.Msg;
import action.Survey;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class Managment_Controller implements Initializable,ControllerI {

    public Button create_Survey_B;
    public Button update_Catalog_B;
    public Button answer_Complaint_B;
    public Button create_Sale_B;
    public Button back_B;
    public Button create_PaymentAccount_B;
    public Button compare_Reports_B;
    public Button conclusion_Survey_B;
    public Button edit_CustomersProfile_B;
    public Button display_Reports_B;
    public Button close_Survey_B;
    public Button add_Comments_B;
	public static ActionEvent event_log;
	public static Survey active_survey;
	
    public void update_Catalog(ActionEvent event) {

    }

    public void back(ActionEvent event) throws IOException {
    	move(event, main.fxmlDir+ "Main_menu_F.fxml");
    }

    public void create_Sale(ActionEvent event) {

    }

    /**
     * handle function for pressing 'create survey' -> first we check if already exist active survey
     * @param event
     * @throws IOException
     */
    public void create_Survey(ActionEvent event) throws IOException {
		
    	/*save the event*/
    	event_log =new ActionEvent();		 
		event_log=event.copyFor(event.getSource(), event.getTarget());
    	
    	/*check if already exist an active survey*/
    	Survey temp_survey = new Survey();
    	Msg check_survey_exist = new Msg();
    	check_survey_exist.setSelect();
    	check_survey_exist.oldO = temp_survey;
    	check_survey_exist.setTableName("survey");
    	check_survey_exist.setRole("check if there is active survey for insert");
    	check_survey_exist.event=event;
		Login_win.to_Client.accept((Object)check_survey_exist);
		
    }

    /**
     * handle function for pressing 'close survey' -> first we check if there is active survey
     * @param event
     */
    public void close_Survey(ActionEvent event) {

    	/*save the event*/
    	event_log =new ActionEvent();		 
		event_log=event.copyFor(event.getSource(), event.getTarget());
		
		/*check if already exist an active survey*/
    	Survey temp_survey = new Survey();
    	Msg check_survey_exist = new Msg();
    	check_survey_exist.setSelect();
    	check_survey_exist.oldO = temp_survey;
    	check_survey_exist.setTableName("survey");
    	check_survey_exist.setRole("check if there is active survey for close");
    	check_survey_exist.event=event;
		Login_win.to_Client.accept((Object)check_survey_exist);
		
    }
    
    public void add_Comments(ActionEvent event) {

    }

    public void answer_Complaint(ActionEvent event) {

    }

    public void conclusion_Survey(ActionEvent event) {
    	
    }

    public void create_PaymentAccount(ActionEvent event) throws IOException {
			move(event ,main.fxmlDir+ "Create_PaymentAccount_F.fxml");
		
    }

    public void edit_CustomersProfile(ActionEvent event) {

    }

    public void display_Reports(ActionEvent event) {

    }

    public void compare_Reports(ActionEvent event) {

    }
    /**
     * get message from server and act regarding to the answer
     * for "create survey" => if there is active survey we pop up a error message, else we continue to the next window
     * for "close survey" => if there is no active survey we pop up a error message, else we continue to the next window
     * @param message
     */
    public void check_if_survey_active(Object message)
    {
    	/*save the answer from server*/
    	Survey to_check = (Survey) (((Msg) message).newO);

    	if(((Msg) message).getRole().equals("check if there is active survey for close")) //for close
    	{
    		if(to_check == null)
        	{
    			JOptionPane.showMessageDialog(null, "There is no active survey");
        		return;
        	}
    		else
    		{
    			/*save the instance of survey in static var for future uses in other controller*/
    			active_survey = to_check;
    			
    			/*the creating was successful -> run in new thread the new window*/
            	Platform.runLater(new Runnable() {
        			
        			@Override
        			public void run() {
        				 	try {
        						move(event_log , main.fxmlDir+ "Close_Survey_F.fxml");
        					} catch (IOException e) {
        						// TODO Auto-generated catch block
        						e.printStackTrace();
        					}  
        			}
        		});
    		}
    	}
    	else if(((Msg) message).getRole().equals("check if there is active survey for insert")) //for create
    	{
    		if(to_check == null)
        	{
    			/*the creating was successful -> run in new thread the new window*/
            	Platform.runLater(new Runnable() {
        			
        			@Override
        			public void run() {
        				 	try {
        						move(event_log , main.fxmlDir+ "Create_Survey_F.fxml");
        					} catch (IOException e) {
        						// TODO Auto-generated catch block
        						e.printStackTrace();
        					}  
        				
        			}
        		}); 
        	}
    		else
    		{
    			JOptionPane.showMessageDialog(null, "There is already active survey, please close this survey before you try again...");
        		return;
    		}
    	}
    	
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
	public void initialize(URL location, ResourceBundle resources)
	{
    	/*set the buttons not visible*/
    	create_Survey_B.setVisible(false);
    	update_Catalog_B.setVisible(false);
    	answer_Complaint_B.setVisible(false);
    	create_Sale_B.setVisible(false);
    	create_PaymentAccount_B.setVisible(false);
    	compare_Reports_B.setVisible(false);
    	conclusion_Survey_B.setVisible(false);
    	edit_CustomersProfile_B.setVisible(false);
    	display_Reports_B.setVisible(false);
    	add_Comments_B.setVisible(false);
    	close_Survey_B.setVisible(false);
    	
    	/*update the current controller to be management controller in general ClientConsole instance*/
    	Login_win.to_Client.setController(this);
    	
    	/*check which privilege has the user*/
    	String privilege = Main_menu.current_user.getPrivilege();
    	
    	switch(privilege)
    	{
    		case "Chain Employee":
    			update_Catalog_B.setVisible(true);
    			break;
    		case "Customer Service Employee":
    			create_Survey_B.setVisible(true);
    			answer_Complaint_B.setVisible(true);
    			close_Survey_B.setVisible(true);
    			add_Comments_B.setVisible(true);
        		break;
    		case "Chain Manager":
    			display_Reports_B.setVisible(true);
    			compare_Reports_B.setVisible(true);
        		break;
    		case "Store Manager":
    			create_PaymentAccount_B.setVisible(true);
    			edit_CustomersProfile_B.setVisible(true);
    			display_Reports_B.setVisible(true);
        		break;
    		case "Service Expert":
    			conclusion_Survey_B.setVisible(true);
        		break;
    		case "Store Employee":
    			create_Sale_B.setVisible(true);
        		break;
    	}
	}
	

}

package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import javax.swing.Icon;

import action.Complain;
import action.Msg;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Post_Complain_Controller implements ControllerI,Initializable {
	public ComboBox<String> topic_names;
	public TextField user_id;
	public TextArea user_txt_complain;
	public Button back_to_main,sumbit_complain;
	public Label date,complain_topic,user_complain;
	public Complain cur_complain;
	 ObservableList<String> list;
	 public static ActionEvent event_g;

	public void back_to_main(ActionEvent event) throws IOException
	{
		Parent menu;
		  menu = FXMLLoader.load(getClass().getResource(main.fxmlDir+ "Main_menu_F.fxml"));
		 Scene win1= new Scene(menu);
		 Stage win_1= (Stage) ((Node) (event.getSource())).getScene().getWindow();
		 win_1.setScene(win1);
		 win_1.show();
	}
	
	
	public boolean check_form()
	{
		boolean ans=true;
		 
		//if((topic_names.getValue().equals(null)))
		//{ 
			// complain_topic.setTextFill(Color.web("#ed0b31"));
			//  ans=false;
		//}
		if(user_txt_complain.getText().equals(""))
		{
		 	user_complain.setTextFill(Color.web("#ed0b31"));
			  ans=false;
		}
		
		return ans;
		
	}
	public void submit_complain(ActionEvent event) throws IOException 
	{
		 event_g =new ActionEvent();		 
		 event_g=event.copyFor(event.getSource(), event.getTarget());
 		 
		if(check_form())
		{
			getUserComplainDetails();
			Msg msg= new Msg();
			Complain com_to_send=cur_complain;
			msg.setInsert();
			msg.setRole("insert a new complain");
			msg.setTableName("complaint");
			msg.oldO=(Complain)com_to_send;
			Login_win.to_Client.accept(msg);
			
			/*after the complain was inserted to DB*/
			Optional<ButtonType> result = Login_win.showPopUp("INFORMATION", "Complain was sent ", "Thanks for letting us know about the problem we will work to correct it quickly ", "Thank you!");
		      if (result.get() == ButtonType.OK)
		      {
		    	  Parent menu;
		    		 menu = FXMLLoader.load(getClass().getResource(main.fxmlDir+ "Main_menu_F.fxml"));
		    		// to_Client.setController(new Managment_Controller());
		    		 Scene win1= new Scene(menu);
		    		 Stage win_1= (Stage) ((Node) (event_g.getSource())).getScene().getWindow();
		    		 win_1.setScene(win1);
		    		 win_1.show(); 
		      }
		}
		
		else {
			/*show pop up if one or more of the fields in invalid*/
			gui.Login_win.showPopUp("ERROR", " Uncomplete- complain", "You have to fill all the required fields", "");
		}
	}
	
/*
 * get the user details (that he insert)
 */
	private void getUserComplainDetails() 
	{
		cur_complain.setCustomer_ID(user_id.getText());
		cur_complain.setUser_txt(user_txt_complain.getText());
		cur_complain.setChosen_topic(topic_names.getValue());
		
	}

/**
 * to override
 * @param o
 */
	public void get_submit_approved(Object o) {
		/*show pop up -> and then return to main*/
		

	}
	/**
	 * set the current user details in the window
	 */
	public void setInitialDetails()
	{	cur_complain=new Complain();
		/*set the topics in  the comboBox*/
    	list = FXCollections.observableArrayList(cur_complain.getComplain_topic()); 
    	topic_names.setItems(list);
    	user_id.setText(gui.Main_menu.current_user.getUser_ID());
    	user_id.setEditable(false);
    	user_txt_complain.setPromptText("Enter text unit 200 chars");
	}
	 
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		 Login_win.to_Client.setController(this);
		 setInitialDetails();
		
	}

}

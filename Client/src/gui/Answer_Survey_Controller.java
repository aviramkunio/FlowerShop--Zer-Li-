package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;

import action.Msg;
import action.Survey;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Answer_Survey_Controller implements ControllerI,Initializable {
public RadioButton R1_1,R1_2 ,R1_3 ,R1_4 ,R1_5 ,R1_6 ;
public RadioButton R2_1,R2_2 ,R2_3 ,R2_4 ,R2_5 ,R2_6 ;
public RadioButton R3_1,R3_2 ,R3_3 ,R3_4 ,R3_5 ,R3_6 ;
public RadioButton R4_1,R4_2 ,R4_3 ,R4_4 ,R4_5 ,R4_6 ;
public RadioButton R5_1,R5_2 ,R5_3 ,R5_4 ,R5_5 ,R5_6 ;
public RadioButton R6_1,R6_2 ,R6_3 ,R6_4 ,R6_5 ,R6_6 ;
public Button Back_to_main,submit_survey;
public ToggleGroup a1,a2,a3,a4,a5,a6 ;
public Label q1,q2,q3,q4,q5,q6;
public Label survey_id;
public static Survey current_survey;
 /**
  * need to be 10 radio button 
  * 
  * 
  */

public void back_to_main(ActionEvent event) throws IOException {


	 Parent menu;
	 menu = FXMLLoader.load(getClass().getResource(main.fxmlDir+ "Main_menu_F.fxml"));
	// to_Client.setController(new Managment_Controller());
	 Scene win1= new Scene(menu);
	 Stage win_1= (Stage) ((Node) (event.getSource())).getScene().getWindow();
	 win_1.setScene(win1);
	 win_1.show();
}

public void form_submit(ActionEvent event) throws IOException {
	int validity_flag = 0;
	 
	
	  /*send answers of the client to DB*/
		if (check_user_form())
		{
			validity_flag=1;
			 get_customer_answers();
			 update_survey_answers_inDB();
		}
	
	if(validity_flag==1) {
		Alert alert = new Alert(AlertType.INFORMATION);
      alert.setTitle("Message");
      alert.setContentText("Your answers were submitted - have a GOOD day!");
      alert.setHeaderText("Thank you!");

      Optional<ButtonType> result = alert.showAndWait();
      if (result.get() == ButtonType.OK)
      {
      	System.out.println("after thr confirmation");
      	
      	
      	/*sends the answers*/
      }
	}
	else {
		Alert alert = new Alert(AlertType.INFORMATION);
	      alert.setTitle("Message");
	      alert.setContentText("In order to complete the survey to will have to answers all 6 Q");
	      alert.setHeaderText("Invalid fields!");


	      Optional<ButtonType> result = alert.showAndWait();
	      if (result.get() == ButtonType.OK)
	      {
	      	System.out.println("");
	      }
		
		
	}
      
    
      
      
        
}
/**
 * check if the user did answer all the six Q
 * if not , show Invalid details msg AND mark the Invalid fields in red
 * @return
 */
private boolean check_user_form() {
	
	 boolean answer=true;
	/*check q1*/
	 
	
	if(a1.getSelectedToggle() == null) {
		System.out.println("nothing is selected in q1  ");
		q1.setTextFill(Color.web("#ed0b31"));
		 answer= false;}
	if(a2.getSelectedToggle() == null) {
		System.out.println("nothing is selected in q2  ");
		q2.setTextFill(Color.web("#ed0b31"));
		 answer= false;}
	
	if(a3.getSelectedToggle() == null) {
		System.out.println("nothing is selected in q3 ");
		q3.setTextFill(Color.web("#ed0b31"));
		 answer=false;}
	if(a4.getSelectedToggle() == null) {
		System.out.println("nothing is selected in q4 ");
		q4.setTextFill(Color.web("#ed0b31"));
		 answer= false;}
	if(a5.getSelectedToggle() == null) {
		System.out.println("nothing is selected in q5  ");
		q5.setTextFill(Color.web("#ed0b31"));
		 answer= false;}
	
	if(a6.getSelectedToggle() == null) {
		System.out.println("nothing is selected in q6  ");
		q6.setTextFill(Color.web("#ed0b31"));
		 answer=false;}
	
	return answer;
}

public void get_customer_answers()
{
	 
	
	if(a1.getSelectedToggle() ==R1_1)
	{
		current_survey.setA1(1);
	}
	if(a1.getSelectedToggle() ==R1_2)
	{
		current_survey.setA1(2);
	}
	if(a1.getSelectedToggle() ==R1_3)
	{
		current_survey.setA1(3);
	}
	if(a1.getSelectedToggle() ==R1_4)
	{
		current_survey.setA1(4);
	}
	if(a1.getSelectedToggle() ==R1_5)
	{
		current_survey.setA1(5);
	}
	if(a1.getSelectedToggle() ==R1_6)
	{
		current_survey.setA1(6);
	}
	
	System.out.println(current_survey.getA1());
		
		 
	
	
	
	
	
	 
}


public void update_survey_answers_inDB() 
{
	Msg msg= new Msg();
	Survey update_survey=current_survey;
	msg.setUpdate();
	msg.setRole("update survey answers");
	msg.setTableName("survey");
	msg.oldO=update_survey;
	  Login_win.to_Client.accept(msg);

	
	
	
}
public void get_survey_qustion()
{
	Msg get_survey_q= new Msg();
	get_survey_q.setSelect();
	get_survey_q.setRole("get survey qustion");
	get_survey_q.setTableName("survey");
	Survey survey=new Survey();
	get_survey_q.oldO=survey;
  Login_win.to_Client.accept(get_survey_q);
	
}

public void set_survey_question(Object msg) 
	{
	
	/*set the question from the Db to the user screen s*/
	 Platform.runLater(new Runnable() {
		
		@Override
		public void run() {
			Msg msg1=(Msg) msg;
	Survey survey= new Survey();
	current_survey=(Survey) msg1.newO;
	survey=(Survey) msg1.newO;
	survey_id.setText(survey.getDate());
	q1.setText(survey.getQ1());
	q2.setText(survey.getQ2());
	q3.setText(survey.getQ3());
	q4.setText(survey.getQ4());
	q5.setText(survey.getQ5());
	q6.setText(survey.getQ6()); 
	current_survey=survey;
			
		}
	});
	
	
	/*set the radio buttons by groups*/
		 setRadioB();
	}
 
public void setRadioB()
{ 
	  	a1 = new ToggleGroup();
	    RadioButton button1 = R1_1;
	    button1.setToggleGroup(a1);
 	    RadioButton button2 =R1_2;
	    button2.setToggleGroup(a1);
	    RadioButton button3 =R1_3;
	    button3.setToggleGroup(a1);
	    RadioButton button4 =R1_4;
	    button4.setToggleGroup(a1);
	    RadioButton button5 =R1_5;
	    button5.setToggleGroup(a1);
	    RadioButton button6 =R1_6;
	    button6.setToggleGroup(a1);
	    
	    
	    a2 = new ToggleGroup();
	    RadioButton button2_1 = R2_1;
	    button2_1.setToggleGroup(a2);
 	    RadioButton button2_2 =R2_2;
	    button2_2.setToggleGroup(a2);
	    RadioButton button2_3 =R2_3;
	    button2_3.setToggleGroup(a2);
	    RadioButton button2_4 =R2_4;
	    button2_4.setToggleGroup(a2);
	    RadioButton button2_5 =R2_5;
	    button2_5.setToggleGroup(a2);
	    RadioButton button2_6 =R2_6;
	    button2_6.setToggleGroup(a2);
	    
	    a3 = new ToggleGroup();
	    RadioButton button3_1 = R3_1;
	    button3_1.setToggleGroup(a3);
 	    RadioButton button3_2 =R3_2;
	    button3_2.setToggleGroup(a3);
	    RadioButton button3_3 =R3_3;
	    button3_3.setToggleGroup(a3);
	    RadioButton button3_4 =R3_4;
	    button3_4.setToggleGroup(a3);
	    RadioButton button3_5 =R3_5;
	    button3_5.setToggleGroup(a3);
	    RadioButton button3_6 =R3_6;
	    button3_6.setToggleGroup(a3);
	    
	    
	    a4 = new ToggleGroup();
	    RadioButton button4_1 = R4_1;
	    button4_1.setToggleGroup(a4);
 	    RadioButton button4_2 =R4_2;
	    button4_2.setToggleGroup(a4);
	    RadioButton button4_3 =R4_3;
	    button4_3.setToggleGroup(a4);
	    RadioButton button4_4 =R4_4;
	    button4_4.setToggleGroup(a4);
	    RadioButton button4_5 =R4_5;
	    button4_5.setToggleGroup(a4);
	    RadioButton button4_6 =R4_6;
	    button4_6.setToggleGroup(a4);
	    
	    
	    
	    a5 = new ToggleGroup();
	    RadioButton button5_1 = R5_1;
	    button5_1.setToggleGroup(a5);
 	    RadioButton button5_2 =R5_2;
	    button5_2.setToggleGroup(a5);
	    RadioButton button5_3 =R5_3;
	    button5_3.setToggleGroup(a5);
	    RadioButton button5_4 =R5_4;
	    button5_4.setToggleGroup(a5);
	    RadioButton button5_5 =R5_5;
	    button5_5.setToggleGroup(a5);
	    RadioButton button5_6 =R5_6;
	    button5_6.setToggleGroup(a5);
	    
	    
	    a6 = new ToggleGroup();
	    RadioButton button6_1 = R6_1;
	    button6_1.setToggleGroup(a6);
 	    RadioButton button6_2 =R6_2;
	    button6_2.setToggleGroup(a6);
	    RadioButton button6_3 =R6_3;
	    button6_3.setToggleGroup(a6);
	    RadioButton button6_4 =R6_4;
	    button6_4.setToggleGroup(a6);
	    RadioButton button6_5 =R6_5;
	    button6_5.setToggleGroup(a6);
	    RadioButton button6_6 =R6_6;
	    button6_6.setToggleGroup(a6);
	    
}



@Override
public void initialize(URL location, ResourceBundle resources) 
{
	 Login_win.to_Client.setController(this);
	 get_survey_qustion();
	//check_if_user already user didt this survey
	
 
}

}






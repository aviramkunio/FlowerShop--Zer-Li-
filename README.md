# :squirrel:ProjectX:squirrel:
## Collage project - Semester 5 - "Zer-Li" application
### how to style .md file: https://help.github.com/articles/basic-writing-and-formatting-syntax/
### what is a .md file: https://fileinfo.com/extension/md

<br /><br /><br />
**Design Conventions:**

## GUI Design Conventions

**1. Font:**
   - Default: System 12px
   - Special font: Arial Bold Italic
   - ~~Green [#29940c]~~ (need new color)
   - Red [#eb0a0a]
		
**2. Titles:**
   - Font: Arial Black
   - Size: fit by eye (big)
   - Color: #0d0d0c (**_Notice_**)
   
   

**3. Window Size:**
   - Default:
     - 500x600 (width x height) [pixels].


**4. Common Button Allocations:**
- **Acceptance Buttons** (√ ,yes, ok, order, add, etc..) will be on the RIGHT.
- **Denial Buttons** (Ꭓ, not, cancel, back, etc..) on LEFT.
- Back Button Position(pixels): Not Decided.
- OK Button Position(pixels): Not Decided.



## **Common Methods:**

1. ### Move to other window (implementation):

```
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
		 
		  //close window by X button
		 win_1.setOnCloseRequest(new EventHandler<WindowEvent>() {
	          public void handle(WindowEvent we) {
	        	  Msg  msg=new Msg();
	      		Person user_logout=current_user;
	      		msg.setRole("user logout");
	      		msg.setTableName("person");
	      		msg.setUpdate();
	      		msg.oldO=user_logout;
	      		Login_win.to_Client.accept(msg);
	          }
	      });        
	} 
```
  
- Example of calling:
```
  move(event,main.fxmlDir+ "Managment_F.fxml");
```

2. ### showPopUpMessage:

```
Optional<ButtonType> option = Login_win.showPopUp(type="CONFIRMATION", title="LogOut", header="Are you sure...", content="blabla");
```

- if you don't need one of the field => insert empty string ""

3. ### Main user cart is:
```
 Main_menu.userCart
```
<br /><br /><br />
 :pencil: :pencil::pencil: :pencil: :pencil:
 ## :pencil:  toAdd:   :pencil:
 :pencil: :pencil::pencil: :pencil: :pencil: 

- [x] Put 'x' inside the file to fill.

**toImplement:** <br/>
- [ ] Card
- [ ] Cancel Order
- [ ] Reports


<br/>

**Hagasha** <br/>
- [ ] prepare a database to present the functionality (לפי מלכי)
- [ ] JavaDOC -> /** details of function */

**General** <br/>
- [ ] a log file ,that record the server action, the system manager can see this file
- [ ] to view profile -> purcahse history
- [ ] update TDL (needed?)
- [ ] Sharon Assignment 2 (until 18.1)
- [ ] Sharon Meeting (16.1 at 15:50 M316)
- [ ] Add to ID creators (maxID +1) 'if no ID exists' -> create id=1.

**Login Window**<br/>
- [ ] login window gui (bg)
- [ ] No labels -> fix
- [ ] remove 3 tries

**Gui** <br/>
- [ ] complaint window gui
- [ ] main gui (bg coloration)


**Edit Profile Info**<br/>
- [ ] After pressing update -> do pop-up || label.

**Order** <br/>
- [ ] cart order button error when cart empty
- [ ] user's order history
- [ ] repair tabbing in order
- [ ] show final price for order in the order pane.
- [ ] order.self collect -> auto storeid/present options
- [ ] order date -> use muhi's method from survey

**Cancel Order** <br/>
- [ ] verify times regarding money compensation 

**Catalog** <br/>
- [ ] Implement adding Item From Catalog to Cart->Order
 
**Self Item** <br/>
- [ ] fit Self Item creation for user story (no description -> selection between options)
- [ ] user input amount of item->check correct input
- [ ] after adding to cart, message should be 'you can create another item.'

**Delivery**<br/>
- [ ] Verify 3 hours time request (curr hour - requested hour >= 3)
- [ ] Add Delivery price for every order with delivery

**Cart**<br/>
- [ ] View item details option
- [ ] Move card functionality from Cart_Controller to Cart entity

**Card** <br/>
- [ ] adding Card to order
 
**Reports** <br/>
- [ ] Implement Reports



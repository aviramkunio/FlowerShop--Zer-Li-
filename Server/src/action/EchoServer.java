package action;

// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import java.sql.PreparedStatement;
import ocsf.server.*;

/**
 * This class overrides some of the methods in the abstract superclass in order
 * to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 * @version July 2000
 */
public class EchoServer extends AbstractServer {
	// Class variables *************************************************

	/**
	 * The default port to listen on.
	 */
	final public static int DEFAULT_PORT = 5555;
	public static String user_pass;
	public static String table_name;
	public static String schema_name;
	public static String user_name;
	// Constructors ****************************************************

	/**
	 * Constructs an instance of the echo server.
	 *
	 * @param port
	 *            The port number to connect on.
	 */
	public EchoServer(int port) {
		super(port);
		user_name = JOptionPane.showInputDialog("Enter User name  ");
		if (user_name.equals("")) {
			JOptionPane.showMessageDialog(null, "Invalid name");
			System.exit(0);
		}
		user_pass = JOptionPane.showInputDialog("Enter Password  ");
		schema_name = JOptionPane.showInputDialog("Enter Schema Name ");

	}

	// Instance methods ************************************************

	/**
	 * This method handles any messages received from the client. This method check
	 * which kind of query arrived from client
	 * 
	 * @param msg
	 *            The message received from the client.
	 * @param client
	 *            The connection from which the message originated.
	 */
	public void handleMessageFromClient(Object msg, ConnectionToClient client) {
		Msg msg1 = (Msg) msg;
		String query_type = msg1.getType();

		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/" + schema_name, user_name,
					user_pass);
			System.out.println("SQL connection succeed");
			/* Define which kind the message the server got */
			/**
			 * first find the kind of the query then, check the role of the msg (the role is
			 * a simple short string)
			 */
			switch (query_type) {
			case "SELECT": {
				if (msg1.getRole().equals("verify user details"))
					check_user_details(msg1, conn, client);
				else if (msg1.getRole().equals("check if ID exist and add payment account"))
					check_id_exist(msg1, conn, client);
				else if (msg1.getRole().equals("check if there is active survey"))
					check_survey_exist(msg1, conn, client);
				else if (msg1.getRole().equals("find items color-type-price"))
					SelectItemsCTP(msg1, conn, client);

			}
			case "UPDATE": {
				if (msg1.getRole().equals("user logout"))
					change_online_status(msg1, conn, "0");
				if (msg1.getRole().equals("update user details"))
					Update_user_details(msg1, conn, client);
				// else getProdectdetails(msg1,conn,client);
				// else UpdateItem(conn,msg,client);
			}
			case "SELECTALL": {
				ViewItems(conn, client);
			}
			case "INSERT": {
				if (msg1.getRole().equals("insert survey"))
					insert_survey(msg1, conn, client);
			}
			}// end switch
		} // end try
		/*
		 * }
		 * 
		 * else { getProdectdetails(msg1,conn,client); } }
		 * 
		 * 
		 * 
		 * else if (msg1.getType().equals("UPDATE")) UpdateItem(conn,msg,client); }
		 */
		catch (SQLException ex) {
			/* handle any errors */
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		} catch (Exception ex) {
			/* handle the error */
		}

	}

	public static void decoy(Msg msg, Connection con, ConnectionToClient client) throws IOException {

		client.sendToClient(msg);
	}

	/**
	 * Query for selecting items by Color-Type-Price combination
	 * 
	 * @param msg
	 *            The message received from the client. msg.newO=max price.
	 *            msg.oldO=min price.
	 * @param client
	 *            The connection from which the message originated.
	 */
	public static void SelectItemsCTP(Msg msg, Connection con, ConnectionToClient client) {

		Msg msg1 = (Msg) msg;
		Product p = (Product) msg1.oldO;

		String type = p.GetType();
		String color = p.GetColor();
		String minprice = Float.toString(msg1.num1);
		String maxprice = Float.toString(msg1.num2);

		Object products = new ArrayList<Object>(); // arraylist of products
													// back from query

		try {
			/** Building the query */
			PreparedStatement ps = con.prepareStatement(
					" SELECT * FROM `zerli`.`item` WHERE Color=? AND Type=? " + "AND Price BETWEEN ? AND ?");
			ps.setString(1, color);
			ps.setString(2, type);
			ps.setString(3, minprice);
			ps.setString(4, maxprice);

			/** Results */
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {

				Product returnproduct = new Product(); // create new product
				returnproduct.SetID(rs.getString(1)); // set details as needed
				returnproduct.SetName(rs.getString(2));
				returnproduct.SetPrice(Float.parseFloat(rs.getString(4)));
				// insert to array (cast from Object)
				((ArrayList<Product>) products).add(returnproduct);
			}

			/** back to client */
			msg1.newO = products;

			client.sendToClient(msg);
			rs.close();

		} catch (IOException x) {
			System.err.println("unable to send msg to client");
		} catch (SQLException ex)

		{/** handle any errors */
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
		return;
	}

	public static void check_user_details(Msg msg1, Connection conn, ConnectionToClient client) {
		Person user = (Person) msg1.oldO;
		String a;
		try {
			PreparedStatement ps = conn.prepareStatement(
					" SELECT * FROM " + msg1.getTableName() + " " + "WHERE ID=? and Password=?;");

			/* insert the names to the query */
			ps.setString(1, user.getUser_ID());
			ps.setString(2, user.getUser_password());
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				a = rs.getString(1);

				// if the user exist
				if (!(a.equals("0")))
				{
					user.setIsExist("1");
					user.setUser_name(rs.getString(2));
					user.setPrivilege(rs.getString(5));
					user.setUser_last_name(rs.getString(3));
					user.setWWID(rs.getString(7));
					user.setIsOnline("1");
					msg1.oldO = user;

					/* check if it is possible to change the status of the user */
					if (change_online_status(msg1, conn, "1") == true) {
						msg1.newO = (Person) user;
						client.sendToClient(msg1);
						return;
					} else {// if the user is already connected

						msg1.newO = (Person) user;
						user.setAlreadyConnected(true);
						client.sendToClient(msg1);
						return;
					}
				} // end if

			} // while rs

			if (rs.next() == false) /* if the user dosent exist in the system */
			{
				Person user_not_exist = new Person( null, null);
				user_not_exist.setIsExist("0");
				msg1.newO = user_not_exist;
				client.sendToClient(msg1);
				return;

			}
		} // try

		catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException x) {
			System.err.println("unable to send msg to client");
		}
	}

	public static boolean change_online_status(Msg msg1, Connection conn, String new_status)
	{

		Person user = (Person) msg1.oldO;
		boolean answer;
		answer = isConnected(msg1, conn);
		if (answer == true && new_status.equals("1"))
			return false;

		try {
			PreparedStatement ps = conn.prepareStatement(
					"UPDATE " + msg1.getTableName() + " " + "SET Online=? WHERE ID=" + user.getUser_ID());
			ps.setString(1, new_status);
			ps.executeUpdate();

			// System.out.println("the online status was changed ");
		}

		catch (SQLException e) {
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * run tests to check if all details are correct and then create the new Payment
	 * Account in DB
	 * 
	 * @param msg1
	 * @param conn
	 * @param client
	 */
	public static void check_id_exist(Msg msg1, Connection conn, ConnectionToClient client) {

		Payment_Account user = (Payment_Account) msg1.oldO;
		PreparedStatement ps;
		try {
			/* check if the id exist in person table */
			ps = conn.prepareStatement(" SELECT * FROM " + msg1.getTableName() + " " + "WHERE ID=?;");
			ps.setString(1, user.getID());
			ResultSet rs;
			rs = ps.executeQuery();
			if (!rs.next()) {
				msg1.newO = null; // this is mean that the id was not found in DB
				client.sendToClient(msg1);
				return;
			} else // the user is exist in person table --> create the payment account
			{
				/* check if the user already exist in payment_account table */
				ps = conn.prepareStatement(" SELECT * FROM payment_account WHERE ID=?;");
				ps.setString(1, user.getID());
				rs = ps.executeQuery();
				if (rs.next()) {
					msg1.newO = null; // this is mean that the id already in DB
					client.sendToClient(msg1);
					return;
				}

				/*
				 * if we reach here --> all the test are fine and we insert the new payment
				 * account
				 */
				ps = conn.prepareStatement(
						"INSERT INTO payment_account (ID, CreditCard, Status, Subscription) VALUES (?, ?, ?, ?);");
				ps.setString(1, user.getID());
				ps.setString(2, user.getCreditCard());
				ps.setString(3, user.getStatus());
				ps.setString(4, user.getSubscription());
				ps.executeUpdate();
				msg1.newO = user;
				client.sendToClient(msg1);
				return;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void check_survey_exist(Msg msg1, Connection conn, ConnectionToClient client) {
		PreparedStatement ps;
		ResultSet rs;
		try {
			ps = conn.prepareStatement(" SELECT * FROM survey WHERE Status = 'Active';");
			rs = ps.executeQuery();
			if (!rs.next()) {
				msg1.newO = msg1.oldO; // this is mean that there is no active server
				client.sendToClient(msg1);
				return;
			}
			msg1.newO = null; // this is mean that there is active server
			client.sendToClient(msg1);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * insert the survey to survey table with new id
	 * 
	 * @param msg1
	 * @param conn
	 * @param client
	 */
	public static void insert_survey(Msg msg1, Connection conn, ConnectionToClient client) {
		Survey user = (Survey) msg1.oldO;
		PreparedStatement ps;
		ResultSet rs;
		int new_id;
		try {
			/* get the last ID */
			ps = conn.prepareStatement("SELECT max(ID) FROM survey;");
			rs = ps.executeQuery();
			rs.next();
			/* execute the insert query */
			ps = conn.prepareStatement(
					"INSERT INTO survey (ID, Date, Q1, Q2, Q3, Q4, Q5, Q6, Status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);");
			new_id = Integer.parseInt(rs.getString(1)) + 1;
			ps.setString(1, "" + new_id); // insert the last id + 1
			ps.setString(2, user.getDate());
			ps.setString(3, user.getQ1());
			ps.setString(4, user.getQ2());
			ps.setString(5, user.getQ3());
			ps.setString(6, user.getQ4());
			ps.setString(7, user.getQ5());
			ps.setString(8, user.getQ6());
			ps.setString(9, "Active");
			ps.executeUpdate();

			msg1.newO = user;
			client.sendToClient(msg1);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static boolean isConnected(Msg msg1, Connection conn) {
		boolean isAlreadyCon = false;
		Person user = (Person) msg1.oldO;
		String current_status;

		try {
			PreparedStatement ps = conn.prepareStatement("SELECT Online FROM " + schema_name + "." + msg1.getTableName()
					+ " " + " WHERE ID=" + user.getUser_ID());
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				current_status = rs.getString(1);
				if (current_status.equals("1")) {
					isAlreadyCon = true;
					// System.out.println("cannot change the user status- user is already online");
					return isAlreadyCon;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return isAlreadyCon;

	}

	public static void Update_user_details(Object msg, Connection con, ConnectionToClient client) {
		String ans = "Update done";
		Msg msg1 = (Msg) msg;
		Person old_d = (Person) msg1.oldO;
		Person new_d = (Person) msg1.newO;
		try {
			PreparedStatement ps = con.prepareStatement(
					"UPDATE " + msg1.getTableName() + " " + "SET FirstName=? , LastName=? , Password=? WHERE ID=?");

			/* insert the names to the query */
			ps.setString(1, new_d.getUser_name());
			ps.setString(2, new_d.getUser_last_name());
			ps.setString(3, new_d.getUser_password());
			ps.setString(4, old_d.getUser_ID());

			ps.executeUpdate();

			client.sendToClient(msg);

		} catch (SQLException e) {
			e.printStackTrace();
			
			}
		  catch (IOException x) {
			System.err.println("unable to send msg to client");
		}

	}

	/**
	 * send a query to DB ->Select all returns the names of the products as an
	 * Object(ArrayList<String>)
	 * 
	 * @param con
	 * @param client
	 */
	public static void ViewItems(Connection con, ConnectionToClient client) {
		Statement stmt;
		Object temp = new ArrayList<String>();
		try {

			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT Name FROM zerli.item");
			while (rs.next()) {
				((ArrayList<String>) temp).add(rs.getString(1));
			}
			client.sendToClient(temp);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException x) {
			System.err.println("unable to send msg to client");
		}
	}

	/**
	 * this method gets the product name and change it. gets two Objects(product)
	 * 
	 * @param con
	 * @param msg
	 * @param client
	 */
	public static void UpdateItem(Connection con, Object msg, ConnectionToClient client) {
		String ans = "Update done";
		Msg msg1 = (Msg) msg;
		Product p_old = (Product) msg1.oldO;
		Product p_new = (Product) msg1.newO;
		try {
			PreparedStatement ps = con.prepareStatement(
					"UPDATE hw2.product SET ProductId=?,ProductName=?,ProductType=? WHERE ProductName=?");

			/* insert the names to the query */
			ps.setString(1, p_new.GetID());
			ps.setString(2, p_new.GetName());
			ps.setString(3, p_new.GetType());
			ps.setString(4, p_old.GetName());

			ps.executeUpdate();

			client.sendToClient(ans);

		} catch (SQLException e) {
			e.printStackTrace();
			try {
				client.sendToClient("UPDATE faild");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} catch (IOException x) {
			System.err.println("unable to send msg to client");
		}

	}

	/**
	 * this method gets the name of a product sends back the products details
	 * 
	 * @param msg
	 * @param con
	 * @param client
	 */
	public static void getProdectdetails(Object msg, Connection con, ConnectionToClient client) {
		Msg msg1 = (Msg) msg;
		Product p = (Product) msg1.oldO;
		try {

			/* send a query with the product name as a parameter */
			PreparedStatement ps = con.prepareStatement("SELECT * FROM hw2.product where ProductName=?");
			ps.setString(1, p.GetName());
			ResultSet r = ps.executeQuery();
			while (r.next()) {
				p.SetID(r.getString(1));
				p.SetName(r.getString(2));
				p.SetType(r.getString(3));
				msg1.oldO = p;
			}
			/* back to client */
			client.sendToClient(msg1);
			r.close();
		} catch (IOException x) {
			System.err.println("unable to send msg to client");
		} catch (SQLException ex)

		{/* handle any errors */
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
	}

	/**
	 * This method overrides the one in the superclass. Called when the server
	 * starts listening for connections.
	 */
	protected void serverStarted() {
		System.out.println("Server listening for connections on port " + getPort());
	}

	/**
	 * This method overrides the one in the superclass. Called when the server stops
	 * listening for connections.
	 */
	protected void serverStopped() {
		System.out.println("Server has stopped listening for connections.");
	}

	// Class methods ***************************************************

	/**
	 * This method is responsible for the creation of the server instance (there is
	 * no UI in this phase).
	 *
	 * @param args[0]
	 *            The port number to listen on. Defaults to 5555 if no argument is
	 *            entered.
	 */
	public static void main(String[] args) {
		int port = 0; // Port to listen on
		try {
			port = Integer.parseInt(args[0]); // Get port from command line
		} catch (Throwable t) {
			port = DEFAULT_PORT; // Set port to 5555
		}

		EchoServer sv = new EchoServer(port);

		try {
			sv.listen(); // Start listening for connections
		} catch (Exception ex) {
			System.out.println("ERROR - Could not listen for clients!");
		}
	}

}
// End of EchoServer class

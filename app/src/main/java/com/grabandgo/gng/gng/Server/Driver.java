
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;


public class Driver {

	private static Connection con;
	private static Statement stat;
	private static ResultSet resSet;

	/*
	 * Skapar en kontakt till database så att andra metoder kan skicka och hämta ur databasen.
	 */
	
	public static void connectionToMysql() {
		String userName = "Bob";
		String passWord = "bob";
		String host = "jdbc:mysql://94.254.94.236:51515";
//		String host = "jdbc:mysql://192.168.1.22:51515";
//		String host = "jdbc:mysql://81.170.228.242:41414";
		try {
			con = DriverManager.getConnection(host, userName, passWord);
			System.out.println("Connected to MySQL server...");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	 /*
	  * Stänger ner kontakten till databasen
	  */
	public static void closeMysql(){
		try {
			con.close();
			System.out.println("Closing connection...");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * Metod som tillåter fejkbanken att stoppa in köp i databasen.
	 * @param konto 	String med kontonummer som stoppas in i databasen.
	 * @param tag		String med försäljarnamn som skall stoppas in i databasen.
	 * @param price		int som representerar pris för den transaktionen som stoppas in i databasen.
	 * @param date  	String som inehåller datum för transaktionen som stoppas in i databasen.
	 * @param timeStamp String med tidpunkt för transaktionen som stoppas in i databasen.
	 */
	public static void newPurchase(String konto, String tag, int price, String date, String timeStamp) {
		connectionToMysql();
		System.out.println("New purchase...");
		try {
			stat = con.createStatement();
			String sql = "INSERT INTO spargrisen.purchase VALUES( NULL,'"
						+ konto + "','" + tag + "'," + price
						+ ",'" + date + "','" + timeStamp + "')";
			stat.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("Purchase added...");
		closeMysql();
	}
	/*
	 * Metod som kollar om mailadress är ledig.
	 * @param email   String som inehåller mailadressen.
	 * @return isTrue Boolean som är true ifall email inte hittas i databasen.
	 */
	public static boolean isUserFree(String email) {
		connectionToMysql();
		System.out.println("Cheking if Username exist in system");
		boolean isTrue = true;
		try {
			stat = con.createStatement();
			String sql = "SELECT * FROM spargrisen.user";
			resSet = stat.executeQuery(sql);
			while (resSet.next()) {
				if(email.equals(resSet.getString("Email"))){
					isTrue=false;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		closeMysql();
		return isTrue;
	}
	/*
	 * Metod som går igenom databasens categorier och tar 
	 * ut de tags som är kopplade til en viss kategori.
	 * @param i 	int som ska visa på vilken categoriID som tagsen tillhör.
	 * @retunr list LinkedList som returneras, den innehåller Strings med TagNamn
	 */
	private static LinkedList<String> getTags(int i){
		LinkedList<String> list = new LinkedList<String>();
		try {
			stat = con.createStatement();
			String sql = "SELECT * FROM spargrisen.tag "
						+"WHERE tCategoryID="+i;
			resSet = stat.executeQuery(sql);
			while (resSet.next()) {
				list.add(resSet.getString("TagName"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	/*
	 * Metod som hämtar categorier ur databasen, skapar nya categoryobjekt av varje kategori
	 * i databasen. Varje Categori-objekt stoppas in i en CategoryList. Denna 
	 * i sin tur loopas igeno och varje kategori får sina tags hämtade.
	 * @param email    Sting med mailadress så att man skall kunna ta ut rätt kategorier ur databasen.
	 * @return catList CategoryList som inehåller användarens kategorier och tags.
	 */
	/**
	 * Metod som skapar ny användare i databasen.
	 * @param email    Stirng som inehåller mailadress för den nya användaren.
	 * @param name     String som inehåller användarens Namn.
	 * @param password Char[] som inehåller lösenordet för användaren. Sätts ihop i metoden till en string.
	 */
	public static void createNewUser( String email, String name, char[] password) {
		connectionToMysql();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < password.length; i++) {
			sb.append(password[i]);
		}
		String fs = sb.toString();
		System.out.println("Addning new user...");
		try {
			stat = con.createStatement();
			String sql = "INSERT INTO spargrisen.user VALUES('"
						 +email+"','"+name+"','"+fs+"')";
			stat.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("User added...");
		closeMysql();
	}
	/**
	 * Metod som Lägger till ny kategori i databasen.
	 * @param categoryName String som inehåller den nya kategorins namn.
	 * @param email		   String som inehåller mailadress så att man vet vilken användare kategorin tillhör.
	 * @param budget	   int som är kategorins budget.
	 * @param imageSource  String som inehåller namnet på bilden som skall användas till kategorin
	 */
	public static void addCategory(String categoryName, String email, int budget, String imageSource) {
		connectionToMysql();
		System.out.println("Addning new Category...");
		try {
			stat = con.createStatement();
			String sql = "INSERT INTO spargrisen.category "
					+ "VALUES(NULL,'"+categoryName+"', "+budget+", '"+email+"', '"+imageSource+"')";
			stat.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("Category added...");
		closeMysql();
	}
	/**
	 * Metod som lägger till tag med kategori id så att man vet vilken kategori tagen tillhör.
	 * @param tagName	 String med det nya tagnamnet.
	 * @param categoryID int som fungerar som kategori id.
	 */
	public static void addTag(String tagName, int categoryID) {
		connectionToMysql();
		System.out.println("Addning new Tag...");
		try {
			
			stat = con.createStatement();
			String sql = "INSERT INTO spargrisen.tag "
					+ "VALUES('" + tagName + "', " + categoryID + ")";
			stat.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("Tag added...");
		closeMysql();
	}
	/**
	 * Metod som tar bort tag från databasen.
	 * @param tagName	 String som inehåller tagens namn som ska tas bort.
	 * @param CategoryID int som inehåller CategoryID så att rätt tag blir borttagen.
	 */
	public static void removeTag(String tagName, int CategoryID) {
		connectionToMysql();
		try{
		System.out.println("Removing Tag...");

			String sql2 = "DELETE FROM spargrisen.tag "
					+ "WHERE tagName='"+tagName+"'AND tCategoryID="+CategoryID;
			stat.executeUpdate(sql2);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("Tag removed...");
		closeMysql();
	}
	/**
	 * Metod som låter användaren byta lösenord.
	 * @param mail		  String som inehåller maiadress för användaren som ska byta lösenord.
	 * @param newPassword char[] som inehåller det nya lösenordet.
	 */
	public static void changePass(String mail, char[] newPassword) {
		connectionToMysql();
		System.out.println("Editing user: "+mail+"...");
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < newPassword.length; i++) {
			sb.append(newPassword[i]);
		}
		String fs = sb.toString();
		try {
			stat = con.createStatement();
			String sql = "UPDATE spargrisen.user SET Password='"+
			fs+"' WHERE Email='"+mail+"'";
			stat.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		closeMysql();
	}
	/**
	 * Metod som kollar så att det angivna lösenordet stämmer verens med det som ligger i databsen.
	 * @param email	   String som inehåller mailadress.
	 * @param password char[] som inehåller lösenordet.
	 * @return pass    boolean som är tru om löseonrdet stämmer överens.
	 */
	public static boolean passwordMatch(String email, char[] password) {
		connectionToMysql();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < password.length; i++) {
			sb.append(password[i]);
		}
		String str = sb.toString();
		boolean pass=false;
		try {
			stat = con.createStatement();
			String sql = "SELECT Password FROM spargrisen.user"+
					" WHERE Email='"+email+"'";
			resSet = stat.executeQuery(sql);
			while (resSet.next()) {
				if(str.equals(resSet.getString("Password"))){
					pass=true;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		closeMysql();
		return pass;
	}

	/**
	 * Metod som hämtar alla köp från en användares bankkonto.
	 * @param bankkonto String med användarens bankkonto.
	 * @return list     LinkedList som inehåller Purchase-objekt.
	 */

	/**
	 * Metod som hämtar en användares lösenord.
	 * @param email String med användarens mailadress.
	 * @return str  String med användarens lösenord.
	 */
	public static String getPassword(String email) {
		connectionToMysql();
		String str = null;
		try {
			stat = con.createStatement();
			String sql = "SELECT Password FROM spargrisen.user"+
					" WHERE Email='"+email+"'";
			resSet = stat.executeQuery(sql);
			while (resSet.next()) {
				str = resSet.getString("Password");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		closeMysql();
		return str;
	}
	/**
	 * Metod som hämtar användarens namn.
	 * @param email String med användarens email.
	 * @return str  String med användarens namn.
	 */
	public static String getInfo(String email) {
		connectionToMysql();
		String str = null;
		try {
			stat = con.createStatement();
			String sql = "SELECT Namn FROM spargrisen.user"+
					" WHERE Email='"+email+"'";
			resSet = stat.executeQuery(sql);
			while (resSet.next()) {
				str = resSet.getString("Namn");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		closeMysql();
		return str;
	}
	/**
	 * Metod som hämtar en användares alla bankkonto ur databasen.
	 * @param email   String med emailadress.
	 * @return konton LinkedList med bankkontona i strings.
	 */
	public static LinkedList<String> getAllBankkonto(String email) {
		LinkedList<String> konton = new LinkedList<String>();
		connectionToMysql();
		try {
			stat = con.createStatement();
			String sql = "SELECT * FROM spargrisen.bankkonto"+
					" WHERE uEmail='"+email+"'";
			resSet = stat.executeQuery(sql);
			while (resSet.next()) {
				konton.add(resSet.getString("Bankkonto"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		closeMysql();
		return konton;
	}
	/**
	 * Metod som ändrar i den valda kategorien.
	 * @param catName	String med nytt kategorinamn.
	 * @param imgSource String med nytt bildnamn.
	 * @param budget	int med ny budget för kategorin.
	 * @param cateID	int som pekar på den kategorin som skall ändras i databsen.
	 */
	public static void editCategory(String catName,String imgSource, int budget, int cateID) {
		connectionToMysql();
		try {
			stat = con.createStatement();
			String sql = "UPDATE spargrisen.category" +
					" SET CategoryName='"+catName+"' , Budget="+budget+", Image='"+imgSource+"'"+
					" WHERE CategoryID="+cateID+"";
			stat.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Metod som går igenom alla tabeller och tar bort data som tillhör användaren.
	 * @param email String med emailadress till användaren som ska tas bort.
	 */
	public static void removeUser(String email){
		connectionToMysql();
		String str = null;
		LinkedList<Integer> intlist = new LinkedList<Integer>();
		LinkedList<String> kontolist = new LinkedList<String>();

		try {
			stat = con.createStatement();
			String sql = "SELECT * FROM spargrisen.category"+
					" WHERE cEmail='"+email+"'";
			resSet = stat.executeQuery(sql);
			while (resSet.next()) {
				intlist.add(resSet.getInt("CategoryID"));
			}
			while(!intlist.isEmpty()){
			int i = intlist.pop();
			stat = con.createStatement();
			System.out.println("Deleting tags...");
			String sql2 = "DELETE FROM spargrisen.tag "+
						"WHERE tCategoryID="+i;
			stat.executeUpdate(sql2);
			stat = con.createStatement();
			System.out.println("Deleting categorys...");
			String sql3 = "DELETE FROM spargrisen.Category "+
					"WHERE CategoryID="+i;
		    stat.executeUpdate(sql3);
			}
			stat = con.createStatement();
			String sql4 = "SELECT * FROM spargrisen.bankkonto "+
						"WHERE uEmail='"+email+"'";
			resSet = stat.executeQuery(sql4);
			while(resSet.next()){
				kontolist.add(resSet.getString("Bankkonto"));
			}
			while(!kontolist.isEmpty()){
				String konto = kontolist.pop();
				stat = con.createStatement();
				System.out.println("Deleting purchases...");
				String sql2 = "DELETE FROM spargrisen.purchase "+
							"WHERE pBankkonto='"+konto+"'";
				stat.executeUpdate(sql2);
				stat = con.createStatement();
				System.out.println("Deleting accounts...");
				String sql3 = "DELETE FROM spargrisen.bankkonto "+
						"WHERE Bankkonto='"+konto+"'";
			    stat.executeUpdate(sql3);
			}
			stat = con.createStatement();
			System.out.println("Deleting user...");
			String sql3 = "DELETE FROM spargrisen.user "+
					"WHERE Email='"+email+"'";
		    stat.executeUpdate(sql3);
			
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		closeMysql();
	}
	/**
	 * Metod som hämtar alla bankkonto ur databasen som fejkbanken kan använda för att stoppa in transkationer.
	 * @return list LinkedList som inehåller alla bankkonton.
	 */
	public static LinkedList<String> getAccounts(){
		connectionToMysql();
		LinkedList<String> list = new LinkedList<String>();
		try {
			stat = con.createStatement();
			String sql = "SELECT * FROM spargrisen.bankkonto ";
			resSet = stat.executeQuery(sql);
			while (resSet.next()) {
				list.add(resSet.getString("Bankkonto"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		closeMysql();
		return list;
	}
	/**
	 * Metod som lägger till konton for användare.
	 * @param email	  String som har emailadressen till användaren som ska ha nytt bankkonto.
	 * @param addBank String som inehåller det nya kontot.
	 */
	public static void addAccount(String email, String addBank) {
		connectionToMysql();
		System.out.println("Addning new Account...");
		try {
			
			stat = con.createStatement();
			String sql = "INSERT INTO spargrisen.bankkonto "
					+ "VALUES('" + addBank + "', '" + email + "')";
			stat.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("Tag added...");
		closeMysql();
	}
		
	
	/**
	 * Mainmetod, har använts för att testa metoderna.
	 * @param args
	 */
	public static void main(String[] args) {
//		removeUser("alex.siech@gmail.com");
//		String str = "pdm";
//		char[] cstr = str.toCharArray();
//		connectionToMysql();
//		createNewUser("menjie@pdm.mao", "Menjie Mao", cstr, "199111281111");
//		addCategory("Bajs", "menjie@pdm.com", 3000, "bajs");
//		addTag("menjie@pdm.mao", "Hemköp", "Osorterat");
//		changeBudgetLimit("Osorterat", "menjie@pdm.mao", 1500);
//		editUser("menjie@ass2mouth.mao", "menjie@pdm.com", cstr);
//		System.out.println(passwordMatch("menjie@pdm.com", cstr));
//		System.out.println(isUserFree("menjie@pdm.com"));
//		System.out.println(!isUserFree("menjie@pdm.com"));
//		System.out.println(getLastPurchase("menjie@pdm.com"));
//		CategoryList cat = retriveUser("jody.o.neill@gmail.com");
//		
//		removeUser("stmsfsd@gmail.com");
//		newPurchase("penis", "hemköo", 23, "2015.06.01", "timeStamp");
//		for(int i =0; i<cat.size(); i++){
//			System.out.println(cat.getCategory().getCategoryName());
		
//		System.out.println(retriveUser("jody.o.neill@gmail.com"));
//		System.out.println(getPassword("a"));
//		System.out.println(getAllBankkonto("a"));

	
		
	

	}
		
}

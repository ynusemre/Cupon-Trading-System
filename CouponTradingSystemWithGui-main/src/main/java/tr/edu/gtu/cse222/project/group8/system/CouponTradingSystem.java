/**
 * 
 */
package tr.edu.gtu.cse222.project.group8.system;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import tr.edu.gtu.cse222.project.group8.adt.Pair;

import tr.edu.gtu.cse222.project.group8.auction.Auction;
import tr.edu.gtu.cse222.project.group8.coupon.Coupon;
import tr.edu.gtu.cse222.project.group8.firm.Firm;
import tr.edu.gtu.cse222.project.group8.user.Admin;
import tr.edu.gtu.cse222.project.group8.user.FirmAdmin;
import tr.edu.gtu.cse222.project.group8.user.User;

/**
 * @author selman
 *
 */
public class CouponTradingSystem {
	
	public static final String baseDirectory =  new File("").getAbsolutePath() + "\\\\src\\\\main\\\\java\\\\tr\\\\edu\\\\gtu\\\\cse222\\\\project\\\\group8\\\\csv\\\\";
	
	public static final String userFile = baseDirectory + "users.csv";
	public static final String firmFile = baseDirectory + "firms.csv";  
	
	public static final String openAuctionsFile = baseDirectory + "open-auctions.csv"; 
	public static final String closedAuctionsFile = baseDirectory + "closed-auctions.csv"; 
	
	public static final String couponsFile = baseDirectory + "coupons.csv";
	
	public static final String ownedcouponsFile = baseDirectory + "coupons-that-owned.csv"; 
	public static final String auctionBidsFile = baseDirectory + "auction-bids.csv";
	
	public static final String openNotificationsFile = baseDirectory + "open-notifications.csv";
	public static final String closedNotificationsFile = baseDirectory + "closed-notifications.csv";

	public static final String walletReloadHistoryFile = baseDirectory + "wallet-reload-history.csv";
	
	public static final String tempFile = baseDirectory + "temp-file.csv";

	public static final int userIDSize = 8;
	public static final int couponIDSize = 10;
	public static final int auctionIDSize = 6;
	public static final int PASSWORDLENGTH = 6;
	public static final String ADMINID = "99999";

	public static final String ADMINPASSWORD = "99999";
	private Admin admin;
		
	public Auction auction;
	
	/**
	 * Empty Constructor
	 */
	public CouponTradingSystem() {
		super();
                auction = new Auction();
	}


	public CouponTradingSystem(Admin admin) {
		this.admin = admin;
                auction = new Auction();
	}

	private Object authentication(String id, String passwordl) {
		
		return null;
	}
	/**
     * 
     * @param id to login
     * @param password to password
     * @return user which loginned
     */
    public Pair<Object,Integer> login(String id, String password) {
        BufferedReader fileReader ;
            String line;
            //Create the file reader
        try {
            fileReader = new BufferedReader(new FileReader(CouponTradingSystem.userFile));
            //Read the file line by line

            while ((line = fileReader.readLine()) != null) {
                //Get all tokens available in line
                String[] tokens = line.split(";");
                if ((tokens[0].equals(id) && tokens[1].equals(password))) {
                    if(tokens[4].equals("0")){
                        User user = new User(tokens[1], tokens[2], tokens[3], id);
                        int dollar = Integer.parseInt(tokens[5]);
                        int cent = Integer.parseInt(tokens[6]);
                        user.reloadWallet(dollar, cent);
                        int type = 0;
                        fileReader.close();

                        return new Pair<>(user, type);
                    }

                    else if(tokens[4].equals("1")){
                        System.out.println("SSSSSSSSSSSSSSS    " + tokens[0]);
                        String firmName = FileHelper.returnDataFieldByCustomerID(new File(firmFile), tokens[0], 2);
                        System.out.println("SSSSSSSSSSSSSSSSSSSS    " + firmName);
                        Firm firm = new Firm(firmName);
                        int type = 1;
                        fileReader.close();
                        return new Pair<>(firm.getAdmin(), type);
                    }
                }
            }

            fileReader.close();
        } catch (FileNotFoundException e1) {

            e1.printStackTrace();
        } catch (IOException e2){
            e2.printStackTrace();
        }
        return new Pair<Object,Integer>(null, null);
    }

	public Boolean registerAsFirm(String name, String surname,String password, String firmName) {
		if(name == null || password == null || surname == null) throw new NullPointerException();
		if(password.length() < CouponTradingSystem.PASSWORDLENGTH) throw new ArithmeticException();
		if(FileHelper.checkDataField(new File(userFile), name, 3)) return false;
                
                Firm firm = new Firm(RandomKey.getAlphaNumericString(userIDSize), firmName,name, firmName, password);
                
                FileHelper.addLine(new File(userFile), firm.getAdmin().toWrite());
                FileHelper.addLine(new File(firmFile), firm.toWrite());

		return true;
	}
	
	public Boolean registerAsUser(String name, String surname,String password) {
		if(name == null || password == null || surname == null) throw new NullPointerException();
		if(password.length() < CouponTradingSystem.PASSWORDLENGTH) throw new ArithmeticException();
		if(FileHelper.checkDataField(new File(userFile), name, 3)) return false;

		FileHelper.addLine(new File(userFile), new User(password, name, surname, RandomKey.getAlphaNumericString(userIDSize)).toWrite());
		return true;
	}
	
//	private Wallet wallet;
//	private HashMap<Integer, User> registeredUsers;
//	private ArrayList<FirmAdmin> firmAdmins;
//	private LinkedList<Firm> firms;	
//	private Auction auction;
//	private Deque<Notification> inform;
	
//	public double takeCommission(int dolar, int cent) {return 0.0;}
//	public LinkedList<Coupon> displayCoupons() {return null;}
//	public boolean isValidCoupon(Coupon coupon) {return false;}

	public static void main(String[] args) {
		CouponTradingSystem system = new CouponTradingSystem();
		User user = new User("id", "password", "name", "lastname", 1000000, 1000000);
		user.test();
		
	}
}
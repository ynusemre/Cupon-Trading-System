/**
 * 
 */
package tr.edu.gtu.cse222.project.group8.user;

import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;

import tr.edu.gtu.cse222.project.group8.adt.MinHeap;
import tr.edu.gtu.cse222.project.group8.adt.SkipList;
import tr.edu.gtu.cse222.project.group8.auction.Auction;
import tr.edu.gtu.cse222.project.group8.coupon.Coupon;
import tr.edu.gtu.cse222.project.group8.firm.Firm;
import tr.edu.gtu.cse222.project.group8.system.CouponTradingSystem;
import tr.edu.gtu.cse222.project.group8.system.FileHelper;
import tr.edu.gtu.cse222.project.group8.system.Notification;
import tr.edu.gtu.cse222.project.group8.system.RandomKey;
import tr.edu.gtu.cse222.project.group8.system.Wallet;

/**
 * @author selman
 *
 */

public class User {

    @Override
    public String toString() {
        return "Name= " + name + ", LastName= " + lastName;
    }

    public Wallet getWallet() {
        return wallet;
    }
	
	/************************ Data Fields *************************************/
	
	
	private String id;
	private String password;
	private boolean loginStatus = false;
	private String name;
	private String lastName;
//	private CouponTradingSystem system;
//	private IdGenerator idGenerator;
	
	
	/* Coupons that on the sale. */
	private String couponsFileThatSaled = CouponTradingSystem.couponsFile; 
	
	/* Coupons that user owned */
	private String couponsFileThatOwned = CouponTradingSystem.couponsFile; 
	
	
	private HashMap<String, Coupon> onSaleCoupons;
	
	private HashMap<String, Coupon> couponsThatOwned;
	
	public SkipList<Coupon> foundedCoupons;
	
	
	
	
	
	
	
	private LinkedList<Coupon> purchasedCoupons;
	
	//private SkipList<String> openedAuction; //session stored
		
	private LinkedList<Coupon> soldCoupons;

	private Wallet wallet;
	private Deque<Notification> notifications;
	
	private Queue notify;
	
	public static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm");
	
	
	
	/***************************  METHODS *************************************/
	
	/**
	 * Constructor of the User Class
	 * @param password			
	 * @param name
	 * @param lastName
	 * @param system
	 */
	
	public User(String password, String name, String lastName, String id) {
            String []line =FileHelper.returnLine(new File(CouponTradingSystem.userFile), name, 3);
               if( line != null){
                   foundedCoupons=new SkipList<>();
                
		this.password = line[1];
		this.name = line[2];
		this.lastName = line[3];
		this.id = line[0];
                this.wallet = new Wallet(Integer.parseInt(line[5]),Integer.parseInt(line[6]));
		this.reloadWallet(Integer.parseInt(line[5]),Integer.parseInt(line[6]));
		this.notifications = new LinkedList<>();
		takeDataFromFile();
               }else{
                 foundedCoupons=new SkipList<>();
                
		this.password = password;
		this.name = name;
		this.lastName = lastName;
		this.id = id;
                this.wallet = new Wallet(0,0);
		this.reloadWallet(0, 0);
		this.notifications = new LinkedList<>();
		takeDataFromFile();  
               }
               
                
	}


	/* ID ; PASSWORD ; NAME ; SURNAME ; USER TYPE(0-USER, 1-FIRMADMIN, 2-ADMIN) ; dolar ; cent */
	public User(String id, String password, String name, String lastname, int dolar, int cent) {
		foundedCoupons=new SkipList<>();
		this.id = id;
		this.password = password;
		this.name = name;
		this.lastName = lastname;
		this.wallet = new Wallet(dolar,cent);
		reloadWallet(dolar, cent);
		this.notifications = new LinkedList<>();
		takeDataFromFile();
	}

	public String getId() {
		return this.id;
	}
        
        public Coupon addNewCoupon(String couponName, String expireDate, String firmName, int dolar, int cent) {
            if(couponName == null || expireDate == null) throw new NullPointerException();
            
            File file = new File(CouponTradingSystem.couponsFile);  //user dosyası
            if(FileHelper.checkDataField(file,couponName,6)) return null;
            Coupon newCoupon;
           
                
            newCoupon = new Coupon(RandomKey.getAlphaNumericString(CouponTradingSystem.couponIDSize), firmName, expireDate, dolar, cent, couponName, this.id);
            FileHelper.addLine(new File(CouponTradingSystem.couponsFile), newCoupon.toWrite());
            couponsThatOwned.put(newCoupon.getId(), newCoupon);
            
            if(FileHelper.checkID(new File(CouponTradingSystem.openNotificationsFile), newCoupon.getName())){
                    String temp[] = FileHelper.returnLine(new File(CouponTradingSystem.openNotificationsFile), newCoupon.getName());
                    FileHelper.removeByID(new File(CouponTradingSystem.openNotificationsFile), newCoupon.getName());
                    String line = temp[0] + ";" + temp[1] + ";" + temp[2] + ";" + temp[3];  
                    FileHelper.addLine(new File(CouponTradingSystem.closedNotificationsFile), line);
            }
            
            return newCoupon;
        }
	
	private void readFile(BufferedReader fileReader , HashMap<String, Coupon> map) throws Exception{
		String line;
	 	int counter = 0;
            //Read the file line by line
            while ((line = fileReader.readLine()) != null) {
			
                //Get all tokens available in line
                String[] temp = line.split(";");
                try {
                    if(counter !=0 ) {
                    	//String firmID = FileHelper.returnDataFieldByName(new File(CouponTradingSystem.firmFile), temp[1], 1);
                    	//Coupon newCoupon = new Coupon(temp[0],new Firm(firmID, temp[1]),temp[2],Integer.parseInt(temp[3]),Integer.parseInt(temp[4]));
						Coupon newCoupon = new Coupon(temp[0], temp[1], temp[2], Integer.parseInt(temp[3]),Integer.parseInt(temp[4]), temp[5], temp[6]);
                    	map.put(temp[0], newCoupon);
								
                    }
                 	
					counter++;
                } catch (ArrayIndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
            }
           
            
            fileReader.close();
		
	}
	
	
	private void takeDataFromFile() {
		
		BufferedReader fileReader ;
       
        try{

        	//Create the file reader
        	fileReader = new BufferedReader(new FileReader(CouponTradingSystem.couponsFile));
        	
        	onSaleCoupons = new HashMap<>();
   		
            readFile(fileReader, onSaleCoupons);
            
            fileReader = new BufferedReader(new FileReader(CouponTradingSystem.couponsFile));
        	
            couponsThatOwned = new HashMap<>();

            readFile(fileReader,couponsThatOwned);
   
        }catch (Exception e){
            e.printStackTrace();
        }
		
	}
	
	
	public void test() {
		
		//takeDataFromFile();
		
		//displayCouponsThatOwned();
		//printCouponsThatOnSale();

		/*MinHeap<Coupon> coupons = searchCoupon("mavi", 0, 10);




		while(!coupons.isEmpty()){
			System.out.println(coupons.remove().toString());
		}*/

		//newCoupon.setId("12457809");
		//sellCoupon("TEST firm2 TEST firm2 TEST firm2");
		//buyCoupon("TEST mavi TEST mavi TEST mavi");

		/*sellCoupon("TEST kirmizi TEST kirmizi TEST kirmizi");
		sellCoupon("TEST firm3 TEST firm3 TEST firm3");*/

		MinHeap<Coupon> coupons = searchCoupon("mavi",0,20);
		
		while(!coupons.isEmpty()){
			System.out.println(coupons.remove());
		}

		//sellCoupon("TEST sari TEST sari TEST sari");
		//notifyMe("TEST sari TEST sari TEST sari");
		//displayNotifications();


		
	
		
		
		
		
		
		//sellCoupon(newCoupon);
		
		
	}
	
	

	
	
	private boolean isLoggedIn() {return loginStatus;}
		
	public Object openAuction(Coupon coupon) {return null;}
	
	public int bidAuction(Object session, int amount) {return 0;}
	/**
	 * 
	 * @param coupon the coupon which is notified.
	 * @return true
	 */
	public boolean notifyMe(String name) {
		//String tokens[] = FileHelper.returnLine(new File(couponsFileThatOwned), name, 6);
		//Coupon coupon = new Coupon(tokens[0], tokens[1], tokens[2], Integer.parseInt(tokens[3]), Integer.parseInt(tokens[4]), tokens[5], tokens[6]);
		File file = new File(CouponTradingSystem.openNotificationsFile);
		String temp = name + ";the coupon is in stock!!!;" + this.getId() + ";" + LocalDate.now(); 
		FileHelper.addLine(file,temp );

		return true;




		/*if(FileHelper.checkDataField(file,coupon.getName(),1)){
			//open notificationdan to ID userını bul ve notification queue'na notification oluştur.
			

		}	*/	
		
	
	}
	/**
	 * display notifications from user
	 */
	public Deque<Notification> displayNotifications() {
		
		BufferedReader fileReader ;
        String line;
		Coupon coupon = null;
        //Create the file reader
		try {
			fileReader = new BufferedReader(new FileReader(CouponTradingSystem.closedNotificationsFile));
			//Read the file line by line
			
			while ((line = fileReader.readLine()) != null) {

				//Get all tokens available in line
				String[] tokens = line.split(";");
				try {
					if ((tokens[2].equals(this.getId()))) {
						for(String key: onSaleCoupons.keySet()) {
							
							if(onSaleCoupons.get(key).getName().equals(tokens[0])){
								coupon = onSaleCoupons.get(key);
							}
						}
						System.out.println(coupon.toString());	
						Notification notification = new Notification(tokens[0], this, LocalDate.now(), LocalTime.now(), coupon);
						notifications.offerFirst(notification);
						System.out.println(notifications.getFirst().toString());
					}


				} catch (ArrayIndexOutOfBoundsException e) {
					e.printStackTrace();
				}
			}
			
			fileReader.close();	
		} catch (FileNotFoundException e1) {
			
			e1.printStackTrace();
		} catch (IOException e2){
			e2.printStackTrace();
		}

		


            return notifications;
				
	}
	
	
	
	
	public void reloadWallet(int dolar ,int cent) {
                
		wallet.reload(dolar, cent);

		if(FileHelper.removeByID(new File(CouponTradingSystem.walletReloadHistoryFile), getId())){
			String line = getId() + ";" + Integer.toString(dolar) + ";" + Integer.toString(cent); 
			FileHelper.addLine(new File(CouponTradingSystem.walletReloadHistoryFile), line);
		}
		else{
			String line = getId() + ";" + Integer.toString(dolar) + ";" + Integer.toString(cent);
			FileHelper.addLine(new File(CouponTradingSystem.walletReloadHistoryFile), line);
		}
		displayWallet();
	}
	
	public void printCouponsThatOnSale() {
					
			for(String key: onSaleCoupons.keySet()) {
				
				System.out.println(onSaleCoupons.get(key).toString());
			}	
	}
	
	/*
	 * User can sell a coupon that owned.
	 * Check user is owned this coupon
	 * That coupon will added to couponsOnSale data file.
	 * @param name the coupon which is notified. 
	 */

	public boolean sellCoupon(String name) {
		String tokens[] = FileHelper.returnLine(new File(couponsFileThatOwned), name, 6);
		if(tokens[0] == null){
			return false;
		}
		System.out.println(tokens[5]);
		Coupon coupon = new Coupon(tokens[0], tokens[1], tokens[2], Integer.parseInt(tokens[3]), Integer.parseInt(tokens[4]), tokens[5], tokens[6]);
		String data = FileHelper.returnDataFieldByID(new File(couponsFileThatOwned), coupon.getId(), 8);
		Integer stock = Integer.parseInt(data);
		if(stock == 1){
			FileHelper.removeByID(new File(couponsFileThatOwned), coupon.getId());
		}
		else{
			String line = coupon.getId() + ";" + coupon.getFirm().getFirmName() + ";" + formatter.format(coupon.getExpireDate()) + ";" + coupon.getDolar() + ";" + coupon.getCent() + ";" + coupon.getName() + ";" + coupon.getOwnerID() + ";" + Integer.toString(stock-1);
			FileHelper.removeByID(new File(couponsFileThatOwned), coupon.getId());
			FileHelper.addLine(new File(couponsFileThatOwned), line);
		}
		data = FileHelper.returnDataFieldByID(new File(couponsFileThatSaled), coupon.getId(), 8);
		if(data == null){
			FileHelper.addLine(new File(couponsFileThatSaled), coupon.toWrite());
		}
		else{
			stock = Integer.parseInt(data);
			String line = coupon.getId() + ";" + coupon.getFirm().getFirmName() + ";" + formatter.format(coupon.getExpireDate()) + ";" + coupon.getDolar() + ";" + coupon.getCent() + ";" + coupon.getName() + ";" + coupon.getOwnerID() + ";" + Integer.toString(stock+1);
			FileHelper.removeByID(new File(couponsFileThatSaled), coupon.getId());
			FileHelper.addLine(new File(couponsFileThatSaled), line);
		}
		
		if(FileHelper.checkID(new File(CouponTradingSystem.openNotificationsFile), coupon.getName())){
			String temp[] = FileHelper.returnLine(new File(CouponTradingSystem.openNotificationsFile), coupon.getName());
			FileHelper.removeByID(new File(CouponTradingSystem.openNotificationsFile), coupon.getName());
			String line = temp[0] + ";" + temp[1] + ";" + temp[2] + ";" + temp[3];  
			FileHelper.addLine(new File(CouponTradingSystem.closedNotificationsFile), line);
		}
		return true;
						
	}
	
	/*
	 * Checks the users wallet
	 * Change owner id for the coupon in data file.
	 * 
	 */
	
	/*private void buyCoupon(Coupon coupon) {
		
		double couponAmount = (coupon.getDolar() + coupon.getCent() / 100 );
		
		System.out.println("asdas   " + couponAmount);
		if(wallet.amount() >= couponAmount) {
		
		

			int dolar = wallet.getDollar() - coupon.getDolar();
			int cent = wallet.getCent() -  coupon.getCent();
			
			changeCouponStock(coupon, false);
			changeCouponOwner(coupon,false);
			
			reloadWallet(dolar,cent);
						
		}
		else {
			System.out.println(" You have not enough money ");
			
			double needed = couponAmount - wallet.amount();
		
			System.out.println("You need : " +  needed );
		}
		
	}*/
	public boolean buyCoupon(String name){
		
		String tokens[] = FileHelper.returnLine(new File(couponsFileThatSaled), name, 6);
                if(tokens == null){
                    throw new NoSuchElementException();
                }
		System.out.println(tokens[1]);
		System.out.println(tokens[0]);
		Coupon coupon = new Coupon(tokens[0], tokens[1], tokens[2], Integer.parseInt(tokens[3]), Integer.parseInt(tokens[4]), tokens[5], tokens[6]);
		double couponAmount = (coupon.getDolar() + coupon.getCent() / 100 );
                
		if(wallet.amount() >= couponAmount) {

			String data = FileHelper.returnDataFieldByID(new File(couponsFileThatSaled), coupon.getId(), 8);
			Integer stock = Integer.parseInt(data);
			String olderOwner = coupon.getOwnerID();
			coupon.setOwnerID(getId());
			if(stock == 1){
				
				FileHelper.removeByID(new File(couponsFileThatSaled), coupon.getId());
				String line = coupon.toWrite();
				FileHelper.addLine(new File(couponsFileThatOwned), line);
			}
			else{
				FileHelper.removeByID(new File(couponsFileThatSaled), coupon.getId());
				String line = coupon.getId() + ";" + coupon.getFirm().getFirmName() + ";" + formatter.format(coupon.getExpireDate()) + ";" + coupon.getDolar() + ";" + coupon.getCent() + ";" + coupon.getName() + ";" + coupon.getOwnerID() + ";" + Integer.toString(stock-1);
				FileHelper.addLine(new File(couponsFileThatSaled), line);
				data = FileHelper.returnDataFieldByID(new File(couponsFileThatOwned), coupon.getId(), 8);
				if(data == null){
					line = coupon.toWrite();
					FileHelper.addLine(new File(couponsFileThatOwned), line);
				}
				else{
					stock = Integer.parseInt(data);
					FileHelper.removeByID(new File(couponsFileThatOwned), coupon.getId());
					line = coupon.getId() + ";" + coupon.getFirm().getFirmName() + ";" + formatter.format(coupon.getExpireDate()) + ";" + coupon.getDolar() + ";" + coupon.getCent() + ";" + coupon.getName() + ";" + coupon.getOwnerID() + ";" + Integer.toString(stock+1);
					FileHelper.addLine(new File(couponsFileThatOwned), line);
				}								
			}
			int dolar = wallet.getDollar() - coupon.getDolar();
			int cent = wallet.getCent() -  coupon.getCent();
			reloadWallet(dolar,cent);
			data = FileHelper.returnDataFieldByID(new File(CouponTradingSystem.walletReloadHistoryFile), olderOwner, 2);
			dolar = Integer.parseInt(data);
			data = FileHelper.returnDataFieldByID(new File(CouponTradingSystem.walletReloadHistoryFile), olderOwner, 3);
			cent = Integer.parseInt(data);
			dolar+=coupon.getDolar();
			cent+=coupon.getCent();
			FileHelper.removeByID(new File(CouponTradingSystem.walletReloadHistoryFile), olderOwner);
			String line = olderOwner + ";" + Integer.toString(dolar) + ";" + Integer.toString(cent); 
			FileHelper.addLine(new File(CouponTradingSystem.walletReloadHistoryFile), line);
			return true;
		}
		else{
                    
			return false;
		}

	}		
	private void changeCouponStock(Coupon coupon , boolean flag) {
		
		BufferedReader fileReader ;
        String line;
        //Create the file reader

        try{

            fileReader = new BufferedReader(new FileReader(couponsFileThatSaled));
            FileWriter tempFile = new FileWriter("temp.csv",true);
            //Read the file line by line
            while ((line = fileReader.readLine()) != null) {
				
                //Get all tokens available in line
                String[] tokens = line.split(";");
                try {
                    if (!(tokens[1].equals(coupon.getId()))) {
                        tempFile.append(line);
                        tempFile.append("\n");
                    }
                    else{
                    	Integer temp_stock = Integer.parseInt(tokens[7]);
                    	if(flag) {

                        	temp_stock++;
                        	
                    	}else {
                    		temp_stock--;
                        	
						}
                    	if(temp_stock != 0) {

                        	for(int i = 0; i<7; i++) {
                        		tempFile.append(tokens[i]);
                        		tempFile.append(";");
                        	}
                        	
                        	tempFile.append(temp_stock.toString());
                        	tempFile.append("\n");	
                    	}	
                    }

                } catch (ArrayIndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
            }

            if (flag) {
				tempFile.append(coupon.toString());
			}
            
            fileReader.close();
            tempFile.close();
            File temp = new File("temp.csv");
            File oldFile = new File(couponsFileThatSaled);
            if (oldFile.exists())
                oldFile.delete();
            boolean s =  temp.renameTo(oldFile);
            if (!s)
                System.out.println("Operation is failed ");
        }catch (Exception e){
            e.printStackTrace();
        }

		
	}
	
	private void changeCouponOwner(Coupon coupon , boolean flag) {
		
		BufferedReader fileReader ;
        String line;
        //Create the file reader

        try{

            fileReader = new BufferedReader(new FileReader(couponsFileThatOwned));
            FileWriter tempFile = new FileWriter("temp.csv",true);
            //Read the file line by line
            while ((line = fileReader.readLine()) != null) {
				
                //Get all tokens available in line
                String[] tokens = line.split(";");
                try {
                    if (!(tokens[1].equals(coupon.getId()))) {
                        tempFile.append(line);
                        tempFile.append("\n");
                    }
                    else{                   	
                    	for(int i = 0; i<6; i++) {
                    		tempFile.append(tokens[i]);
                    		tempFile.append(";");
                    	}
                    	
                    	if (flag) {
							tempFile.append("00000000");
						}
                    	else {
                        	tempFile.append(this.id);	
						}

                    	tempFile.append(tokens[7]);
                		tempFile.append(";");
                    	tempFile.append("\n");	

                        System.out.println("Coupon has soled.");
                    }

                } catch (ArrayIndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
            }

            fileReader.close();
            tempFile.close();
            File temp = new File("temp.csv");
            File oldFile = new File(couponsFileThatOwned);
            if (oldFile.exists())
                oldFile.delete();
            boolean s =  temp.renameTo(oldFile);
            if (!s)
                System.out.println("Operation is failed ");
        }catch (Exception e){
            e.printStackTrace();
        }
		
	}
	
	private void displayWallet() {
		
		System.out.println("You have " + this.wallet.amount() + "dolar in your wallet."); 
		
	}
		
	public List<Coupon> displayCouponsThatOwned() {
		
		List<Coupon> coupons = new ArrayList<>();
		BufferedReader fileReader;
		
		String line;
		
		try {
			fileReader = new BufferedReader(new FileReader(CouponTradingSystem.couponsFile));
			
			while( (line = fileReader.readLine()) !=null  ) {

				//Get all tokens available in line
                String[] tokens = line.split(";");
                try {
                	
                    if (tokens[6].equals(this.id)) {
                    	String firmID = FileHelper.returnDataFieldByName(new File(CouponTradingSystem.firmFile), tokens[1], 1);
						String firmAdminID = FileHelper.returnDataFieldByID(new File(CouponTradingSystem.firmFile), firmID, 3);
						coupons.add(new Coupon(tokens[0],new Firm(firmID, tokens[1],firmAdminID),tokens[2],Integer.parseInt(tokens[3]),Integer.parseInt(tokens[4]), tokens[5],tokens[6]));					
                    }

                } catch (ArrayIndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
			}
			
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return coupons;
		
	}
        
        public String toWrite(){
            return(id+";" + password+ ";" + name + ";" + lastName +";0;"+ getWallet().getDollar()+ ";"+ getWallet().getCent());
        }

	/*
	 * If coupon is valid deletes succesfully.
	 */
	
	public void removeCoupon(Coupon coupon) {
		
		BufferedReader fileReader ;
        String line;
        //Create the file reader

        try{

            fileReader = new BufferedReader(new FileReader(couponsFileThatOwned));
            FileWriter tempFile = new FileWriter("temp.csv",true);
            //Read the file line by line
            while ((line = fileReader.readLine()) != null) {

                //Get all tokens available in line
                String[] tokens = line.split(";");
                try {
                    if (!(tokens[1].equals(coupon.getId()))) {
                        tempFile.append(line);
                        tempFile.append("\n");
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
            }

            fileReader.close();
            tempFile.close();
            File temp = new File("temp.csv");
            File oldFile = new File(couponsFileThatOwned);
            if (oldFile.exists())
                oldFile.delete();
            boolean s =  temp.renameTo(oldFile);
            if (!s)
                System.out.println("Operation is failed ");
        }catch (Exception e){
            e.printStackTrace();
        }
	}
	/*public boolean searchCoupon(String name){
		Coupon newCoupon = new Coupon(name, null, null, 0, 0);
		
		foundedCoupons.find(newCoupon);
	}*/
	public MinHeap<Coupon> searchCoupon(String name,int lowerBound, int upperBound) {

		boolean flag = false;
                
                //takeDataFromFile();
             
      
		for(String key: onSaleCoupons.keySet()) {
			
			String temp = onSaleCoupons.get(key).toString();
		
			if (temp.contains(name)) {
				
				foundedCoupons.insert(onSaleCoupons.get(key));
				flag = true;
			}
		}
 
		if(!flag){
			System.out.println(" Searched coupon not found! ");
		}
		return searchByPrice(lowerBound, upperBound);	
	}
	
	private MinHeap<Coupon> searchByPrice(int lowerBound, int upperBound ) {
		
		MinHeap<Coupon> que = new MinHeap<>();
		Iterator<Coupon> iter = foundedCoupons.descendingIterator();
		while(iter.hasNext()){
			Coupon coupon = iter.next();
			double price = coupon.getDolar()  + coupon.getCent() /100;
			if( price > lowerBound && price < upperBound){
				que.add(coupon);
			 }	
		}
		if(que.isEmpty()){
			return null;
		}

		return que;
		
		
	}
	
	
}

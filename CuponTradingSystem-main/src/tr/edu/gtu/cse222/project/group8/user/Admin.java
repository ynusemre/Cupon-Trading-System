package tr.edu.gtu.cse222.project.group8.user;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

import tr.edu.gtu.cse222.project.group8.coupon.Coupon;
import tr.edu.gtu.cse222.project.group8.firm.Firm;
import tr.edu.gtu.cse222.project.group8.system.CouponTradingSystem;
import tr.edu.gtu.cse222.project.group8.system.FileHelper;
import tr.edu.gtu.cse222.project.group8.system.RandomKey;

/**
 * Admin class to represent the admin of the Coupon Trading System.
 * @author Sena
 *
 */
public class Admin{
	private String password;
	private String id;

	public Admin(String password) {
		this.password = password;
		this.id = CouponTradingSystem.ADMINID;
	}
	
	/**
	 * Add a new firm.
	 * @param firmName to add
	 * @return firm if succesfull, otherwise null.
	 */
	public Firm addFirm(String firmName, String firmAdminName, String firmAdminLastName, String password){
        if(firmName == null || firmAdminName == null || firmAdminLastName == null || password == null) throw new NullPointerException();
		if(password.length() < CouponTradingSystem.PASSWORDLENGTH) throw new ArithmeticException();
        
		File file = new File(CouponTradingSystem.firmFile);  //user dosyası
        if(FileHelper.checkDataField(file,firmName,2)) return null;
        Firm newFirm = new Firm(RandomKey.getAlphaNumericString(CouponTradingSystem.userIDSize), firmName,firmAdminName, firmAdminLastName, password);

        //check if firm already exist or not.
        if(FileHelper.addLine(file, newFirm.toWrite())){
            newFirm.setAdmin(new FirmAdmin(password, firmAdminName, firmAdminLastName, newFirm));
            FileHelper.addLine(new File(CouponTradingSystem.userFile), newFirm.getAdmin().toWrite());
            return newFirm;
        }

        return null;
    }

	public boolean removeFirm(String firmID){
		File file = new File(CouponTradingSystem.firmFile);
		return(FileHelper.removeByID(file, firmID));
	}

	public boolean removeUser(String userID){
		File file = new File(CouponTradingSystem.userFile);
		
		return(FileHelper.removeByID(file, userID));
	}
	
	public boolean removeFirmAdmin(String firmAdminID){
		File file = new File(CouponTradingSystem.userFile);
		
		return(FileHelper.removeByID(file, firmAdminID));
	}
	public boolean removeCoupon(String couponID){
		File file = new File(CouponTradingSystem.couponsFile);
		
		return(FileHelper.removeByID(file, couponID));
	}

	/**
	 * Display coupons of the given user.
	 * @param ID of the user
	 * @return List of the coupons
	 * @throws NoSuchElementException if user does not exist.
	 */
	public List<Coupon> displayCoupons(String userID) throws NoSuchElementException{
		File file = new File(CouponTradingSystem.couponsFile);
		List<Coupon> coupons = new ArrayList<>();
		if(FileHelper.checkID(new File(CouponTradingSystem.userFile), userID)){
			try {
				Scanner reader = new Scanner(file);
				while(reader.hasNextLine()){
					String data = reader.nextLine();
					
					String[] temp = data.split(";");
					if(temp[6].equals(userID)){
						String firmID = FileHelper.returnDataFieldByName(new File(CouponTradingSystem.firmFile), temp[1], 1);
						String firmAdminID = FileHelper.returnDataFieldByID(new File(CouponTradingSystem.firmFile), firmID, 3);	
						coupons.add(new Coupon(temp[0],new Firm(firmID, temp[1], firmAdminID),temp[2],Integer.parseInt(temp[3]),Integer.parseInt(temp[4]), temp[5],temp[6]));
					}
				}
				
				reader.close();
			}
			
			catch (IOException e) {
				System.err.println("Error occured while reading/writing from/to file.\n");
			}

			return coupons;
		}

		else throw new NoSuchElementException();
	}

	public boolean isValidCoupon(String id){
		if(!FileHelper.checkID(new File(CouponTradingSystem.couponsFile), id)) return false;
		String date = FileHelper.returnDataFieldByID(new File(CouponTradingSystem.couponsFile), id, 3);
		if(date == null) return false;
		Date dateFormat;
		try {
			dateFormat = Coupon.formatter.parse(date);
			return(dateFormat.compareTo(new Date()) > 0);
		} 
		
		catch (ParseException e) {
			e.printStackTrace();
		}
		
		return false;
	}

	/**
	 * Check if auction open or ended.
	 * @param auctionID to check
	 * @return true if open, false otherwise.
	 * @throws NoSuchElementException if auctionID does not exist.
	 */
	public boolean checkAuctionStatus(String auctionID) throws NoSuchElementException{
		File openFile = new File(CouponTradingSystem.openAuctionsFile);
		File closeFile = new File(CouponTradingSystem.closedAuctionsFile);
		
		if(FileHelper.checkID(openFile, auctionID)) return true;
		if(FileHelper.checkID(closeFile, auctionID)) return false;
		else{
			throw new NoSuchElementException();
		}
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getID() {
		return this.id;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (!(o instanceof Admin)) {
			return false;
		}
		Admin admin = (Admin) o;
		return this.id.equals(admin.id);
	}


	@Override
	public String toString() {
		return ("ID: " + getID() + " - Password: " + getPassword());
	}
}

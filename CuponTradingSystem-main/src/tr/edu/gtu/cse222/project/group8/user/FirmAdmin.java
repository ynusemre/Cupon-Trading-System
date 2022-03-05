/**
 * 
 */
package tr.edu.gtu.cse222.project.group8.user;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.TreeSet;

import tr.edu.gtu.cse222.project.group8.auction.Auction;
import tr.edu.gtu.cse222.project.group8.coupon.Coupon;
import tr.edu.gtu.cse222.project.group8.firm.Firm;
import tr.edu.gtu.cse222.project.group8.system.CouponTradingSystem;
import tr.edu.gtu.cse222.project.group8.system.FileHelper;
import tr.edu.gtu.cse222.project.group8.system.RandomKey;


/**
 * @author selman
 * @author emrea 
 */
public class FirmAdmin {

    private String id;
    private String password;
    private boolean loginStatus = false;
    private String name;
    private String lastName;

    public Firm getFirm() {
        return firm;
    }
    private Firm firm;


    /**
     * @param password
     * @param name
     * @param lastName
     * @param firm
     */
    public FirmAdmin(String password, String name, String lastName, Firm firm) {
        super();
        this.id = RandomKey.getAlphaNumericString(CouponTradingSystem.userIDSize);
        this.password = password;
        this.name = name;
        this.lastName = lastName;
        this.firm = firm;
    }

    public FirmAdmin(String id, Firm firm){
        String[] line = FileHelper.returnLine(new File(CouponTradingSystem.userFile), id);
        if(line == null) throw new NoSuchElementException();
        this.id = line[0];
        this.password = line[1];
        this.name = line[2];
        this.lastName = line[3];
        this.firm = firm;
    }

    public FirmAdmin(String id, String password, String name, String lastName, int dolar, int cent) {
		this.id = id;
		this.password = password;
		this.name = name;
		this.lastName = lastName;
	}

    public FirmAdmin(String token) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

	public String toWrite() {
            return(id+";" + password+ ";"+name + ";" + lastName +";1;0;0");
        }

    public LinkedList<Coupon> soldCouponsDisplay() {
        return this.firm.listSoldCoupons();
    }

    public LinkedList<Coupon> onSaleCouponsDisplay() {
        return this.firm.listOnSaleCoupons();
    }

    public void addCoupon(Coupon coupon, int stock) {
        this.firm.addNewCoupon(coupon, stock);
    }

    public void deleteCoupon(Coupon coupon) {
        this.firm.removeCoupon(coupon);
    }

    public Coupon find(String id) {
        TreeSet<Firm.CouponStock> onSaleCoupons = firm.getonSaleCoupons();
        Firm.CouponStock temp;
        Iterator it = onSaleCoupons.iterator();

        while (it.hasNext()) {
            temp = (Firm.CouponStock) it.next();
            if (temp.getCoupon().getId().equals(id))
                return temp.getCoupon();
        }
        System.out.println(" finded coupon not found! ");

        return null;
    }

    public String getId() {
        return this.id;
    }

    public void search(String str) {
        TreeSet<Firm.CouponStock> onSaleCoupons = firm.getonSaleCoupons();
        Firm.CouponStock temp;
        Iterator it = onSaleCoupons.iterator();

        while (it.hasNext()) {
            temp = (Firm.CouponStock) it.next();
            if (temp.getCoupon().getName().indexOf(str) != -1)
                System.out.println(temp.getCoupon().toString());
        }
    }

    public void setLoginStatus(boolean status) {
        loginStatus = status;
    }
    
    public void userInterface() {

        int choice = 1;
        Integer cupKey;
        Scanner input = new Scanner(System.in);

        while (choice != 0) {

            System.out.println("\n*****Welcome to the Coupon Trading System*****");
            System.out.println("Please select an option to continue...");
            System.out.println("1 - Display Firm Coupons");
            System.out.println("2 - Add Firm Coupons");
            System.out.println("3 - Delete Firm Coupons ");
            System.out.println("4 - Search in the Firm's Coupons ");
            System.out.println("0 - Back \n");

            choice = input.nextInt();


            switch (choice) {
                case 1:
                    System.out.println("On Sale Coupons :");
                    onSaleCouponsDisplay();
                    System.out.println("Sold Coupons :");
                    soldCouponsDisplay();
                    break;
                case 2:
                    System.out.println("Enter the number of Coupons: ");
                    int stock = input.nextInt();

                    input.nextLine();

                    System.out.println("Enter the expire Date: ");
                    String date = input.nextLine();
                    
                    System.out.println("Enter the name of coupon: ");
                    String name = input.nextLine();


                    addCoupon(new Coupon(RandomKey.getAlphaNumericString(CouponTradingSystem.couponIDSize), this.firm, date,0, 0, name), stock);
                    break;

                case 3:
                    onSaleCouponsDisplay();
                    input.nextLine();
                    System.out.println("Enter the id of Coupon to remove: ");
                    String id = input.nextLine();
                    deleteCoupon(find(id));
                    break;

                case 4:
                    input.nextLine();
                    System.out.println("Search : ");
                    String keyword = input.nextLine();

                    search(keyword);

                    break;

                default:
                    break;
            }
        }
        input.close();
    }

}


	

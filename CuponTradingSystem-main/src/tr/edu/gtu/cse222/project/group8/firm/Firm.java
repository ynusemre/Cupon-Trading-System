/**
 * 
 */
package tr.edu.gtu.cse222.project.group8.firm;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.TreeSet;
import java.util.NoSuchElementException;

import tr.edu.gtu.cse222.project.group8.adt.BinarySearchTree;
import tr.edu.gtu.cse222.project.group8.coupon.Coupon;
import tr.edu.gtu.cse222.project.group8.system.CouponTradingSystem;
import tr.edu.gtu.cse222.project.group8.system.FileHelper;
import tr.edu.gtu.cse222.project.group8.system.FileObj;
import tr.edu.gtu.cse222.project.group8.system.RandomKey;
import tr.edu.gtu.cse222.project.group8.user.FirmAdmin;

/**
 * @author selman
 *
 */
public class Firm implements FileObj{
	
	public class CouponStock implements Comparable<CouponStock>{

            public CouponStock(Coupon coupon, int stock) {
                this.stock = stock;
                this.coupon = coupon;
            }
		private Coupon coupon;
		private int stock = 1;
		
		/**
		 * @return the coupon
		 */
		public Coupon getCoupon() {
			return coupon;
		}
		/**
		 * @param coupon the coupon to set
		 */
		public void setCoupon(Coupon coupon) {
			this.coupon = coupon;
		}
		/**
		 * @return the stock
		 */
		public int getStock() {
			return stock;
		}
		/**
		 * @param stock the stock to set
		 */
		public void setStock(int stock) {
			this.stock = stock;
		}
		
		@Override
		public String toString() {
		    return coupon.toString() + "Stock: " + this.stock;
		}

		@Override
		public int compareTo(CouponStock o) {
		    return 1;
		}		
	}
	
	private String id;
	private String firmName;
	private LinkedList<CouponStock> soldCoupons;
	private TreeSet<CouponStock> onSaleCoupons;
	private FirmAdmin admin;
        
       
	
	public Firm(String firmName){
		this.id = FileHelper.returnDataFieldByName(new File(CouponTradingSystem.firmFile), firmName, 1);
		String[] line = FileHelper.returnLine(new File(CouponTradingSystem.firmFile), this.id);
		if(line == null) throw new NoSuchElementException();
        
        	this.firmName = line[1];
		this.admin = new FirmAdmin(line[2], this);
		
		onSaleCoupons = new TreeSet<CouponStock>();
		soldCoupons = new LinkedList<CouponStock>();
	}
	
	/**
	 * @param idGenerator
	 * @param firmName
	 */
	public Firm(String firmName, FirmAdmin admin){
		super();
		this.firmName = firmName;
		this.admin = admin;
		this.id = RandomKey.getAlphaNumericString(CouponTradingSystem.userIDSize);
		
		onSaleCoupons = new TreeSet<CouponStock>();
		soldCoupons = new LinkedList<CouponStock>();	
	}

	public Firm(String id, String firmName, FirmAdmin admin) {
		this.id = id;
		this.firmName = firmName;
		this.admin = admin;
		onSaleCoupons = new TreeSet<CouponStock>();
		soldCoupons = new LinkedList<CouponStock>();
	}

	public Firm(String id, String firmName, String firmAdminName, String firmAdminLastName, String password) {
		this.id = id;
		this.firmName = firmName;
		this.admin = new FirmAdmin(password, firmAdminName, firmAdminLastName, this);
		onSaleCoupons = new TreeSet<CouponStock>();
		soldCoupons = new LinkedList<CouponStock>();
	}

	public Firm(String id, String firmName, String firmAdminID) {
		this.id = id;
		this.firmName = firmName;
		this.admin = new FirmAdmin(firmAdminID,this);
		onSaleCoupons = new TreeSet<CouponStock>();
		soldCoupons = new LinkedList<CouponStock>();
	}

	
	public TreeSet<CouponStock> getonSaleCoupons() {
		return onSaleCoupons;
	}
	
	public void addNewCoupon(Coupon coupon, int stock) {
		onSaleCoupons.add(new CouponStock(coupon, stock));
	}

	public void soldCoupon(Coupon coupon) {
       		listSoldCoupons().add(coupon);
    	}
	
	public Coupon removeCoupon(Coupon coupon) {
		Iterator it = onSaleCoupons.iterator();
		while (it.hasNext()) {
		    CouponStock temp = (CouponStock) it.next();
		    if (temp.coupon.equals(coupon)) {
			Coupon ret = temp.coupon;
			it.remove();
			return ret;
		    }
		}

		return null;
	}
	
	public LinkedList<Coupon> listOnSaleCoupons() {
            Iterator it = onSaleCoupons.iterator();
            LinkedList<Coupon> ret = new LinkedList();
            CouponStock temp;

            while (it.hasNext()) {
                temp = (CouponStock) it.next();
                System.out.println(onSaleCoupons.size());
                System.out.println(temp.toString());
                ret.add(temp.coupon);
            }
            if(!it.hasNext())
                return ret;

            return null;
        }
	
	public LinkedList<Coupon> listSoldCoupons() {
		Iterator it = soldCoupons.iterator();

		Coupon temp;

		while (it.hasNext()) {
		    temp = (Coupon) it.next();
		    System.out.println(temp.toString());
		}

		return null;
	}


	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFirmName() {
		return this.firmName;
	}

	public void setFirmName(String firmName) {
		this.firmName = firmName;
	}

	public FirmAdmin getAdmin() {
        return this.admin;
    }

    public void setAdmin(FirmAdmin admin) {
        this.admin = admin;
    }

	@Override
	public String toWrite() {
		return(this.id + ";" + this.firmName + ";" + this.admin.getId());
	}

	public static Firm getFirm(String name) {
		return null;
	}

}

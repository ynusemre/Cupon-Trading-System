/**
 * 
 */
package tr.edu.gtu.cse222.project.group8.system;

import java.time.LocalDate;
import java.time.LocalTime;

import tr.edu.gtu.cse222.project.group8.coupon.Coupon;
import tr.edu.gtu.cse222.project.group8.user.User;

/**
 * @author selman
 *
 */
public class Notification {
	
	private String message;
	private User to;
	private LocalDate date;
	private LocalTime time;
	private Coupon coupon = null;
	
	/**
	 * @param to
	 * @param coupon
	 */
	
	public Notification(User to, Coupon coupon) {
		super();
		this.to = to;
		this.coupon = coupon;
	}
	

	public Notification(String message, User to, LocalDate date, LocalTime time, Coupon coupon) {
		this.message = message;
		this.to = to;
		this.date = date;
		this.time = time;
		this.coupon = coupon;
	}
	public String toString(){
		return coupon.getName(); 
	}

}

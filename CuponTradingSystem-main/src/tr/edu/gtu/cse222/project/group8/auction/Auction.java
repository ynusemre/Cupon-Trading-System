/**
 * 
 */
package tr.edu.gtu.cse222.project.group8.auction;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;


import tr.edu.gtu.cse222.project.group8.system.CouponTradingSystem;
import tr.edu.gtu.cse222.project.group8.system.RandomKey;
import tr.edu.gtu.cse222.project.group8.adt.MSYHeap;
import tr.edu.gtu.cse222.project.group8.coupon.Coupon;
import tr.edu.gtu.cse222.project.group8.firm.Firm;
import tr.edu.gtu.cse222.project.group8.user.FirmAdmin;
import tr.edu.gtu.cse222.project.group8.user.User;

/**
 * @author selman
 *
 */
public class Auction {
	
	private class Session implements Comparable<Session>{
		
		private class Offer implements Comparable<Offer>{
			
			// Data Fields
			private String id;
			private User user;
			private int dolar;
			private int cent;
			
			/**
			 * @param user
			 * @param dolar
			 * @param cent
			 */
			public Offer(User user, int dolar, int cent, String id) {
				this.id = id;
				this.user = user;
				this.dolar = dolar;
				this.cent = cent;
			}
			
			@Override
			public String toString() {
				StringBuffer buffer = new StringBuffer();
				buffer.append("Auction ID : " + this.id + "\n");
				buffer.append("Bid : " + this.dolar + " dolar " + this.cent + " cent " +"\n");
				buffer.append("User : " + "\n");
				buffer.append(user.toString() + "\n"); //user toString check
				buffer.append("---------------------");
				return buffer.toString();
			}
			
			public String toWrite() {
				/* AUCTION ID ; DOLAR ; CENT ; USER ID */
				StringBuffer buffer = new StringBuffer();
				buffer.append(id + ";");
				buffer.append(dolar + ";");
				buffer.append(cent + ";");
				buffer.append(user.getId());
				return buffer.toString();		
			}
			
			public int getDolar() {
				return dolar;
			}
			
			public int getCent() {
				return cent;
			}
			
			public User getUser() {
				return this.user;
			}
			
			public String getId() {
				return this.id;
			}

			@Override
			public int compareTo(Offer o) {
				if (this.dolar != o.getDolar()) {
					return this.dolar - o.getDolar();
				}else {
					return this.cent - o.getCent();
				}
			}
			
		}
		
		// Data Fields
		private String id;
		private Coupon coupon;
		private Object seller; // firm or user (instance of)
		private MSYHeap<Offer> bidder; // minheap suan max heap yapilmali
		private LocalDateTime expireDate; 
		
		private User buyer = null;
		private int dolar;
		private int cent;
		
		/**
		 * @param coupon
		 * @param seller
		 * @param expireDate
		 * @throws Exception 
		 */
		public Session(Coupon coupon, Object seller, LocalDateTime expireDate) throws Exception {
			this.id = RandomKey.getAlphaNumericString(8);
			this.coupon = coupon;
			if (seller instanceof User || seller instanceof FirmAdmin ) {
				this.seller = seller;
			}else {
				throw new Exception("Seller can be User or FirmAdmin!");
			}
			this.expireDate = expireDate;
			this.bidder = new MSYHeap<Offer>();
			this.openSession();
		}
		
		public Session(String id, String couponId, String sellerId, LocalDateTime expireDate) throws Exception {
			/* AUCTION ID ; COUPON ID ; SELLER ID ; EXPIRE DATE */ //open-auctions file 
			this.id = id;
			this.coupon = this.getCouponObject(couponId);
			this.seller = this.getUserOject(sellerId);
			this.expireDate = expireDate;
			this.bidder = this.getOffers(id);
		}
		
		public Session(String id, String couponId, String sellerId, LocalDateTime expireDate, int dolar, int cent, String buyerId) throws Exception {
			/* AUCTION ID ; COUPON ID ; SELLER ID ; EXPIRE DATE ; DOLAR ; CENT ; USER ID */ //closed-auctions file
			this.id = id;
			this.coupon = this.getCouponObject(couponId);
			this.seller = this.getUserOject(sellerId);
			this.expireDate = expireDate;
			this.dolar = dolar;
			this.cent = cent;
			this.buyer = (User) this.getUserOject(buyerId);
		}
		
		private Coupon getCouponObject(String id) throws Exception { // icindeki contructorlar diger objelerin icerisinde doldurulmali
			/* ID ; FIRM NAME ; EXPIRE DATE ; PRICE DOLAR ; PRICE CENT ; COUPON NAME ; OWNER ID ; STOCK*/
			
			String line = "";
//			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"); 
			try {
				BufferedReader read = new BufferedReader(new FileReader(CouponTradingSystem.couponsFile));
				while ( (line = read.readLine()) != null) {
					String[] tempLine = line.split(";");
						
					if(tempLine[0].compareTo(id) == 0) {
//						LocalDateTime dateTime = LocalDateTime.parse(tempLine[2], formatter);	
						Coupon tempCoupon = new Coupon(tempLine[0],tempLine[1],tempLine[2],Integer.parseInt(tempLine[3]),
								Integer.parseInt(tempLine[4]),tempLine[5],tempLine[6]);
						read.close();
						return tempCoupon;
					}	
				}
				read.close();		
				throw new Exception("Coupon lost..");
			} catch ( IOException e) {
				System.err.println(e);
				return null;
			}	
		}
		
		private Object getUserOject(String id) throws Exception { // icindeki contructorlar diger objelerin icerisinde doldurulmali
			/* ID ; PASSWORD ; NAME ; SURNAME ; USER TYPE(0-USER, 1-FIRMADMIN, 2-ADMIN) ; dolar ; cent */

			String line = "";
			Object tempObject = null;
//			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"); 
			try {
				BufferedReader read = new BufferedReader(new FileReader(CouponTradingSystem.userFile));
				while ( (line = read.readLine()) != null) {
					String[] tempLine = line.split(";");
						
					if(tempLine[0].compareTo(id) == 0) {
//						LocalDateTime dateTime = LocalDateTime.parse(tempLine[2], formatter);
						if (Integer.parseInt(tempLine[4])==0) {
							tempObject = new User(tempLine[0],tempLine[1],tempLine[2],tempLine[3],Integer.parseInt(tempLine[4]),
									Integer.parseInt(tempLine[5]));
						}else if (Integer.parseInt(tempLine[4])==1) {
							
							// FirmAdmin(String password, String name, String lastName, Firm firm)
							// firm filedan cekilip constructore verilmeli
							tempObject = new FirmAdmin(tempLine[0],tempLine[1],tempLine[2],tempLine[3],Integer.parseInt(tempLine[4]),
									Integer.parseInt(tempLine[5]));
						}
						read.close();
						return tempObject;
					}	
				}
				read.close();		
				throw new Exception("Coupon lost..");
			} catch ( IOException e) {
				System.err.println(e);
				return null;
			}	
		}
		
		private MSYHeap<Offer> getOffers(String id) throws NumberFormatException, Exception{

			/* AUCTION ID ; DOLAR ; CENT ; USER ID */
			MSYHeap<Offer> output = new MSYHeap<>();
			String line = "";
			try {
				BufferedReader read = new BufferedReader(new FileReader(CouponTradingSystem.userFile));
				while ( (line = read.readLine()) != null) {
					String[] tempLine = line.split(";");
					if(tempLine[0].compareTo(id) == 0) {
						Offer tempOffer = new Offer((User)this.getUserOject(tempLine[3]), Integer.parseInt(tempLine[1]), Integer.parseInt(tempLine[2]), id);
						output.add(tempOffer);
					}	
				}
				read.close();
				return output;
			} catch ( IOException e) {
				System.err.println(e);
				return null;
			}	
		}
		
		
		@Override
		public String toString() {
			StringBuffer buffer = new StringBuffer();
			buffer.append("Coupon : " + "\n");
			buffer.append(this.coupon.toString());
			buffer.append("Seller : " + "\n");
			buffer.append(this.seller.toString());
			buffer.append("---------------------");
			return buffer.toString();
		}
		
		public String toWrite() throws Exception {
			/* AUCTION ID ; COUPON ID ; SELLER ID ; EXPIRE DATE */
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"); 
			StringBuffer buffer = new StringBuffer();
			buffer.append(id + ";");
			buffer.append(coupon.getId() + ";");
			if (seller instanceof User) {
				buffer.append(((User) seller).getId()+ ";");
			}else if(seller instanceof FirmAdmin) {
				buffer.append(((FirmAdmin) seller).getId()+ ";");
			}else {
				throw new Exception("Seller is not proper!");
			}
			buffer.append(this.expireDate.format(formatter));
			return buffer.toString();		
		}
		
		public boolean endSession() {
			Offer winOffer = bidder.remove();
			return addBuyer(winOffer);
			
		}
		
		private boolean addBuyer(Offer offer) {
			this.buyer = offer.getUser();
			this.dolar = offer.getDolar();
			this.cent = offer.getCent();
			
			if (closeSession(offer)) {
				return true;
			}
			return false;
		}

		public Offer offer(User user, int dolar, int cent) {
			Offer tempOffer = new Offer(user, dolar, cent, this.id);
			if (appendOfferToFile(tempOffer)) {
				bidder.add(tempOffer);
				return tempOffer;
			}
			return null;
		}
		
		private boolean appendOfferToFile(Offer offer) {
			FileWriter writer;
			try {
				writer = new FileWriter(CouponTradingSystem.auctionBidsFile,true);
				writer.append(offer.toWrite());
				writer.append("\n");
				writer.flush();
				writer.close();
			} catch (IOException e) {
				System.err.println(e);
				return false;
			}
			return true;			
		}
		
		private boolean closeSession(Offer offer) {
			/* AUCTION ID ; DOLAR ; CENT ; USER ID */ //auction-bids file 
			/* AUCTION ID ; COUPON ID ; SELLER ID ; EXPIRE DATE */ //open-auctions file 
			/* AUCTION ID ; COUPON ID ; SELLER ID ; EXPIRE DATE ; DOLAR ; CENT ; USER ID */ //closed-auctions file
			String line = "";
			try {
				FileWriter writer = new  FileWriter(CouponTradingSystem.tempFile);
				FileWriter writer2 = new  FileWriter(CouponTradingSystem.closedAuctionsFile);
				BufferedReader read = new BufferedReader(new FileReader(CouponTradingSystem.openAuctionsFile));
				
				while ( (line = read.readLine()) != null) {
					String[] tempLine = line.split(";");
		
					if (offer.getId().compareTo(tempLine[0]) == 0 
							&& offer.getUser().getId() == tempLine[3]) {
						StringBuffer buff = new StringBuffer();
						buff.append(this.toWrite());
						buff.append(";");
						buff.append(offer.toWrite());
						writer2.append(buff.toString());
						writer2.append("\n");
						writer2.flush();
						writer2.close();
					}else if (offer.getId().compareTo(tempLine[0]) == 0 
							&& offer.getUser().getId() != tempLine[3]) {
						System.out.println("losers are deleted");
					}else {
						writer.append(line);
						writer.append("\n");
					}
				}
				read.close();
				writer.flush();
				writer.close();
				
				File f1 = new File(CouponTradingSystem.openAuctionsFile);
				if (f1.delete()) {
					File f2 = new File(CouponTradingSystem.tempFile);
					f2.renameTo(f1);
				}
				return true;
			} catch ( IOException e) {
				System.err.println(e);
				return false;
			} catch (Exception e) {
				System.err.println(e);
				return false;
			}	
		}
		
		private boolean openSession() throws Exception {
			FileWriter writer;
			try {
				writer = new FileWriter(CouponTradingSystem.openAuctionsFile,true);
				writer.append(this.toWrite());
				writer.append("\n");
				writer.flush();
				writer.close();
			} catch (IOException e) {
				System.err.println(e);
				return false;
			}
			return true;
		}

		public LocalDateTime getExpireDate() {
			return this.expireDate;
		}
		
		@Override
		public int compareTo(Session o) {
			return this.expireDate.compareTo(o.getExpireDate());
		}
		
	}
		
	public ArrayList<Session> openAuctionList() throws Exception {
		/* AUCTION ID ; COUPON ID ; SELLER ID ; EXPIRE DATE */ //open-auctions file 
		ArrayList<Session> output = new ArrayList<>();
		String line = "";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"); 

		try {
			BufferedReader read = new BufferedReader(new FileReader(CouponTradingSystem.openAuctionsFile));
			while ( (line = read.readLine()) != null) {
				String[] tempLine = line.split(";");
					
				LocalDateTime dateTime = LocalDateTime.parse(tempLine[3], formatter);					
				Session tempSession = new Session(tempLine[0], tempLine[1], tempLine[2], dateTime);
				output.add(tempSession);
			}
			read.close();			
			return output;
		} catch ( IOException e) {
			System.err.println(e);
			return null;
		}	
	}
	
	public ArrayList<Session> closedAuctionList() throws NumberFormatException, Exception {
		/* AUCTION ID ; COUPON ID ; SELLER ID ; EXPIRE DATE ; DOLAR ; CENT ; USER ID */ //closed-auctions file
		ArrayList<Session> output = new ArrayList<>();
		String line = "";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"); 

		try {
			BufferedReader read = new BufferedReader(new FileReader(CouponTradingSystem.closedAuctionsFile));
			while ( (line = read.readLine()) != null) {
				String[] tempLine = line.split(";");
					
				LocalDateTime dateTime = LocalDateTime.parse(tempLine[3], formatter);					
				Session tempSession = new Session(tempLine[0], tempLine[1], tempLine[2], dateTime, 
						Integer.parseInt(tempLine[4]), Integer.parseInt(tempLine[5]), tempLine[6]);
				
				output.add(tempSession);
			}
			read.close();			
			return output;
		} catch ( IOException e) {
			System.err.println(e);
			return null;
		}	
	}
	
	
	public boolean endAuction(Session session){
		return session.endSession();
	}
	
	public Session createAuction(Coupon coupon, Object seller, LocalDateTime expireDate) {
		try {
			Session tempSession = new Session(coupon, seller, expireDate);
			return tempSession;
		} catch (Exception e) {
			System.err.println(e);
			return null;
		}
	}
	
	public boolean offer(Session session, User user, int dolar, int cent) {
		if(session.offer(user, dolar, cent) != null) {
			return true;
		}
		return false;
	}

	/**
	 * UNIT TEST
	 * @param args
	 */
//	public static void main(String[] args) {
//		
//		
//	}
	
}

package tr.edu.gtu.cse222.project.group8.system;

import java.util.Stack;


public class Wallet {

    private class Money {
        private int dolar;
        private int cent;

        /**
         * @return the dolar
         */

        public double getAmount() {
            return dolar + cent/100;
        }

        /**
         * @param amount the dolar to set
         */

        public void setAmount(int dolar, int cent) {
            this.dolar = dolar;
            this.cent = cent;
        }

        /**
         * @param newAmount to be added to amount
         */
        public void addAmount(int dolar, int cent) {
            dolar += dolar;
            cent += cent;
        }

        /**
         * @param amount to be set
         *               Constructor
         */
        public Money(int dolar, int cent) {
            this.dolar = dolar;
            this.cent = cent;
        }
        
        public int getDolar() {
        	return dolar;
        }
        
        public int getCent() {
        	return cent;
        }
    }

    public Money money;

    

    public void reload(int dollar, int cent) {
    	this.setAmount(dollar, cent);

    }

    private void addAmount(int dolar, int cent) {
        money.addAmount(dolar,cent);

    	history.add(new Money(dolar,cent));
    }

    public void setAmount(int dolar, int cent) {
		history.add(new Money(dolar,cent));
		money.setAmount(dolar,cent);
    }

    public int getDollar(){
        return money.getDolar();
    }

    public int getCent(){
        return money.getCent();
    }

    public double amount() {
        return money.getAmount();
    }

    public Stack<Money> history;

    /**
     * wallet constructor
     */
    public Wallet() {
        super();
        this.money = new Money(0,0);
        this.history = new Stack<Money>();
    }

    public Wallet(int dolar, int cent) {
        super();
        this.money = new Money(dolar,cent);
        this.history = new Stack<Money>();	}

	public static void main(String [] args){
        Wallet wallet = new Wallet();
        wallet.addAmount(5,99);
        System.out.println(wallet.getDollar() + "," + wallet.getCent());
        wallet.setAmount(78,999);
        System.out.println(wallet.getDollar() + "," + wallet.getCent());
    }

}

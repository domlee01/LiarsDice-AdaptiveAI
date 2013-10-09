package com.dice;

import java.util.Random;

public class me_player extends player {
	
	int lv = 1;
	public int get_lv() {return lv;}
	public void set_lv(int lv) {this.lv = lv;}
		
	int exp_lucky = 0;
	public double prob_lucky = 0.0;
	public int get_exp_lucky() {return exp_lucky;}
	
	int exp_exchange = 0;
	public int get_exp_exchange() {return exp_exchange;}
	int exp_peep = 0;
	public int get_exp_peep() {return exp_peep;}
	
	public me_player(){
		super();		
	}				
	
	public void load_lucky(int exp){
		exp_lucky = exp;
		prob_lucky = (double)exp_lucky / (double)1000.0;
	}
	public void load_exchange(int exp){
		exp_exchange = exp;		
	}
	public void load_peep(int exp){
		exp_peep = exp;		
	}
	
	public void shake(){
		if (exp_lucky > 0) {
			Random r = new Random();
			double x = r.nextDouble();
			boolean applylucky = (x < prob_lucky);
			this.lucky = applylucky;
		}
		super.shake();
	}
	
	public void set_prob_lucky(boolean win) {
		if (win && prob_lucky >= 0.35)
			this.prob_lucky -= 0.15;
		else if (!win && prob_lucky<=0.65)
			this.prob_lucky += 0.15;
	}
	
		 
	
}

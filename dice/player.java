package com.dice;

import java.util.Random;

public class player {

	dicecup player_dices;
		
	boolean plusone_dice;
	boolean empty_dice;
	
	int player_life;
	public String player_name;
	
	public int called_dice;
	public int called_count;
	
	/* SPECIALS */
	public boolean lucky = false;
	
	public player(){player_dices = new dicecup(); plusone_dice=false; empty_dice=false; player_life=100;}
	
	public dicecup getdices() {return player_dices;};
	public boolean getplusone() {return plusone_dice;};
	public boolean getempty() {return empty_dice;};
	public int getlife() {return player_life;};
	public void setplusone(boolean d) {plusone_dice=d;};
	public void setempty(boolean d) {empty_dice=d;};
	
	public void shake(){
		plusone_dice=false; empty_dice=false;
		if (!lucky)			
			player_dices.generate();
		else 
			player_dices.generate_lucky();			
		
		check_special_cases();
		/*
		if ((player_dices.dice_numbers[0]==1 && player_dices.dice_numbers[1]==1 && player_dices.dice_numbers[2]==1 && player_dices.dice_numbers[3]==1 && player_dices.dice_numbers[4]==1)||
			(player_dices.dice_numbers[1]==1 && player_dices.dice_numbers[2]==1 && player_dices.dice_numbers[3]==1 && player_dices.dice_numbers[4]==1 && player_dices.dice_numbers[5]==1))
			empty_dice = true;
		else {
			for (int i=0; i<player_dices.dice_numbers.length; i++){
				if (player_dices.dice_numbers[i] == 5)
					plusone_dice = true;
			}
		}
		*/
	}
	
	public void check_special_cases(){
		empty_dice = false;
		plusone_dice = false;
		if ((player_dices.dice_numbers[0]==1 && player_dices.dice_numbers[1]==1 && player_dices.dice_numbers[2]==1 && player_dices.dice_numbers[3]==1 && player_dices.dice_numbers[4]==1)||
				(player_dices.dice_numbers[1]==1 && player_dices.dice_numbers[2]==1 && player_dices.dice_numbers[3]==1 && player_dices.dice_numbers[4]==1 && player_dices.dice_numbers[5]==1))
				empty_dice = true;
			else {
				for (int i=0; i<player_dices.dice_numbers.length; i++){
					if (player_dices.dice_numbers[i] == 5)
						plusone_dice = true;
				}
			}
	}

	public boolean open(diceplay game) throws InterruptedException
	{			
		//Thread.sleep(500);
		
		double ptrue = prob_true(game.currentDiceNum, game.currentCalledTotal, game.players.size()-1, game.one_used);
		/*double pnext = next_best_prob(game);
		System.out.println("ptrue = "+ ptrue + " next best = "+ pnext);
		if (pnext > ptrue)
			return false;
		*/
		boolean bopen =  prob_open(ptrue);
		
		return bopen;		
	}
	
	private boolean prob_open(double ptrue){
		Random r = new Random();
		double rand = r.nextDouble();
		
		//System.out.println("rand: "+ rand + " - ptrue: " + ptrue);
		
		return (rand>ptrue);	
		
	}
	
	/*
	private double prob_true2(int DiceNum, int CalledTotal, int playerCount, boolean one_used){
		
		double d1 = 1.0/6.0, d2 = 5.0/6.0; 
		int x_count = CalledTotal - player_dices.dice_numbers[DiceNum-1];
		if (!one_used && DiceNum != 1) {
			x_count -= player_dices.dice_numbers[0];
			d1 = 2.0/6.0; d2 = 4.0/6.0;
		}		
		if (x_count <= 0)
			return 1.0;
		int n_count = playerCount*5;
		//* combo(n, x)
		double p_total = 0;
		for (int i=x_count-1; i>=0; i--) {
			long combo = dice_ai.combination(n_count, i);
			double p_i = combo * Math.pow(d1, i) * Math.pow(d2, (n_count-i));
			p_total += p_i;
			System.out.println(" x = "+ i + " Combo = "+combo + " p(i) = " + p_i);
		}
		double ptrue = 1.0 - p_total;
		
		return ptrue;
	}*/
	
	protected double prob_true(int DiceNum, int CalledTotal, int playerCount, boolean one_used){
		
		double p1 = 1.0/6.0, p2 = 5.0/6.0; 
		int x_count = CalledTotal - player_dices.dice_numbers[DiceNum-1];
		if ((!one_used) && (DiceNum != 1)) {
			x_count -= player_dices.dice_numbers[0];
			p1 = 2.0/6.0; p2 = 4.0/6.0;
		}
		int n_count = playerCount*5;
		
		double ptrue = dice_ai.prob_true(x_count, n_count, p1, p2);
		return ptrue;
	}
	
	public void next_best_call(diceplay game){
		int cur_dice = game.currentDiceNum;
		int cur_tot = game.currentCalledTotal;
		
		double pnext = 0.0;
		int dicenext = cur_dice;
		int totnext = cur_tot;
		for (int i=cur_dice+1; i<=6; i++){
			double p = prob_true(i, cur_tot, game.players.size()-1, game.one_used);
			//System.out.println(i+" - "+cur_tot+" - "+p);
			if (p>pnext) {
				pnext = p;
				dicenext = i;
			}
		}
		
		for (int i=1; i<=6; i++){
			double p = prob_true(i, cur_tot+1, game.players.size()-1, game.one_used);
			//System.out.println(i+" - "+(cur_tot+1)+" - "+p);
			if (p>pnext) {
				pnext = p;
				dicenext = i;
				totnext = cur_tot +1;
			}		
		}
		game.call(dicenext, totnext);		
		this.called_dice = dicenext;
		this.called_count = totnext;
		return;
	}
	
	protected double next_best_prob(diceplay game){
		int cur_dice = game.currentDiceNum;
		int cur_tot = game.currentCalledTotal;
		
		double pnext = 0.0;
		for (int i=cur_dice+1; i<=6; i++){
			double p = prob_true(i, cur_tot, game.players.size()-1, game.one_used);
			if (p>pnext) {
				pnext = p;
			}
		}
		
		for (int i=1; i<=6; i++){
			double p = prob_true(i, cur_tot+1, game.players.size()-1, game.one_used);
			if (p>pnext) {
				pnext = p;
			}		
		}
		return pnext;
	}	
	
	public int damage_life() {
		/*if (player_life >= 20)
			player_life -= 20;
		else
			player_life = 0;
		return player_life;*/
		return damage_life(20);
	}
	
	public int damage_life(int hit) {
		if (player_life >= hit)
			player_life -= hit;
		else
			player_life = 0;
		return player_life;
	}
	
	public void reset(){plusone_dice=false;empty_dice=false;lucky = false;called_dice=0;called_count=0;};
		
	
}

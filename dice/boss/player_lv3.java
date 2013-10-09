package com.dice.boss;

import com.dice.dice_ai;
import com.dice.diceplay;
import com.dice.player;


public class player_lv3 extends player {
	
	public player_lv3(){
		super();
	}
	
	public boolean open(diceplay game) throws InterruptedException
	{			
		//Thread.sleep(1000);
		
		double ptrue = prob_true(game.currentDiceNum, game.currentCalledTotal, game.players.size()-1, game.one_used);
		double pnext = next_best_prob(game);
		//System.out.println("ptrue = "+ ptrue + " next best = "+ pnext);
				
		return dice_ai.prob_open(ptrue, pnext);		
	}
	
	
	public void next_best_call(diceplay game){
		int cur_dice = game.currentDiceNum;
		int cur_tot = game.currentCalledTotal;
		
		double pnext = 0.0;
		int dicenext = cur_dice;
		int totnext = cur_tot;
		if (cur_dice != 1){
			for (int i=6; i>cur_dice; i--){
				double p = prob_true(i, cur_tot, game.players.size()-1, game.one_used);
				//System.out.println(i+" - "+cur_tot+" - "+p);
				if (p>pnext) {
					pnext = p;
					dicenext = i;
				}
			}
		}
		
		for (int i=6; i>=1; i--){
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
}

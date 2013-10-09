package com.dice.boss;

import java.util.Random;

import com.dice.dice_ai;
import com.dice.diceplay;
import com.dice.player;


public class player_lv8 extends player {

	private double pliar = 0.5;
	
	public player_lv8(){
		super();
	}
	
	public boolean open(diceplay game) throws InterruptedException
	{			
		//Thread.sleep(1000);
		
		double ptrue = prob_true(game.currentDiceNum, game.currentCalledTotal, game.players.size()-1, game.one_used);
		double pnext = next_best_prob(game);
		//System.out.println("OPEN: ptrue = "+ ptrue + " next best = "+ pnext);
				
		return dice_ai.prob_open(ptrue, pnext);		
	}
	
	
	public void next_best_call(diceplay game){
		int cur_dice = game.currentDiceNum;
		int cur_tot = game.currentCalledTotal;
		
		double pnext = 0.0;
		int dicenext = cur_dice;
		int totnext = cur_tot;
		Random r = new Random();
		double rand = r.nextDouble();
		boolean lie = (rand < pliar);
		//System.out.println("lie :"+ lie + " pliar = " + pliar + " rand = " + rand);
		if (cur_dice != 1){
			for (int i=6; i>cur_dice; i--){
				double p = prob_true(i, cur_tot, game.players.size()-1, game.one_used);
				//System.out.println(i+" - "+cur_tot+" - "+p);
				if (lie) {
					p = dice_ai.prob_true_with_pliar(p, pliar);
					if (i==1) p=p/2;
				}
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
			if (lie) {
				p = dice_ai.prob_true_with_pliar(p, pliar);
				if (i==1) p=p/2;
			}
			//System.out.println(i+" - "+(cur_tot+1)+" - "+p);
			if (p>pnext) {
				pnext = p;
				dicenext = i;
				totnext = cur_tot +1;
			}		
		}
		if ((lie) && (pliar < 1.0))
			pliar += 0.1;
		game.call(dicenext, totnext);
		this.called_dice = dicenext;
		this.called_count = totnext;		
		return;
	}
	
	public void reset(){
		super.reset();
		pliar = 0.5;
	};
}

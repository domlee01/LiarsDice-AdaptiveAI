package com.dice.boss;

import java.util.ArrayList;
import java.util.Random;

import com.dice.diceplay;
import com.dice.player;


public class player_lv2 extends player {

	private class dicecall {
		int dice;
		int total;
		dicecall(int d, int t) {dice=d; total=t;}
	}
	public player_lv2(){
		super();
	}
	
	public void next_best_call(diceplay game){
		int cur_dice = game.currentDiceNum;
		int cur_tot = game.currentCalledTotal;
		ArrayList<dicecall> randcalls = new ArrayList<dicecall>();
		double pnext = 0.0;
		if (cur_dice != 1){
			for (int i=cur_dice+1; i<=6; i++){
				double p = prob_true(i, cur_tot, game.players.size()-1, game.one_used);
				//System.out.println(i+" - "+cur_tot+" - "+p);
				if (p==pnext) {
					randcalls.add(new dicecall(i, cur_tot));
				} else if (p>pnext) {
					randcalls.clear();
					randcalls.add(new dicecall(i, cur_tot));
					pnext = p;
				}			
			}
		}
		
		for (int i=1; i<=6; i++){
			double p = prob_true(i, cur_tot+1, game.players.size()-1, game.one_used);
			//System.out.println(i+" - "+(cur_tot+1)+" - "+p);
			if (p==pnext) {
				randcalls.add(new dicecall(i, cur_tot +1));
			}else if (p>pnext) {
				randcalls.clear();
				randcalls.add(new dicecall(i, cur_tot +1));
				pnext = p;
			}			
		}
		Random r = new Random();
		int rand = r.nextInt(randcalls.size());
		
		//System.out.println("size of arr "+ randcalls.size() + " r = " + rand);
		game.call(randcalls.get(rand).dice, randcalls.get(rand).total);
		this.called_dice = randcalls.get(rand).dice;
		this.called_count = randcalls.get(rand).total;		
		return;
	}
}

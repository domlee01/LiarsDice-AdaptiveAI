package com.dice;

import java.util.ArrayList;
import java.util.Arrays;

public class diceplay {

	public boolean one_used = false;
	public int currentDicer=-1;
	
	public ArrayList<player> players = new ArrayList<player>(); 
	public me_player me;
		
	public int currentDiceNum=0;
	public int currentCalledTotal=0;	
	
	public int totaldices;
	
	public void one_called()
	{
		one_used = false;
	}
	
	public void myDices(me_player me){
		this.me = me;
		//this.me.shake();
		players.add(this.me);
	}
	
	public void playerDices(player player){
		//player.shake();
		players.add(player);
	}
	
	public void shake(){
		//me.shake();
		currentDiceNum=0;
		currentCalledTotal=players.size();
		for (int i=0; i<players.size(); i++)
			players.get(i).shake();
	}
	
	public void reset(){
		one_used = false;
		currentDicer=0;			
		currentDiceNum=0;
		currentCalledTotal=players.size();
		for (int i=0; i<players.size(); i++)
			players.get(i).reset();
	}
	
	public void getAllDicecups(){
		for (int i=0; i<players.size(); i++)
			System.out.println("Player ["+ i + "] : " + Arrays.toString(players.get(i).getdices().dice_numbers));		
	}
	public boolean validate(int diceNum, int howMany){
		if (howMany<players.size() || (howMany <= currentCalledTotal && diceNum <= currentDiceNum))
			return false;
		if ((howMany==currentCalledTotal) && (diceNum<=currentDiceNum))
			return false;
		
		call(diceNum, howMany);
		return true;
	}
	
	public void call(int diceNum, int howMany){
		currentDiceNum = diceNum;
		currentCalledTotal = howMany;
		
		if ((!one_used) && (diceNum == 1)){
			one_used = true;
		}
			
	}
	
	public boolean Open(){
		int totalplayerdices = 0;
		for(int i=0;i<players.size();i++)
		{
			if (players.get(i).getempty())
				continue;
			totalplayerdices += players.get(i).getdices().getNumDices(currentDiceNum-1);
			if (!one_used){
				totalplayerdices += players.get(i).getdices().getNumDices(0);
			}			
			if (players.get(i).getplusone() && players.get(i).getdices().getNumDices(currentDiceNum-1) > 0)
				totalplayerdices ++;
		}
		totaldices = totalplayerdices;
		System.out.println("Total "+ currentDiceNum + ": "+ totalplayerdices);
		return (currentCalledTotal > totalplayerdices);
	}
	
	public void punish(int open_pid, boolean win, boolean pdouble){
		int loser = (win)?currentDicer:open_pid;
		players.get(loser).damage_life();
		if (pdouble)
			players.get(loser).damage_life();
		/*
		if (win){
			players.get(currentDicer).damage_life();			
		}else{
			players.get(open_pid).damage_life();	
		}	*/
		
		me.set_prob_lucky((loser!=0));
	}
	
	public boolean gameover(){
		if (me.getlife() <= 0) {
			System.out.println("You have 0 life. Game Over");
			return true;
		}
		for(int i=0;i<players.size();i++)
		{
			if (players.get(i).getlife() <= 0) {
				System.out.println("Player "+i+" have 0 life. Game Over");				
				return true;
			}
		}
		return false;
	}
	
	public int[] getAllLives(){
		int [] lives = new int[players.size()];
		for (int i=0; i<players.size(); i++)
			lives[i] = players.get(i).getlife();
		
		return lives;
	}
	
	public int getlife(int i){
		return players.get(i).getlife();
	}
}

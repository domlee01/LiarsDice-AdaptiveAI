package com.dice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import android.os.Handler;
import android.widget.Toast;

import com.costumedicer.CostumeDicer;
import com.costumedicer.DicerSpecials;
import com.costumedicer.R;
import com.costumedicer.story.StoryLineDialog;
import com.dice.boss.player_lv1;
import com.dice.boss.player_lv2;
import com.dice.boss.player_lv3;
import com.dice.boss.player_lv4;
import com.dice.boss.player_lv5;
import com.dice.boss.player_lv6;
import com.dice.boss.player_lv7;
import com.dice.boss.player_lv8;
import com.dice.boss.player_lv9;

public class dicegame {

	private Random r;
	
	public int num_computers;
	public int lvl;
	public diceplay game;
	public player[] gamecomputers;
	
	public DicerSpecials specials;
	
	public HashMap<Integer,List<Integer>> masked_dices;
	
	public boolean pdouble = false;
	boolean game_over = false;
	
	android.app.ProgressDialog ai_dialog;
	boolean cur_open;
	public int cur_j;
	public int open_dicer;
	
	player cur_player;
		
	CostumeDicer mainActivity;
	private Handler mHandler;
	
	// references to our playerName
    private String[] mCostumeNames;
	private String[] mCostumeMasters;
    
	public dicegame(CostumeDicer main){
		this.mainActivity = main;
		mCostumeNames = mainActivity.getResources().getStringArray(R.array.story_players);
		mCostumeMasters = mainActivity.getResources().getStringArray(R.array.master_players);
		
		r = new Random();
	}
	
	public void game_init(int comps, int lvl){
		this.num_computers = comps;
		this.lvl = lvl;
				
		gamecomputers = new player[num_computers];		
		game = new diceplay();
		me_player me = new me_player();
		game.myDices(me);
		loadMyProfile();
		
		int[] arrCostumes = new int[num_computers];
    	
		for (int i=0; i< num_computers; i++){
			switch (lvl){
			case 0: case 1:
				gamecomputers[i] = new player_lv1();
				break;
			case 2:
				gamecomputers[i] = new player_lv2();
				break;
			case 3:
				gamecomputers[i] = new player_lv3();
				break;
			case 4:
				gamecomputers[i] = new player_lv4();
				break;
			case 5:
				gamecomputers[i] = new player_lv5();
				break;
			case 6:
				gamecomputers[i] = new player_lv6();
				break;
			case 7:
				gamecomputers[i] = new player_lv7();
				break;
			case 8:
				gamecomputers[i] = new player_lv8();
				break;
			case 9:
				gamecomputers[i] = new player_lv9();
				break;
			
			}			
			arrCostumes[i] = selectCostume(i);
			gamecomputers[i].player_name = (arrCostumes[i]<9)?mCostumeNames[arrCostumes[i]]:mCostumeMasters[(arrCostumes[i]-9)];
	    	   
			game.playerDices(gamecomputers[i]);		
		}
		game_over = false;
		if (mainActivity.MODE == 1 || mainActivity.MODE == 2) {
			mainActivity.showStory(false);
		}
		mainActivity.init_Comp_Costumes(arrCostumes);
			
	}
	
	private void init_masked_dices(){
		masked_dices = new HashMap<Integer,List<Integer>>();
		for (int i=1; i<game.players.size(); i++){
			masked_dices.put(i, Arrays.asList(6, 6, 6, 6, 6));
		}
	}
	
	private void lucky(){
		//Toast.makeText(mainActivity, "Yeah! "+game.me.prob_lucky, Toast.LENGTH_SHORT).show();
    	if (game.me.lucky){
    		specials.GotLucky(mainActivity);	    		
    	}
	}
	
	public List<Integer> get_player_masked_dices(int pid){ return masked_dices.get(pid); }
	
	public void game_shake() {game.shake();init_masked_dices();lucky();}
	
	public String get_cur_playername(){return get_playername(cur_j);};
	public String get_playername(int i){return game.players.get(i).player_name;};
	
	public int get_curDiceNum() {if (game!=null) return game.currentDiceNum; else return 0;}
	public int get_curCalledTotal() {if (game!=null) return game.currentCalledTotal; else return 0;}
	public void set_curDiceNum(int curDiceNum) {if (game!=null) game.currentDiceNum=curDiceNum;}
	public void set_curCalledTotal(int curDCalledTotal) {if (game!=null) game.currentCalledTotal=curDCalledTotal;}
	public void set_call(int curDiceNum, int curDCalledTotal) {if (game!=null) game.call(curDiceNum, curDCalledTotal);}
	
	public int get_masterid(){
		int masterid = 9;
		for (int i=0; i<this.mCostumeMasters.length; i++){
			if (get_playername(1).equals(mCostumeMasters[i]))
				masterid +=i;
		}
		return masterid;
	}
	
	public int get_id(int pid){
		for (int i=0; i<this.mCostumeNames.length; i++){
			//if (get_playername(pid) == mCostumeNames[i])
			if (get_playername(pid).equals(mCostumeNames[i]))
				return i;
		}
		for (int i=0; i<this.mCostumeMasters.length; i++){
			//if (get_playername(pid) == mCostumeMasters[i])
			if (get_playername(pid).equals(mCostumeMasters[i]))
				return (9+i);
		}
		return 0;
	}
	
	public void loadMyProfile(){
		specials = new DicerSpecials();
		
		game.me.load_lucky(mainActivity.my_profile.myProfile.lucky);
		//game.me.load_lucky(300);
		game.me.load_exchange(mainActivity.my_profile.myProfile.exchange);
		//game.me.load_exchange(300);
		if (game.me.exp_exchange >= 100){
			specials.exchange = game.me.exp_exchange/100;
			specials.GotExchange(mainActivity);
		}
		game.me.load_peep(mainActivity.my_profile.myProfile.peep);
		//game.me.load_peep(200);
		if (game.me.exp_peep >= 100){
			specials.peep = game.me.exp_peep/100;
			specials.GotPeep(mainActivity);
		}
		
	}
	
	/**
     * the heart of the worker
     */
	public void run() {
		this.mHandler = new Handler()
		{
		    public void handleMessage(android.os.Message msg)
		    {
		        super.handleMessage(msg);		       
		        if (ai_dialog.isShowing()) ai_dialog.dismiss();
		        
		        switch (msg.what)
		        {
		        	/*Computer Opens*/
		            case 0:
		            	mainActivity.current_game_over = true;
		            	setOpenDicer(cur_j);
		            	mainActivity.CompOpen();
		            	break;
		            /*Next Player Calls*/
			        case 1:
			        	//mainActivity.set_PlayerCall();
			        	//mainActivity.showComputerDetails();
			        	if (cur_j == (game.players.size()-1))
			        		mainActivity.MyTurn();
			        	else {
			        		cur_j++;
			        		mainActivity.MyChallenge();
			        		//runComputer();
			        	}		
		            	    
		                break;
		        }
		        
		      							
		        
		    }
		};
		
		runComputer();
	}

	
	public void runComputer(){
		ai_dialog = android.app.ProgressDialog.show(mainActivity, "", 
				get_cur_playername()+" "+mainActivity.getResources().getText(R.string.strCompThinking), true);
		mainActivity.set_Costumes(cur_j);			        		
		new Thread(new Runnable()
        {
            @Override
            public void run()
            {
            	try {
            		cur_player = game.players.get(cur_j);
            		cur_open = (get_curDiceNum() == 0)?false:cur_player.open(game);
					//Thread.sleep(500);				
					if (cur_open){	
						//" OPEN!!!"
						if (mainActivity == null) return;
						mainActivity.runOnUiThread (new Runnable() { public void run() { ai_dialog.setMessage(get_cur_playername() + " " + mainActivity.getResources().getText(R.string.open)); } });
						Thread.sleep(2000);				
						
						if (mHandler != null)
							mHandler.sendEmptyMessage(0);													
					} else {
						if (mainActivity == null) return;
						cur_player.next_best_call(game);
						game.currentDicer = cur_j;
						
						/* SPECIAL */
						if (specials!=null){
							if ((lvl > 8) && (r.nextFloat()>0.5))
								if (specials.CompExchange(mainActivity));
									Thread.sleep(2000);
						}
						
						if (mainActivity == null) return;
						//mainActivity.runOnUiThread (new Runnable() { public void run() { ai_dialog.dismiss(); mainActivity.set_PlayerCall(); } });						
						mainActivity.runOnUiThread (new Runnable() { public void run() { if (ai_dialog != null) if (ai_dialog.isShowing()) ai_dialog.dismiss(); mainActivity.showComputerDetails(); } });
									        	
						Thread.sleep(2000);			
												
						if (mHandler != null)
							mHandler.sendEmptyMessage(1);
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        }).start();
		
	}
	
	
	public boolean Open(){
		boolean win = game.Open();
		game.punish(open_dicer, win, pdouble);
		return win;
	}		
	
	public int getCalledDicer(){return game.currentDicer;}
	public void setCalledDicer(int id){game.currentDicer = id;}
	public int getOpenDicer(){return open_dicer;}
	public void setOpenDicer(int id){open_dicer = id;}
	public int[] getDices(int i){return game.players.get(i).getdices().dice_numbers;}
	public int[] get_lives(){return game.getAllLives();}
	public int get_life(int i){return game.getlife(i);}
	public int get_totaldices() {return game.totaldices;}
	public boolean isGameOver() {return game.gameover();}
	public void reset(){
		game.reset();
		pdouble = false;
	}
	
	public static List<Integer> cupTodices(int[] cup){
    	List<Integer> arr = new ArrayList<Integer>();
    	for (int i=0; i<cup.length; i++){
    		if (cup[i]>0){
    			for (int j=0; j<cup[i]; j++){
    				arr.add(i);
    			}    				
    		}
    	}
    	return arr;
    }
	
	private int selectCostume(int p){
		int i=0;
		switch (mainActivity.MODE) {
			case 0:
				int numSelection = 4;
				if (mainActivity.my_profile.myProfile.completed > 0) numSelection = 9;
								
				i = r.nextInt(numSelection);
				boolean found_costume = false;
				while (!found_costume) {
					found_costume = true;
					for (int j=0; j<p; j++){
						if (gamecomputers[j].player_name == this.mCostumeNames[i]){
							found_costume = false;
							i = (i+1)%numSelection;
						}
					}
				}
				break;
			case 1:
				i = mainActivity.my_profile.myProfile.story;
				break;
			case 2:
				if (mainActivity.my_profile.myProfile.master3)
					i = (new Random()).nextInt(3) + 9;
				else if (mainActivity.my_profile.myProfile.master2)
					i = (new Random()).nextInt(2) + 9;
				else		
					i = 9;				
				mainActivity.my_profile.showToast("Master "+mCostumeMasters[i-9].toUpperCase()+" "+this.mainActivity.getResources().getText(R.string.master_challenge), StoryLineDialog.mThumbs[i], 0, Toast.LENGTH_LONG, true);
				break;				
		}		
		return i;
	}
	
	public void cleanup(){
		if (mHandler != null) mHandler = null;
		if (game != null) game = null;
		if (gamecomputers != null) gamecomputers = null;
		if (cur_player != null) cur_player = null;
		if (specials != null) specials = null;
		if (ai_dialog != null) 
			if (ai_dialog.isShowing()) ai_dialog.dismiss();
		if (mainActivity != null) mainActivity = null;
	}
		
}

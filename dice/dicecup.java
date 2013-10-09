package com.dice;

import java.util.Arrays;
import java.util.Random;

import android.util.Log;

public class dicecup {

	public int[] dice_numbers = new int[] { 0, 0, 0, 0, 0, 0 };
	
	public void generate(){
		for (int i=0; i<dice_numbers.length; i++)
			dice_numbers[i] = 0;
		Random randomGenerator = new Random();
	    for (int idx = 1; idx <= 5; ++idx){
	      int randomDice = randomGenerator.nextInt(6);
	      dice_numbers[randomDice]++;
	    }
	    //System.out.println(Arrays.toString(dice_numbers));	
	    Log.d("CostumeDicer", Arrays.toString(dice_numbers));
	}
	
	public void generate_lucky(){
		for (int i=0; i<dice_numbers.length; i++)
			dice_numbers[i] = 0;
		Random randomGenerator = new Random();
		int randomDice = randomGenerator.nextInt(6);
	    dice_numbers[randomDice]++;
	    for (int idx = 2; idx <= 5; ++idx){	    	
	      if (randomGenerator.nextDouble() > 0.50){
	    	  dice_numbers[randomDice]++;  
	      } else
	    	  dice_numbers[randomGenerator.nextInt(6)]++;
	    }
	}
	
	public void generate2(){
		for (int i=0; i<dice_numbers.length; i++)
			dice_numbers[i] = 0;
		Random random = new Random();
	    for (int idx = 1; idx <= 5; ++idx){
	    	int randomDice = getRandomInteger(1, 6, random);
	    	dice_numbers[randomDice-1]++;
	    }
	    //System.out.println(Arrays.toString(dice_numbers));
	}
	
	private int getRandomInteger(int aStart, int aEnd, Random aRandom){
	    if ( aStart > aEnd ) {
	      throw new IllegalArgumentException("Start cannot exceed End.");
	    }
	    //get the range, casting to long to avoid overflow problems
	    long range = (long)aEnd - (long)aStart + 1;
	    // compute a fraction of the range, 0 <= frac < range
	    long fraction = (long)(range * aRandom.nextDouble());
	    int randomNumber =  (int)(fraction + aStart);    
	    return randomNumber;
	}

	
	public int getNumDices(int dice){
		return dice_numbers[dice];
	}
	
}

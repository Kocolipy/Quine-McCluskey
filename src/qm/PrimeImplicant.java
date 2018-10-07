package qm;

import java.util.ArrayList;

public class PrimeImplicant{
	private int mSize;
	private ArrayList<Integer> mArray = new ArrayList<>();
	private boolean mChecked = false;
	
	public void check(){
		mChecked = true;
	}
	public boolean isChecked(){
		return mChecked;
	}
	public void add(int value){
		mArray.add(value);
	}
	public void set(int pos, int value){
		mArray.set(pos,value);
	}
	public int getValueAt(int pos){
		return mArray.get(pos); 
	}
	public void print(){
		System.out.println(mArray);
	}
	@Override
	public boolean equals(Object o){
		return mArray.equals(((PrimeImplicant) o).mArray);
	}
	public String toString(boolean MSB){
		String alphabets = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		String res = "";
		if (MSB){
			alphabets = new StringBuilder(alphabets.substring(0, mSize)).reverse().toString();
		}
		for(int i=mSize - 1; i >= 0; i--){
			if(getValueAt(i) == 1){
				res += alphabets.charAt(i) + ".";
			}
			else if (getValueAt(i) == 0){
				res +=  "~" + alphabets.charAt(i) + ".";
			}
		}
		return res.length()==0 ? "" : res.substring(0, res.length()-1);
	}
	public int numOnes(){
		int count = 0;
		for (int i =0 ; i< mSize; i++){
			if (getValueAt(i) == 1) count ++;
		}
		return count;
	}
	public boolean isGrayBitShiftOf(PrimeImplicant a){
		int diff = 0;
		assert (mSize == a.mSize);
		for (int i =0; i < mSize; i ++){
			if (getValueAt(i) != a.getValueAt(i)){
				if (diff == 1){
					return false;
				}
				diff ++;
			}
		}
		return (diff == 1);
	}
	public PrimeImplicant primeImplicantBuffer(PrimeImplicant a){
		assert(isGrayBitShiftOf(a));
		PrimeImplicant results = null;
		try {
			 results = new PrimeImplicant(0, mSize);
			 for (int i =0; i < mSize; i++){
					if (getValueAt(i) == a.getValueAt(i)){
						results.set(i, getValueAt(i));
					} else {
						results.set(i, 2);
					}
				}
		} catch (IncorrectSizeException e) {
			//will not happen since we are initializing the prime implicant to 0;
			e.printStackTrace();
		}
		return results;
	}
	public PrimeImplicant(int value, int size) throws IncorrectSizeException{
		if (Math.pow(2, size) <= value) throw new IncorrectSizeException("Size of Prime Implicant is too small");
		mSize = size;
		for (int i= 0; i < mSize; i ++){
			add(value%2);
			value = value/2;
		}
	}
	public ArrayList<Integer> generateCoverage(){
		ArrayList<Integer> results = new ArrayList<>();
		ArrayList<Integer> indexOf2 = new ArrayList<>();
		int count = 0;
		for (int i=0; i < mSize; i ++){
			if (getValueAt(i) == 2){
				indexOf2.add(i);
			} else {
				count += Math.pow(2, i)*getValueAt(i);
			}
		}
		results.add(count);
		for (int i: indexOf2){
			int size = results.size();
			for (int j = 0; j < size; j++){
				results.add((int) (results.get(j) + Math.pow(2, i)));
			}
		}
		return results;
	}
}

package qm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Qm {
	private int mSize;
	private ArrayList<Integer> mIntList;
	private ArrayList<Integer> mWildCardList;
	private boolean mMSB;
	private ArrayList<ArrayList<PrimeImplicant>> mSortingListA = new ArrayList<>();
	private ArrayList<ArrayList<PrimeImplicant>> mSortingListB = new ArrayList<>();
	private ArrayList<ArrayList<PrimeImplicant>> mSortingListC = new ArrayList<>();
	private ArrayList<ArrayList<PrimeImplicant>> mSortingListD = new ArrayList<>();
	private ArrayList<PrimeImplicant> mLeftovers = new ArrayList<>();
	
	private ArrayList<PrimeImplicant> generatePrimeImplicants(ArrayList<Integer> intList) throws IncorrectSizeException{
		ArrayList<PrimeImplicant> primeList = new ArrayList<>();
		for (int i: intList){
			primeList.add(new PrimeImplicant(i, mSize));
		}
		return primeList;
	}
	private void addToSortingList(ArrayList<ArrayList<PrimeImplicant>> sortingList, PrimeImplicant prime){
		int pos = prime.numOnes();
		while (sortingList.size() < pos + 1){
			sortingList.add(new ArrayList<PrimeImplicant>());
		}
		if (!sortingList.get(pos).contains(prime)) sortingList.get(pos).add(prime);
	}
	private void compare(ArrayList<ArrayList<PrimeImplicant>> source, ArrayList<ArrayList<PrimeImplicant>> target){
		if (source.size() == 0){
			return;
		}
		for (int i = 0; i < source.size()-1; i++){
			for (PrimeImplicant x :source.get(i)){
				for (PrimeImplicant y : source.get(i+1)){
					if (x.isGrayBitShiftOf(y)){
						PrimeImplicant buffer = x.primeImplicantBuffer(y);
						x.check();
						y.check();
						addToSortingList(target, buffer);
					}
				}
				if (!x.isChecked()){
					mLeftovers.add(x);
				}
			}
		}
		for (PrimeImplicant y: source.get(source.size()-1)){
			if (!y.isChecked()){
				mLeftovers.add(y);
			}
		}
	}
	private ArrayList<PrimeImplicant> removeEssentials(ArrayList<PrimeImplicant> initialList, ArrayList<Integer> minTerms){
		//Return a list of Essential Prime Implicants
		//modifies the initialList by removing the essential prime implicants
		ArrayList<PrimeImplicant> essentialList = new ArrayList<>();
		for (int i: minTerms){
			int count = 0;
			PrimeImplicant essential = null;
			for (PrimeImplicant prime : initialList){
				if (prime.generateCoverage().contains(i)){
					essential = prime;
					count ++;
				}
				if (count > 1){
					break;
				}
			}
			if (count == 1){
				essentialList.add(essential);
				initialList.remove(essential);
			}
		}
		return essentialList;
	}
	private void removeMinterms(PrimeImplicant prime, ArrayList<Integer> minterms){
		for (int i :prime.generateCoverage()){
			mIntList.remove(Integer.valueOf(i));
		}
	}
	public String calculate() throws IncorrectSizeException{
		ArrayList<PrimeImplicant> results = new ArrayList<>();
		//Adding Minterms and Dont Care to List A
		for (PrimeImplicant prime : generatePrimeImplicants(mIntList)){
			addToSortingList(mSortingListA, prime);
		}
		for (PrimeImplicant prime : generatePrimeImplicants(mWildCardList)){
			addToSortingList(mSortingListA, prime);
		}
		
		//Getting List B
		compare(mSortingListA, mSortingListB);
		
		//Getting List C
		compare(mSortingListB, mSortingListC);
		
		//Getting List D
		compare(mSortingListC, mSortingListD);

		//Getting all terms which are prime implicants
		for (ArrayList<PrimeImplicant> lst: mSortingListD){
			for (PrimeImplicant prime: lst){
				mLeftovers.add(prime);
			}
		}
		
		//Getting the Essential Prime Implicants and removing the minterms covered by them
		while (mIntList.size() != 0){
			//remove all essential prime implicants
			ArrayList<PrimeImplicant> essentialList = removeEssentials(mLeftovers, mIntList);
			for (PrimeImplicant essential : essentialList){
				results.add(essential);
				removeMinterms(essential, mIntList);
			}
			//no more essential prime implicants, i.e. all remaining minterms are covered by 1 or more prime implicants
			if (essentialList.size() == 0){
				int biggest = 0;
				PrimeImplicant biggestPrime = null;
				for (PrimeImplicant prime : mLeftovers){
					int count = 0;
					ArrayList<Integer> coverage = prime.generateCoverage();
					for (int i: mIntList){
						if (coverage.contains(Integer.valueOf(i))) count++;
					}
					if (count > biggest){
						biggest = count;
						biggestPrime = prime;
					}
				}
				results.add(biggestPrime);
				removeMinterms(biggestPrime, mIntList);
			}
		}
		return getSolution(results);
	}
	private String getSolution(ArrayList<PrimeImplicant> results){
		String res = "";
		for (PrimeImplicant prime: results){
			res += prime.toString(mMSB) + " + ";
		}
		if (res.equals(" + ")){
			return "The Essential Prime Implicant is the entire solution space.";
		} else {
			return res.substring(0, res.length()-3);
		}
	}
	public Qm(int size, ArrayList<Integer> intList, ArrayList<Integer> wildCardList, boolean MSB){
		mSize = size;
		mIntList = intList;
		mWildCardList = wildCardList;
		mMSB = MSB;
	}
	
	public static void main(String[] args){
		Qm a = new Qm(4, new ArrayList<>(Arrays.asList(5,6,7,8)), new ArrayList<>(Arrays.asList()), true);
		try {
			a.calculate();
		} catch (IncorrectSizeException e) {
			e.printStackTrace();
		}
	}
}

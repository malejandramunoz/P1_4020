package classes;

/**
 * Main class where the election takes place. Where the class processes the file and outputs the results in a new text file by the name of results.txt
 * @author Maria A. Munoz
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import structures.ArrayList;
import structures.LinkedList;
import structures.DynamicSet;

@SuppressWarnings("rawtypes")
public class Election {
	
	/**
	 * Declaration of all the variables used inside the class
	 */
	public ArrayList<Ballot> ballots = new ArrayList<Ballot>(10);
	public LinkedList<String[]> candidatesID;
	public ArrayList<DynamicSet> rankedList;
	public boolean winnerSelected = false;
	public int rounds = 0;
	public DynamicSet<Integer> tobeEliminated;
	public DynamicSet<Integer> eliminatedCandidates;
	public int blankBallots = 0;
	public int invalidBallots = 0;
	public int eliminatedCandidate;



	/**
	 * Constructor where it receives the two strings where the files are located
	 * First try and catch read the candidates and stored how many would there be. It also saves 
	 * each candidateID with its respective candidate name in a LinkedList of String Arrays.
	 * Second try and catch read the ballots and put them inside of an arraylist of ballots.
	  
	 * @param candidatesFile - Where the candidates names and ID's have been stored.
	 * @param ballotsFile - Where the ballots casted have been stored.
	 */
	public Election(String candidatesFile, String ballotsFile) {
		
		try {
			File candidatesCount = new File(candidatesFile); 
			Scanner reader = new Scanner(candidatesCount);
			candidatesID = new LinkedList<String[]>();
			while(reader.hasNext()) {
				String candidates = reader.nextLine();
				String[] candidateID = candidates.split(",");
				candidatesID.add(candidateID);
			}
			reader.close();

		} catch(FileNotFoundException e){
			System.out.println("An error ocurred");
			e.printStackTrace();
		}
		
		
		try {
			File votingBooth = new File(ballotsFile); 
			Scanner reader = new Scanner(votingBooth);
			while(reader.hasNext()) {
				String ballot = reader.nextLine();
				ballots.add(new Ballot(ballot));
			}
			reader.close();	

			eliminatedCandidates = new DynamicSet<Integer>(candidatesID.size());
		} catch(FileNotFoundException e) {
			System.out.println("An error ocurred");
			e.printStackTrace();
		}
		
	}

	
	/**
	 * Method that initiates the ranking array so that it wouldn't be an empty list when the amountVotes Method began to add the rankedList of the 
	 * candidates. Put inside a method as to not repeat code every time it was needed.
	 */
	public void rankingInicialiciation() {
		rankedList = new ArrayList<DynamicSet>(candidatesID.size()); 
		for (int i = 0; i < candidatesID.size(); i++) {
			DynamicSet temp = new DynamicSet(3);
			rankedList.add(i, temp);
		}
	}
	
	
	/**
	 * Method that it would count what amount of votes that each candidate has. Once the eliminated candidates have one, it checks that an already 
	 * eliminated candidate doesn't get votes again.It returns an ArrayList of DynamicSets where it stores the ballot number 
	 * of each vote where the rank matches the @param rankCheck. There are two for loops as to make sure that none of the ballots are miscounted and
	 * or eliminated when they weren't supposed to be.
	  
	 * @param ballotList - ArrayList with the ballots from the file given.
	 * @param rankCheck - Rank to be checked and counted votes for. The lower the better.
	 * @param eliminatedCandidates - DynamicSet with the candidatesID that have already been eliminated in a past round
	 * @return ArrayList with the ballot numbers where the candidate has been given a rank of the RankCheck. To identify which candidate it is, it's index
	 * is candidateID - 1.
	 */
	@SuppressWarnings({ "unchecked" })
	public ArrayList<DynamicSet> amountVotes(ArrayList<Ballot> ballotList, int rankCheck, DynamicSet<Integer> eliminatedCandidates) {
		rankingInicialiciation();
		if(eliminatedCandidates.size() > 0) {
			for (Integer candidate : eliminatedCandidates) {
				for (Ballot ballot : ballotList) {
					int checking = ballot.getCandidateByRank(rankCheck);
					if(checking == candidate) {
						ballot.eliminate(candidate);
					}
				}
			}
		}
		for (Ballot ballot : ballotList) {
			if(ballot.getCandidateByRank(rankCheck) > -1) {
				DynamicSet temp = rankedList.get(ballot.getCandidateByRank(rankCheck) - 1);
				temp.add(ballot.ballotNumber);
				rankedList.replaceAll(rankedList.get(ballot.getCandidateByRank(rankCheck) - 1), temp);
			}
		}

		return rankedList;
	}
	

	/**
	 * Method that counts the least amount of votes and returns the ID of the candidates that have the least amount of votes with the rank used in 
	 * the past check. It also saves the candidate that was eliminated so that it can be accessed later if needed.
	  
	 * @param rankedList - ArrayList with the ballot numbers where the candidate has been given a rank of the RankCheck. To identify which candidate it is, it's index
	 * is candidateID - 1.
	 * @return DynamicSet of Integers with the candidates that are close to being eliminated from the race.
	 */
	public DynamicSet<Integer> checkLeast(ArrayList<DynamicSet> rankedList){
		tobeEliminated = new DynamicSet<>(3);
		int minAmmount = rankedList.get(0).size();
		for (int i = 1; i < rankedList.size(); i++) {
			if(rankedList.get(i).size() == 0) {
				continue;
			}
			if(rankedList.get(i).size() <= minAmmount) {
				minAmmount = rankedList.get(i).size();
				tobeEliminated.add(i + 1);
			}
		}
		if(rankedList.get(0).size() <= minAmmount) {
			tobeEliminated.add(1);
		}
		return tobeEliminated;
	}
	/**
	 * Method to break the tie. It enters the whole ballot list & the candidates for elimination in  so that it can re-count the votes with a new rank 
	 * to check. 
	  
	 * @param ballotList - ArrayList of the ballots from the file given.
	 * @param rankCheck - Rank to be checked and counted votes for. The lower the better.
	 * @param toBeEliminated - DynamicSet of Integers with the candidates that are close to being eliminated from the race.
	 * @return LinkedList of LinkedList of Integers that have the candidateID with the amount of votes accumulated with the rankCheck.
	 */
	public LinkedList<LinkedList<Integer>> tieBreaker(ArrayList<Ballot> ballotList, int rankCheck, 
			DynamicSet<Integer> toBeEliminated){
		LinkedList<LinkedList<Integer>> amountVotes = new LinkedList<LinkedList<Integer>>();
		int count = 0;
		for(Integer candidate: toBeEliminated) {
			for(Ballot ballots: ballotList) {
				if(ballots.getCandidateByRank(rankCheck) == candidate) {
					count++;
				}
			}
			LinkedList<Integer> temp = new LinkedList<Integer>();
			temp.add(candidate);
			temp.add(count);
			amountVotes.add(temp);
			count = 0;
		}
		return amountVotes;
	}
	/**
	 * Method that eliminates the candidate that has been declared the loser of that round. It also added the candidate ID to the already 
	 * eliminated DynamicSet so it can keep track of the amount of candidates eliminated.
	 
	 * @param toBeEliminated - DynamicSet of Integers with the candidates that are close to being eliminated from the race.
	 * @param ballotList - ArrayList of the ballots from the file given.
	 * @return DynamicSet of Integers that have already been eliminated from the race.
	 */
	public DynamicSet<Integer> eliminateCandidate(DynamicSet<Integer> toBeEliminated, ArrayList<Ballot> ballotList) {

		for (Integer integer : toBeEliminated) {
			eliminatedCandidate = integer;
		}
		for (Ballot ballot: ballotList) {
			if(ballot.getRankByCandidate(eliminatedCandidate) == 1) {
				ballot.eliminate(eliminatedCandidate);
			}
		}
		eliminatedCandidates.add(eliminatedCandidate);
		return eliminatedCandidates;
	}
	/**
	 * Checks the initial condition given with the ballots and the current rankings when called. Counts the amount of ballots casted divides that by two
	 * and then checks that value against the actual sizes of the votes that each candidate has been given. If this value is right, it returns true and 
	 * ends the loop in the Main method.
	 
	 * @param ballotList - ArrayList of the ballots from the file given.
	 * @param rankedList - ArrayList of the ballot numbers where the candidate has been given a rank of the RankCheck. To identify which candidate it is, it's index
	 * is candidateID - 1.
	 * @return Boolean that returns true if a winner has been found, false if a winner hasn't been found.
	 */
	public boolean halfVotes(ArrayList<Ballot> ballotList, ArrayList<DynamicSet> rankedList) {
		int totalVotes = ballotList.size(); 
		totalVotes /= 2;
		for (DynamicSet dynamicSet : rankedList) {
			if(dynamicSet.size() >= totalVotes) {
				winnerSelected = true;
			}
		}
		return winnerSelected;
	}

	
	/**
	 * Method to check the validity of each ballot entered. Will delete if either the ballot is invalid(the voter put two different candidates in the same
	 * rank or same candidate with different rank) or if the ballot was blank upon entering the voting booth.
	 */
	public void checkValidity() {
		int i;
		for(i = 0; i < ballots.size(); i++) {
			Ballot toBeChecked = ballots.get(i);
			if(toBeChecked.invalidBallot(toBeChecked)) {
				invalidBallots++;
				ballots.remove(i);
				i--;
			} else if(toBeChecked.blankBallot(toBeChecked)) {
				blankBallots++;
				ballots.remove(i);
				i--;
			}
		}
	}
	
	
	/**
	 * Method to get the name of the candidate from the candidateID, this will just make it easier to print once a candidate has been eliminated or when
	 * a winner is finally chosen.
	 * 
	 * @param inputList - LinkedList of a String array filled with the candidates name and ID with the index candidateID - 1.
	 * @param candidateID - Integer that represents one of the candidates in the race.
	 * @return String with the name of the candidate represented by the candidateID
	 */
	public String getNameByCandidate(LinkedList<String[]> inputList, int candidateID) {
		String name = "";
		for (String[] stringArray : inputList) {
			for (int i = 0; i < stringArray.length; i++) {
				int compare = Integer.parseInt(stringArray[1]);
				if(compare == candidateID) {
					name = stringArray[0];
				}
			}
		}
		return name;
	}
	
	
	/**
	 * Method to correctly count the votes that each candidate got after each round. Easier like this to make sure it's not accessing the incorrect 
	 * candidate, adding whenever it's not supposed to or using a ranking list after it's been updated.
	 * 
	 * @param rankedList
	 * @param candidateID - Integer that represents one of the candidates in the race.
	 * @return Amount of votes that the candidate has gotten in that round.
	 */
	public int countVotes(ArrayList<DynamicSet> rankedList, int candidateID) {
		int amount = rankedList.get(candidateID - 1).size();
		return amount;
	}

	/**
	 * Main where the output file is created. First the files is created with the name(which can be changed easily), secondly, the object of Election to be 
	 * used is created. From there, the variables that will be used are created with their newElection counterpart (this is done for convenience as to not 
	 * called newElection every time there is a need to access any of the variables inside the class). Now, the output file begins to take form. Printing 
	 * the first things that have already been taken from file. 

	 * With that printed inside the file, the true Election begins. With the rankedList (ArrayList of DynamicSets where each DynamicSet holds the amount
	 * of votes casted with the rank chosen) already initialized, we begin checking is there's any about to be eliminated with the Method of checkLeast. If
	 * there is one more than candidate in the toBeEliminated, then it begins the tieBreaking process with a new rankCheck. After the loser has been picked
	 * the amount of rounds is updated,the output file is updated with the name, round and amount of votes that the candidate lost with, the candidate is
	 * eliminated from the ballot inside of ballotList where it is pertinent(the ballots where the candidate was voted with the rank chosen), and rankedList 
	 * is updated with that updated ballotList. If the toBeEliminated size is only one (that means there's only candidate about to be eliminated), then it
	 * does the same process as before, updated the ranks, the ballotList, and the output file. Before continuing, it will always check if halfVotes is true,
	 * if it's false, it will continue the process until this is true. Once it is true, it will uptade the output file with the winner's name, and the amount
	 * of votes that the candidate amounted to win.
	 */
	public static void main(String[] args) {
		try {
			FileWriter results = new FileWriter("result.txt");
			int rounds = 0;
			Election newElection = new Election("candidates.csv", "ballots.csv");
			ArrayList<Ballot> ballotList = newElection.ballots;
			newElection.checkValidity();
			LinkedList<String[]> candidateList = newElection.candidatesID;
			DynamicSet<Integer> eliminatedCandidates = new DynamicSet<Integer>(candidateList.size());
			ArrayList<DynamicSet> rankedList = newElection.amountVotes(ballotList, 1, eliminatedCandidates);
			DynamicSet<Integer> toBeEliminated;
			LinkedList<LinkedList<Integer>> amountVotes = new LinkedList<LinkedList<Integer>>();
			results.write("Number of ballots: " + ballotList.size() + "\n");
			results.write("Number of blank ballots: " + newElection.blankBallots + "\n");
			results.write("Number of invalid ballots: " + newElection.invalidBallots + "\n");

			while(!newElection.halfVotes(ballotList, rankedList)) {
				toBeEliminated = newElection.checkLeast(rankedList);
				
				if(toBeEliminated.size() > 1) {
					amountVotes = newElection.tieBreaker(ballotList, 2, toBeEliminated);
					toBeEliminated.clear();
					int min = amountVotes.get(0).get(1);
					for (LinkedList<Integer> linkedList : amountVotes) {
						if(min > linkedList.get(1)) {
							toBeEliminated.add(linkedList.get(0));
							min = linkedList.get(1);
						}
					}
					
					if(amountVotes.get(0).get(1) == min) {
						toBeEliminated.add(amountVotes.get(0).get(0));
					}
					
					rounds++;
					eliminatedCandidates = newElection.eliminateCandidate(toBeEliminated, ballotList);
					int ones = newElection.countVotes(rankedList, newElection.eliminatedCandidate);
					results.write("Round " + rounds + ": " + newElection.getNameByCandidate(candidateList, newElection.eliminatedCandidate) + " was eliminated with " 
					+ ones + " #1's\n");
					rankedList = newElection.amountVotes(ballotList, 1, eliminatedCandidates);
					
				} else {
					rounds++;
					eliminatedCandidates = newElection.eliminateCandidate(toBeEliminated, ballotList);
					int ones = newElection.countVotes(rankedList, newElection.eliminatedCandidate);
					results.write("Round " + rounds + ": " + newElection.getNameByCandidate(candidateList, newElection.eliminatedCandidate) + " was eliminated with " 
							+ ones + " #1's\n");
					rankedList = newElection.amountVotes(ballotList, 1, eliminatedCandidates);
				}
			}
			
			int maxAmount = rankedList.get(0).size();
			int winner = 0;
			for (int i = 0; i < rankedList.size(); i++) {
				if(rankedList.get(i).size() > maxAmount) {
					maxAmount = rankedList.get(i).size();
					winner = i + 1;
				}
			}
			
			int ones = newElection.countVotes(rankedList, winner);
			results.write("Winner: " + newElection.getNameByCandidate(candidateList, winner) + " wins with "
					+ ones + " #1's");
			results.close();
		} catch (IOException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		    }
	}
	
}
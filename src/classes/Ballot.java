package classes;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.Scanner;

import structures.ArrayList;
import structures.LinkedList;
import structures.List;
import structures.DynamicSet;

/**
 * In this class, this is the constructor for everything ballot. For everything that has to do
 * with the actual ballots is here.
 */

@SuppressWarnings("unused")
public class Ballot implements BallotInterface {

	/**
	 * It was decided to add the final integer of CandidateID and Rank as to make sure they would
	 * never get mixed up whenever coding.
	 */
	private String candidateID;
	private String rank;
	private String[] separatedBallots;
	public String ballotNumber;
	public int size = 0;
	private LinkedList<LinkedList<String>> listofVotes;
	public final int CandidateID = 0;
	public final int Rank = 1;


	/**
	 * With this, the program constructs the Ballot and returns a LinkedList of LinkedList with
	 * each rank and each vote that was entered in the input file read in the Election class.
	 * @param ballotEntered
	 */
	public Ballot(String ballotEntered) {

		listofVotes = new LinkedList<LinkedList<String>>();

		separatedBallots = ballotEntered.split(",");


		ballotNumber = ballotEntered.split(",")[0];

		for (int i = 1; i < separatedBallots.length; i++) {
			String[] votes = separatedBallots[i].split(":");
			this.candidateID = votes[CandidateID];
			this.rank = votes[Rank];
			LinkedList<String> setVote = new LinkedList<String>();
			setVote.add(candidateID);
			setVote.add(rank);
			listofVotes.add(setVote);

		}

		size = separatedBallots.length - 1;
		
	}

	

	/**
	 * Returns the ballot number associated with the votes cast.
	 * @return Ballot number associated with the votes cast
	 */
	@Override
	public int getBallotNum() {
		return Integer.parseInt(ballotNumber);
	}
	

	/**
	 * Returns the rank that the voter cast on the candidate.
	 * @param candidateID - Integer that represents one of the candidates in the race.
	 * @return Integer with the rank associated with the candidateID.
	 */
	@Override
	public int getRankByCandidate(int candidateID) {
		for (LinkedList<String> votes : listofVotes) {
			int idFromList = Integer.parseInt( votes.get(CandidateID));
			if(idFromList == candidateID) {
				return Integer.parseInt(votes.get(Rank));
			}
		}
		return -1;
	}
	
	
	/**
	 * Returns the candidate that the voter cast based on the rank.
	 * @param rank - Integer given by the voter. Lower is better. 
	 * @return Integer with the rank associated with the candidateID.
	 */
	@Override
	public int getCandidateByRank(int rank) {
		for (LinkedList<String> votes : listofVotes) {
			int rankFromList = Integer.parseInt(votes.get(Rank));
			if(rankFromList == rank) {
				return Integer.parseInt(votes.get(CandidateID));
			}
		}
		return -1;
	}
	
	
	/**
	 * Eliminates a given candidate from the ballot linked and updates the other ranks
	 * @param candidateID - Integer that represents one of the candidates in the race.
	 * @return Boolean that returns true if it was able to delete the candidate, false if it was unable to.
	 */
	@Override
	public boolean eliminate(int candidateID) {
		int i = 1;
		boolean check = false;
		for (LinkedList<String> votes : listofVotes) {
			int idFromList = Integer.parseInt(votes.get(CandidateID));
			if(idFromList == candidateID) {
				listofVotes.removeObj(votes);
				size--;
				check = true;
			}
			int updatedRank = Integer.parseInt(votes.get(Rank)) - 1;
			votes.set(i, String.valueOf(updatedRank));
		}
		return check;
	}
	
	
	/**
	 * Returns the size of the array and therefore the amount of votes casted.
	 * @return Integer with the amount of votes casted in this ballot.
	 */

	public int getSize() {
		return this.size;
	}
	
	
	/**
	 * Returns if the ballot is valid. Checking if there are any same candidatesID with different ranks 
	 * or ranks given to two different candidates.
	 * @param ballot - Ballot to be checked if it's valid or not.
	 * @return Boolean that returns true if the ballot is invalid, false if it's valid
	 */
	@Override
	public boolean invalidBallot(Ballot ballot) {
		for (int i = 0; i < listofVotes.size(); i++) {
			for (int j = i + 1; j < listofVotes.size(); j++) {
				String ic = listofVotes.get(i).get(CandidateID);
				String jc = listofVotes.get(j).get(CandidateID);
				if(ic.equals(jc)) {
					return true;
				}
				ic = listofVotes.get(i).get(Rank);
				jc = listofVotes.get(j).get(Rank);
				if(ic.equals(jc)) {
					return true;
					
				}
			}
		}

		return false;
	}


	/**
	 * Returns if it is a blank Ballot or not. All it needs to check if the size is zero
	 * as that means that the only thing inside the line at that point was the ballot number.
	 * @param ballot - Ballot to be checked if it's blank or not.
	 * @return Boolean that returns true if the ballot was blank, false if it was true.
	 */
	@Override
	public boolean blankBallot(Ballot ballot) {
		if(this.getSize() == 0) {
			return true;
		}
		return false;
	}

}

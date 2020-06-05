package classes;

import structures.LinkedList;

/**
 * Interface to make sure the program was meeting the requirements asked for in the specifications.
 * Each one has a simple definition of what it was supposed to do.
 * @author Maria A. Munoz
 *
 */

public interface BallotInterface {
	public int getBallotNum(); // returns the ballot number
	 public int getRankByCandidate(int candidateID); // rank for that candidate
	 public int getCandidateByRank(int rank); // candidate with that rank
	 public boolean eliminate(int candidateID); // eliminates the candidate from the ballot in the rank designated inside
	 public boolean invalidBallot(Ballot ballot); // checks if the ballot is valid
	 public boolean blankBallot(Ballot ballot); // check if the ballot is blank
}

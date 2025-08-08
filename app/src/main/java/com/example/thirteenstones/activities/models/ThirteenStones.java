package com.example.thirteenstones.activities.models;

import com.google.gson.Gson;

import java.util.Arrays;

public class ThirteenStones
{
    private final static int sDEFAULT_PILE_START = 13, sPLAYER_COUNT = 2;
    private int mNumberOfGamesPlayed = 0;
    private final int[] mArrayPlayerWinCount;

    public final static int sMIN_PICK = 1;
    public final static int sMAX_PICK = 3;

    private final int mPileStart;
    private int mPileCurrent;
    private boolean mFirstPlayerTurn, mWinnerIsLastPlayerToPick; //false by default

    public ThirteenStones ()
    {
        this (sDEFAULT_PILE_START, false);
    }

    public ThirteenStones (int pileSize)
    {
        this (pileSize, false);
    }

    public ThirteenStones (int pileSize, boolean winnerIsLastPlayerToPick)
    {
        mPileStart = pileSize;
        mWinnerIsLastPlayerToPick = winnerIsLastPlayerToPick;
        mArrayPlayerWinCount = new int[sPLAYER_COUNT];
        startGame ();
    }

    public void startGame ()
    {
        mPileCurrent = mPileStart;
        mFirstPlayerTurn = true;
        mNumberOfGamesPlayed++;
    }

    public void takeTurn (int amount)
    {
        if (!isGameOver ()) {
            tryToTakeTurnWith (amount);
            updateGameWinStatisticsIfGameHasJustEnded();
        }
        else {
            throw new IllegalStateException ("May not take a turn while the game is over.");
        }
    }

    private void updateGameWinStatisticsIfGameHasJustEnded() {
        if (isGameOver())
            mArrayPlayerWinCount[getWinningPlayerNumber()-1]++;  // player 1 or 2 == element 0 or 1
    }

    private void tryToTakeTurnWith (int amount)
    {
        if (isInMinToMaxRange (amount) && isNotGreaterThanPileCurrent (amount)) {
            takeValidTurn (amount);
        }
        else {
            throw new IllegalArgumentException
                    ("Pick Amount must be: " + sMIN_PICK + " - " + sMAX_PICK +
                            " and up to number of remaining stones in the pile.");
        }
    }

    private boolean isNotGreaterThanPileCurrent (int amount)
    {
        return amount <= mPileCurrent;
    }

    private boolean isInMinToMaxRange (int amount)
    {
        return amount >= sMIN_PICK && amount <= sMAX_PICK;
    }

    private void takeValidTurn (int amount)
    {
        // decrement pile
        mPileCurrent -= amount;

        // switch player turns if not Game Over
        if (!isGameOver ())
            mFirstPlayerTurn = !mFirstPlayerTurn;
    }

    public boolean isGameOver ()
    {
        return mPileCurrent <= 0;
    }

    public boolean isWinnerIsLastPlayerToPick ()
    {
        return mWinnerIsLastPlayerToPick;
    }

    public int getNumberOfWinsForPlayer (int playerNumber)
    {
        if (playerNumber < 1 || playerNumber > sPLAYER_COUNT)
            throw new IllegalArgumentException("Player number must be between 1 and "
                    + sPLAYER_COUNT + ".");
        return mArrayPlayerWinCount[playerNumber-1];
    }

    public void resetStatistics ()
    {
        mNumberOfGamesPlayed= isGameOver() ? 0 : 1;
        Arrays.fill(mArrayPlayerWinCount, 0);
    }

    public void setWinnerIsLastPlayerToPick (boolean winnerIsLastPlayerToPick)
    {
        mWinnerIsLastPlayerToPick = winnerIsLastPlayerToPick;
    }

    public int getWinningPlayerNumberIfGameOver()
    {
        if (!isGameOver())
            throw new IllegalStateException("No winner yet; the game is still ongoing.");
        return getWinningPlayerNumber();
    }

    private int getWinningPlayerNumber() {
        return (mWinnerIsLastPlayerToPick == mFirstPlayerTurn) ? 1 : 2;
    }

    public int getCurrentPlayerNumber ()
    {
        return mFirstPlayerTurn ? 1 : 2;
    }

    public int getNumberOfGamesPlayed ()
    {
        return mNumberOfGamesPlayed;
    }

    public int getStonesRemaining ()
    {
        return mPileCurrent;
    }

    public String getRules ()
    {
        return "13 Stones is a simple game. Game play begins with a pile of " +
                mPileStart + " stones.\n\n" +
                "Players each take one turn per round removing " +
                sMIN_PICK + "-" + sMAX_PICK + " stones per turn.\n\n" +
                "By default, the player that empties the pile loses the game.";
    }

    /**
     * Reverses the game object's serialization as a String
     * back to a ThirteenStones game object
     *
     * @param json The serialized String of the game object
     * @return The game object
     */
    public static ThirteenStones getGameFromJSON (String json)
    {
        Gson gson = new Gson ();
        return gson.fromJson (json, ThirteenStones.class);
    }

    /**
     * Serializes the game object to a JSON-formatted String
     *
     * @param obj Game Object to serialize
     * @return JSON-formatted String
     */
    public static String getJSONFromGame (ThirteenStones obj)
    {
        Gson gson = new Gson ();
        return gson.toJson (obj);
    }

    public String getJSONFromCurrentGame()
    {
        return getJSONFromGame(this);
    }
}
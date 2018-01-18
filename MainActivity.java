package com.example.rachelxu.scarnesdice;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private static final int NUM_IMAGES = 6;
    private static final int NUM_COMPUTER_TURN = 4;
    private static final int TARGET_SCORE = 30;
    private static final int DEFAULT_IMAGE = R.drawable.dice1;
    private int userTotal = 0;
    private int userCurrent = 0;
    private int computerTotal = 0;
    private int computerCurrent = 0;
    private Integer[] images = {R.drawable.dice1, R.drawable.dice2, R.drawable.dice3,
            R.drawable.dice4, R.drawable.dice5, R.drawable.dice6};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        clearScores();
        rollDiceOnClick();
        resetScoreOnClick();
        holdOnClick();
    }

    private void computerTurn() {
        final Button holdButton = findViewById(R.id.hold_button);
        final Button rollButton = findViewById(R.id.roll_button);
        final TextView computerHoldView = findViewById(R.id.computer_hold_text);
        final TextView winnerView = findViewById(R.id.winner);

        final int oldComputerTotal = computerTotal;
        computerCurrent = 0;

        holdButton.setEnabled(false);
        rollButton.setEnabled(false);

        Handler handler = new Handler();
        for (int i = 0; i < NUM_COMPUTER_TURN; i++) {
            final int index = i;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    int dice = rollAndDisplay() + 1;
                    if (dice == 1) {
                        computerCurrent = 0;
                        computerTotal = oldComputerTotal;
                    } else {
                        computerTotal += dice;
                        computerCurrent += dice;
                    }
                    setScore(R.id.computer_turn_score, computerCurrent);
//                    setScores(userTotal, userCurrent, computerTotal, computerCurrent);
                    final Handler computerHoldTextHandler = new Handler();
                    if (index == NUM_COMPUTER_TURN - 1) {
                        computerHoldView.setText("Computer holds");
                        computerHoldTextHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                computerHoldView.setText("");
                                holdButton.setEnabled(true);
                                rollButton.setEnabled(true);
                                setScore(R.id.computer_total_score, computerTotal);
                                if (computerTotal >= TARGET_SCORE) {
                                    winnerView.setText("Computer wins!");
                                    computerHoldTextHandler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            resetGame();
                                        }
                                    }, 1000);
                                }
                            }
                        }, 1000);
                    } else if (dice == 1) {
                        computerHoldView.setText("Computer rolled a one");
                        computerHoldTextHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                computerHoldView.setText("");
                            }
                        }, 1000);
                    }
                }
            }, (i + 1) * 1000);
        }
    }

    private void resetGame() {
        final TextView winnerView = findViewById(R.id.winner);
        ImageView dice = findViewById(R.id.imageView);

        winnerView.setText("");
        clearScores();
        dice.setImageResource(DEFAULT_IMAGE);
    }

    private void resetScoreOnClick() {
        Button resetButton = findViewById(R.id.reset_button);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetGame();
            }
        });
    }

    private void holdOnClick() {
        Button holdButton = findViewById(R.id.hold_button);

        holdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userCurrent = 0;
                setScores(userTotal, userCurrent, computerTotal, computerCurrent);
//                setScore(R.id.user_turn_score, userCurrent);
                computerTurn();
            }
        });
    }

    private int rollAndDisplay() {
        int rand = (int) (Math.random() * NUM_IMAGES);
        ImageView newDice = findViewById(R.id.imageView);
        newDice.setImageResource(images[rand]);
        return rand;
    }

    private void rollDiceOnClick() {
        final Button rollButton = findViewById(R.id.roll_button);
        final Handler computerTurnHandler = new Handler();
        final Handler winnerHandler = new Handler();
        final int oldUserTotal = userTotal;

        rollButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                setScores(userTotal, userCurrent, computerTotal, computerCurrent);
                int diceScore = rollAndDisplay() + 1;
                if (diceScore == 1) {
                    userCurrent = 0;
                    userTotal = oldUserTotal;
                    computerTurnHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            computerTurn();
                        }
                    }, 500);
                } else {
                    userTotal += diceScore;
                    userCurrent += diceScore;
                }
                setScore(R.id.user_turn_score, userCurrent);
                if (userTotal >= TARGET_SCORE) {
                    Log.d("current user score:", String.valueOf(userTotal));
                    final TextView winnerView = findViewById(R.id.winner);
                    setScore(R.id.user_total_score, userTotal);
                    winnerView.setText("You win!");
                    winnerHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            resetGame();
                        }
                    }, 1000);
                }
            }
        });
    }

    private void setScore(int id, int newScore) {
        TextView textView = findViewById(id);
        String text = "";
        switch (id) {
            case R.id.user_total_score:
                text = "Your Score: ";
                break;
            case R.id.computer_total_score:
                text = "Computer Score: ";
                break;
            case R.id.user_turn_score:
                text = "Your Turn Score: ";
                break;
            case R.id.computer_turn_score:
                text = "Computer Turn Score: ";
                break;
        }
        textView.setText(text + newScore);
    }

    private void setScores(int userTot, int userCur, int computerTot, int computerCur) {
        setScore(R.id.user_total_score, userTot); // user total score
        setScore(R.id.user_turn_score, userCur); // user current score
        setScore(R.id.computer_total_score, computerTot); // computer total score
        setScore(R.id.computer_turn_score, computerCur);
    }

    private void clearScores() {
        userTotal = 0;
        userCurrent = 0;
        computerTotal = 0;
        computerCurrent = 0;
        setScores(userTotal, userCurrent, computerTotal, computerCurrent);
    }
}

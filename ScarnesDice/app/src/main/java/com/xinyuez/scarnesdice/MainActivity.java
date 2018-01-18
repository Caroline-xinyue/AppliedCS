package com.xinyuez.scarnesdice;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.Random;

public class MainActivity extends AppCompatActivity
{
    private static final int DICE_FACES = 6;
    private static final int COMPUTER_TURNS = 4;
    private static final int DEFAULT_DICE_FACE = R.drawable.dice1;
    TextView userTotScoreTxt;
    TextView compTotScoreTxt;
    TextView turnScoreTxt;
    TextView statusTxt;
    ImageView diceImg;
    int[] diceFaces = {R.drawable.dice1, R.drawable.dice2, R.drawable.dice3,
                       R.drawable.dice4, R.drawable.dice5, R.drawable.dice6};
    Button rollBtn;
    Button holdBtn;
    Button resetBtn;

    public enum Turn {
        PLAYER,
        COMPUTER
    };

    /*
      the user's overall score state
      the user's turn score
      the computer's overall score
      the computer's turn score
    */
    int userTotScore;
    int userTurnScore;
    int compTotScore;
    int compTurnScore;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setup();
        rollOnClick();
        holdOnClick();
        resetOnClick();
    }

    protected void setup()
    {
        userTotScoreTxt = findViewById(R.id.user);
        compTotScoreTxt = findViewById(R.id.comp);
        turnScoreTxt = findViewById(R.id.turn);
        statusTxt = findViewById(R.id.status);
        diceImg = findViewById(R.id.dice);
        rollBtn = findViewById(R.id.roll);
        holdBtn = findViewById(R.id.hold);
        resetBtn = findViewById(R.id.reset);
    }

    protected int display(final int faceIdx)
    {
        diceImg.setImageResource(diceFaces[faceIdx]);
        return faceIdx + 1;
    }

    protected void rollOnClick()
    {
        rollBtn.setEnabled(false);
        rollBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                turnScoreTxt.setText("Your Turn Score: " + 0);
                int faceIdx = new Random().nextInt(DICE_FACES);
                final int score = display(faceIdx);
                setUserScore(score);
            }
        });
        rollBtn.setEnabled(true);
    }

    protected void holdOnClick()
    {
        holdBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                hold(Turn.PLAYER);
            }
        });
    }

    protected void hold(final Turn turn)
    {
        if(turn == Turn.PLAYER)
        {
            userTotScore += userTurnScore;
            userTurnScore = 0;
            turnScoreTxt.setText("Your Turn Score: " + userTurnScore);
            userTotScoreTxt.setText("Your Score: " + userTotScore);
            rollBtn.setEnabled(false);
            holdBtn.setEnabled(false);
            new Handler().postDelayed(new Runnable()
            {
                @Override
                public void run()
                {
                    statusTxt.setText("Computer's Turn");
                }
            }, 1000);
            computerTurn();
        } else
        {
            compTotScore += compTurnScore;
            compTurnScore = 0;
            turnScoreTxt.setText("Your Turn Score: " + compTurnScore);
            compTotScoreTxt.setText("Computer Score: " + compTotScore);
            rollBtn.setEnabled(true);
            holdBtn.setEnabled(true);
            new Handler().postDelayed(new Runnable()
            {
                @Override
                public void run()
                {
                    statusTxt.setText("User's Turn");
                }
            }, 1000);
        }
    }

    protected void resetOnClick()
    {
        resetBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                userTotScore = 0;
                userTurnScore = 0;
                compTotScore = 0;
                compTurnScore = 0;
                userTotScoreTxt.setText("Your Score: " + 0);
                compTotScoreTxt.setText("Computer Score: " + 0);
                turnScoreTxt.setText("Your Turn Score: " + 0);
                statusTxt.setText("Status: ");
                diceImg.setImageResource(DEFAULT_DICE_FACE);
                rollBtn.setEnabled(true);
                holdBtn.setEnabled(true);
            }
        });
    }

    protected void setUserScore(int score)
    {
        if(score != 1)
        {
            userTurnScore += score;
        } else
        {
            hold(Turn.PLAYER);
        }
        turnScoreTxt.setText("Your Turn Score: " + userTurnScore);
    }

    protected void setCompScore(int score)
    {
        if(score != 1)
        {
            compTurnScore += score;
        } else
        {
            hold(Turn.COMPUTER);
        }
        turnScoreTxt.setText("Your Turn Score: " + compTurnScore);
    }

    protected void computerTurn()
    {
        for(int i = 0; i < COMPUTER_TURNS; i++)
        {
            turnScoreTxt.setText("Your Turn Score: " + 0);
            final int faceIdx = new Random().nextInt(DICE_FACES);
            final boolean compRolledOne = faceIdx == 0;
            new Handler().postDelayed(new Runnable()
            {
                @Override
                public void run()
                {
                    if(compRolledOne)
                    {
                        new Handler().postDelayed(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                final int score = display(faceIdx);
                                setCompScore(score);
                                statusTxt.setText("Computer rolled a one");
                                hold(Turn.COMPUTER);
                            }
                        }, 1000);
                    } else
                    {
                        new Handler().postDelayed(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                final int score = display(faceIdx);
                                setCompScore(score);
                            }
                        }, 1000);
                    }
                }
            }, (i + 1) * 1000);
            if(compRolledOne)
            {
                return;
            }
        }
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                statusTxt.setText("Computer holds");
                hold(Turn.COMPUTER);
            }
        }, 1000);
    }
}

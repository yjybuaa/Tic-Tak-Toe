package course.ece.lab3;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import course.ece.lab3.GameView;

public class MainActivity extends AppCompatActivity {
    private GameView mGameView;
    private Button btnStart;

    public static final int PIECE_NONE = 0;
    public static final int PIECE_BLUE = 1;
    public static final int PIECE_RED = 2;
    public static final int STATE_NOT_START = 0;
    public static final int STATE_PLAYING = 1;
    public static final int STATE_BLUE_WIN = 2;
    public static final int STATE_RED_WIN = 3;
    public static final int STATE_DRAW_GAME = 4;
    public static final String TAG_GAME_STATE = "tagGameState";
    public static final String TAG_IS_BLUE_TURN = "tagIsBlueTurn";
    public static final String TAG_LINE_LEFT = "tagLineLeft";
    public static final String TAG_LINE_MIDDLE = "tagLineMiddle";
    public static final String TAG_LINE_RIGHT = "tagLineRight";
    public static final String TAG_WIN_LINE = "tagWinLine";

    int[][] boardState = new int[3][3];
    boolean[] hvWinLine = new boolean[8];
    boolean  isBlueTurn = true;
    int gameState = STATE_NOT_START;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        setTitle(R.string.wait_start);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        mGameView=(GameView)findViewById(R.id.mGameView);
        //mGameView = (GameView) findViewById(R.id.mGameView);
        mGameView.setHandler(new Handler() {
            public void handleMessage(Message msg) {
                if (gameState != STATE_PLAYING)
                    return;
                int posX = msg.getData().getInt(GameView.TAG_ON_TOUCH_X);
                int posY = msg.getData().getInt(GameView.TAG_ON_TOUCH_Y);
                inputPiece(posX, posY);

                mGameView.invalidate();
            }
        });
        btnStart = (Button) findViewById(R.id.btnStart);
        btnStart.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                gameState = STATE_PLAYING;
                if (isBlueTurn) setTitle(R.string.turn_blue);
                else setTitle(R.string.turn_red);
                btnStart.setVisibility(View.INVISIBLE);
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        boardState[i][j] = PIECE_NONE;
                    }
                }
                for (int i = 0; i < 8; i++)
                    hvWinLine[i] = false;
                mGameView.cleanAll();
                mGameView.invalidate();
            } });
    }


    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(TAG_GAME_STATE, gameState);
        outState.putBoolean(TAG_IS_BLUE_TURN, isBlueTurn);
        outState.putIntArray(TAG_LINE_LEFT,    boardState[0]);
        outState.putIntArray(TAG_LINE_MIDDLE,  boardState[1]);
        outState.putIntArray(TAG_LINE_RIGHT,   boardState[2]);
        outState.putBooleanArray(TAG_WIN_LINE, hvWinLine);
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        gameState = savedInstanceState.getInt(TAG_GAME_STATE, STATE_NOT_START);
        isBlueTurn = savedInstanceState.getBoolean(TAG_IS_BLUE_TURN, true);
        boardState[0] = savedInstanceState.getIntArray(TAG_LINE_LEFT);
        boardState[1] = savedInstanceState.getIntArray(TAG_LINE_MIDDLE);
        boardState[2] = savedInstanceState.getIntArray(TAG_LINE_RIGHT);
        hvWinLine = savedInstanceState.getBooleanArray(TAG_WIN_LINE);
        if (gameState == STATE_PLAYING) {
            btnStart.setVisibility(View.INVISIBLE);
            if (isBlueTurn) setTitle(R.string.turn_blue);
            else setTitle(R.string.turn_red); }
            else { btnStart.setVisibility(View.VISIBLE);
            switch (gameState) {
                case STATE_NOT_START :
                    setTitle(R.string.wait_start);
                    break;
                    case STATE_BLUE_WIN :
                        setTitle(R.string.win_blue);
                        break;
                        case STATE_RED_WIN :
                            setTitle(R.string.win_red);
                            break;
                            case STATE_DRAW_GAME :
                                setTitle(R.string.draw_game);
                                break;
            }
        }
        mGameView.cleanAll();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (boardState[i][j] == PIECE_BLUE)
                    mGameView.setBlueCross(i, j);
                else if (boardState[i][j] == PIECE_RED)
                    mGameView.setRedCircle(i, j); } }
                    for (int i = 0; i < 8; i++)
                        if (hvWinLine[i])
                            mGameView.setWinLine(i);
        mGameView.invalidate(); }

    private void inputPiece(int posX, int posY) {
        if (boardState[posX][posY] != PIECE_NONE)
       // {setTitle(R.string.draw_game);
            return;
         if (isBlueTurn) {
            boardState[posX][posY] = PIECE_BLUE;
            mGameView.setBlueCross(posX, posY);
           if (boardState[0][0] == PIECE_BLUE && boardState[0][1] == PIECE_BLUE && boardState[0][2] == PIECE_BLUE) {
               setTitle(R.string.win_blue);
               mGameView.setWinLine(3);
               gameState = STATE_BLUE_WIN;
               btnStart.setVisibility(View.VISIBLE);
           }
           else if (boardState[1][0] == PIECE_BLUE && boardState[1][1] == PIECE_BLUE && boardState[1][2] == PIECE_BLUE)
           {
              setTitle(R.string.win_blue);
               mGameView.setWinLine(4);
               gameState = STATE_BLUE_WIN;
               btnStart.setVisibility(View.VISIBLE);
           }
           else if (boardState[2][0] == PIECE_BLUE && boardState[2][1] == PIECE_BLUE && boardState[2][2] == PIECE_BLUE)
           {
               setTitle(R.string.win_blue);
               mGameView.setWinLine(5);
               gameState = STATE_BLUE_WIN;
               btnStart.setVisibility(View.VISIBLE);
           }
           else if (boardState[0][0] == PIECE_BLUE && boardState[1][0] == PIECE_BLUE && boardState[2][0] == PIECE_BLUE)
           {
               setTitle(R.string.win_blue);
               mGameView.setWinLine(0);
               gameState = STATE_BLUE_WIN;
               btnStart.setVisibility(View.VISIBLE);
           }
           else if (boardState[0][1] == PIECE_BLUE && boardState[1][1] == PIECE_BLUE && boardState[2][1] == PIECE_BLUE)
           {
               setTitle(R.string.win_blue);
               mGameView.setWinLine(1);
               gameState = STATE_BLUE_WIN;
               btnStart.setVisibility(View.VISIBLE);
           }
           else if (boardState[0][2] == PIECE_BLUE && boardState[1][2] == PIECE_BLUE && boardState[2][2] == PIECE_BLUE)
           {
               setTitle(R.string.win_blue);
               mGameView.setWinLine(2);
               gameState = STATE_BLUE_WIN;
               btnStart.setVisibility(View.VISIBLE);
           }
           else if (boardState[0][0] == PIECE_BLUE && boardState[1][1] == PIECE_BLUE && boardState[2][2] == PIECE_BLUE)
           {
               setTitle(R.string.win_blue);
               mGameView.setWinLine(6);
               gameState = STATE_BLUE_WIN;
               btnStart.setVisibility(View.VISIBLE);
           }
           else if (boardState[0][2] == PIECE_BLUE && boardState[1][1] == PIECE_BLUE && boardState[2][0] == PIECE_BLUE)
           {
               setTitle(R.string.win_blue);
               mGameView.setWinLine(7);
               gameState = STATE_BLUE_WIN;
               btnStart.setVisibility(View.VISIBLE);
           }

           else {
            isBlueTurn = false;
            setTitle(R.string.turn_red);}
         }
            else {
            boardState[posX][posY] = PIECE_RED;
            mGameView.setRedCircle(posX, posY);
            if (boardState[0][0] == PIECE_RED && boardState[0][1] == PIECE_RED && boardState[0][2] == PIECE_RED)
            {
                setTitle(R.string.win_red);
                mGameView.setWinLine(3);
                gameState = STATE_RED_WIN;
                btnStart.setVisibility(View.VISIBLE);
            }
            else if (boardState[1][0] == PIECE_RED && boardState[1][1] == PIECE_RED && boardState[1][2] == PIECE_RED)
            {
                setTitle(R.string.win_red);
                mGameView.setWinLine(4);
                gameState = STATE_RED_WIN;
                btnStart.setVisibility(View.VISIBLE);
            }
            else if(boardState[2][0] == PIECE_RED && boardState[2][1] == PIECE_RED && boardState[2][2] == PIECE_RED)
            {
                setTitle(R.string.win_red);
                mGameView.setWinLine(5);
                gameState = STATE_RED_WIN;
                btnStart.setVisibility(View.VISIBLE);
            }
            else if (boardState[0][0] == PIECE_RED && boardState[1][0] == PIECE_RED && boardState[2][0] == PIECE_RED)
            {
                setTitle(R.string.win_red);
                mGameView.setWinLine(0);
                gameState = STATE_RED_WIN;
                btnStart.setVisibility(View.VISIBLE);
            }
            else if (boardState[0][1] == PIECE_RED && boardState[1][1] == PIECE_RED && boardState[2][1] == PIECE_RED)
            {
                setTitle(R.string.win_red);
                mGameView.setWinLine(1);
                gameState = STATE_RED_WIN;
                btnStart.setVisibility(View.VISIBLE);
            }
            else if (boardState[0][2] == PIECE_RED && boardState[1][2] == PIECE_RED && boardState[2][2] == PIECE_RED)
            {
                setTitle(R.string.win_red);
                mGameView.setWinLine(2);
                gameState = STATE_RED_WIN;
                btnStart.setVisibility(View.VISIBLE);
            }
            else if (boardState[0][0] == PIECE_RED && boardState[1][1] == PIECE_RED && boardState[2][2] == PIECE_RED)
            {
                setTitle(R.string.win_red);
                mGameView.setWinLine(6);
                gameState = STATE_RED_WIN;
                btnStart.setVisibility(View.VISIBLE);
            }
            else if (boardState[0][2] == PIECE_RED && boardState[1][1] == PIECE_RED && boardState[2][0] == PIECE_RED)
            {
                setTitle(R.string.win_red);
                mGameView.setWinLine(7);
                gameState = STATE_RED_WIN;
                btnStart.setVisibility(View.VISIBLE);
            }
            else{
            isBlueTurn = true;
            setTitle(R.string.turn_blue);}
        }
        if (boardState[0][0] != PIECE_NONE && boardState[0][1] != PIECE_NONE && boardState[0][2] != PIECE_NONE &&
        boardState[1][0] != PIECE_NONE && boardState[1][1] != PIECE_NONE && boardState[1][2] != PIECE_NONE &&
                boardState[2][0] != PIECE_NONE && boardState[2][1] != PIECE_NONE && boardState[2][2] != PIECE_NONE &&
                gameState != STATE_BLUE_WIN && gameState != STATE_RED_WIN)
        setTitle(R.string.draw_game);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
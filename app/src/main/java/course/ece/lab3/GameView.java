package course.ece.lab3;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceView;

public class GameView extends SurfaceView {
    public float mDivision = 50f;
    public static String TAG_ON_TOUCH_X = "tagOnTouchX";
    public static String TAG_ON_TOUCH_Y = "tagOnTouchY";
    private Handler mHandler;

    public static final int LINE_TOP_H = 0;
    public static final int LINE_MIDDLE_H = 1;
    public static final int LINE_BOTTOM_H = 2;
    public static final int LINE_LEFT_V = 3;
    public static final int LINE_MIDDLE_V = 4;
    public static final int LINE_RIGHT_V = 5;
    public static final int LINE_LEFT_D = 6;
    public static final int LINE_RIGHT_D = 7;
    public static final int PIECE_NONE = 0;
    public static final int PIECE_BLUE = 1;
    public static final int PIECE_RED = 2;
    public int[][] boardState;
    public boolean[] hvLines;




    public void onDraw(Canvas canvas){
        drawBoard(canvas);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (boardState[i][j] == PIECE_BLUE) {
                    drawBlueCross(canvas, i, j);
                }
                else if (boardState[i][j] == PIECE_RED) {
                    drawRedCircle(canvas, i, j);
                } } }
                if (hvLines[LINE_TOP_H])
                        drawWinLine(canvas, LINE_TOP_H,    boardState[0][0] == PIECE_BLUE);
        if (hvLines[LINE_MIDDLE_H])
            drawWinLine(canvas, LINE_MIDDLE_H, boardState[0][1] == PIECE_BLUE);
        if (hvLines[LINE_BOTTOM_H])
            drawWinLine(canvas, LINE_BOTTOM_H, boardState[0][2] == PIECE_BLUE);
        if (hvLines[LINE_LEFT_V])
            drawWinLine(canvas, LINE_LEFT_V,   boardState[0][0] == PIECE_BLUE);
        if (hvLines[LINE_MIDDLE_V])
            drawWinLine(canvas, LINE_MIDDLE_V, boardState[1][0] == PIECE_BLUE);
        if (hvLines[LINE_RIGHT_V])
            drawWinLine(canvas, LINE_RIGHT_V,  boardState[2][0] == PIECE_BLUE);
        if (hvLines[LINE_LEFT_D])
            drawWinLine(canvas, LINE_LEFT_D,   boardState[0][0] == PIECE_BLUE);
        if (hvLines[LINE_RIGHT_D])
            drawWinLine(canvas, LINE_RIGHT_D,  boardState[2][0] == PIECE_BLUE);
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (mHandler == null || motionEvent.getAction() != MotionEvent.ACTION_DOWN)
            return false;
        int ptrCount = motionEvent.getPointerCount();
        for (int i = 0; i < ptrCount; i++) {
            float tmpX = motionEvent.getX(i);
            float tmpY = motionEvent.getY(i);
            if (tmpX > mDivision && tmpX < mDivision * 7 && tmpY > mDivision && tmpY < mDivision * 7) {
                int posX = 0;
                int posY = 0;
                if (tmpX > mDivision * 5) {
                    posX = 2; }
                    else if (tmpX > mDivision * 3) {
                    posX = 1; } if (tmpY > mDivision * 5) {
                    posY = 2; } else if (tmpY > mDivision * 3) {
                    posY = 1; } Message msg = mHandler.obtainMessage();
                Bundle bundle = new Bundle();
                bundle.putInt(TAG_ON_TOUCH_X, posX);
                bundle.putInt(TAG_ON_TOUCH_Y, posY);
                msg.setData(bundle);
                mHandler.sendMessage(msg);
            }
        }
        return true;
    }

    public void setHandler(Handler handler) {
        mHandler = handler;
    }

    private void drawRedCircle(Canvas canvas, int posX, int posY) {
        if (canvas == null) return;
        Paint paint  = new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5.0f);
        canvas.drawCircle(mDivision * (posX * 2 + 2), mDivision * (posY * 2 + 2), mDivision - 10, paint);
    }

    private void drawBlueCross(Canvas canvas, int posX, int posY) {
        if (canvas == null) return;
        Paint paint  = new Paint();
        paint.setColor(Color.BLUE);
        paint.setStrokeWidth(5.0f);
        canvas.drawLine(mDivision * (posX * 2 + 1) + 10, mDivision * (posY * 2 + 1) + 10.0f, mDivision * (posX * 2 + 3) - 10, mDivision * (posY * 2 + 3) - 10.0f, paint);
        canvas.drawLine(mDivision * (posX * 2 + 3) - 10, mDivision * (posY * 2 + 1) + 10.0f, mDivision * (posX * 2 + 1) + 10, mDivision * (posY * 2 + 3) - 10.0f, paint);
    }

    private void drawWinLine(Canvas canvas, int line, boolean blue) {
        if (canvas == null)
            return;
        Paint paint  = new Paint();
        paint.setColor((blue)? Color.BLUE : Color.RED);
        paint.setStrokeWidth(10.0f); switch (line) {
            case LINE_TOP_H : canvas.drawLine(mDivision * 2, mDivision * 2, mDivision * 6, mDivision * 2, paint);
            break;
            case LINE_MIDDLE_H : canvas.drawLine(mDivision * 2, mDivision * 4, mDivision * 6, mDivision * 4, paint);
            break;
            case LINE_BOTTOM_H : canvas.drawLine(mDivision * 2, mDivision * 6, mDivision * 6, mDivision * 6, paint);
            break;
            case LINE_LEFT_V : canvas.drawLine(mDivision * 2, mDivision * 2, mDivision * 2, mDivision * 6, paint);
            break;
            case LINE_MIDDLE_V : canvas.drawLine(mDivision * 4, mDivision * 2, mDivision * 4, mDivision * 6, paint);
            break;
            case LINE_RIGHT_V : canvas.drawLine(mDivision * 6, mDivision * 2, mDivision * 6, mDivision * 6, paint);
            break;
            case LINE_LEFT_D : canvas.drawLine(mDivision * 2, mDivision * 2, mDivision * 6, mDivision * 6, paint);
            break;
            case LINE_RIGHT_D : canvas.drawLine(mDivision * 2, mDivision * 6, mDivision * 6, mDivision * 2, paint);
            break;
        }
    }

    public void cleanAll() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                boardState[i][j] = PIECE_NONE;
            }
        }
        for (int i = 0; i < 8; i++) {
            hvLines[i] = false;
        }
    }

    public void setBlueCross(int posX, int posY) {
        boardState[posX][posY] = PIECE_BLUE;
    }

    public void setRedCircle(int posX, int posY) {
        boardState[posX][posY] = PIECE_RED;
    }

    public void setWinLine(int line) {
        if (line < 0 || line >= 8) return;
        hvLines[line] = true;
    }




    public GameView(Context context, AttributeSet attrs){
        super(context, attrs);
        setWillNotDraw(false);
        boardState = new int[][] {
                {PIECE_NONE, PIECE_NONE, PIECE_NONE },
                { PIECE_NONE, PIECE_NONE, PIECE_NONE },
                { PIECE_NONE, PIECE_NONE, PIECE_NONE }
        };
        hvLines = new boolean[]
                { false, false, false, false, false, false, false, false };
    }

    public void onSizeChanged(int width, int height, int oldWidth, int oldHeight){
        mDivision = ((width < height)? width : height) / 8;
    }

    private void drawBoard(Canvas canvas) {
        if (canvas == null)
            return;
        canvas.drawColor(Color.BLACK);
        Paint paint = new Paint();
        paint.setColor(Color.GREEN);
        paint.setStrokeWidth(5.0f);
        canvas.drawLine(mDivision * 1, mDivision * 3, mDivision * 7, mDivision * 3, paint);
        canvas.drawLine(mDivision * 1, mDivision * 5, mDivision * 7, mDivision * 5, paint);
        canvas.drawLine(mDivision * 3, mDivision * 1, mDivision * 3, mDivision * 7, paint);
        canvas.drawLine(mDivision * 5, mDivision * 1, mDivision * 5, mDivision * 7, paint);
    }
}

package wayne.mycalendar.CalendarView;

/**
 * Created by Wayne on 2015/3/3.
 */

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

/** the cell of the calendar to display every single date **/
public class Cell{
    protected Rect mBound = null;
    protected int mDayOfMonth = 1;	// from 1 to 31
    protected Paint mPaint = new Paint(Paint.SUBPIXEL_TEXT_FLAG|Paint.ANTI_ALIAS_FLAG);
    protected Paint bgPaint = new Paint(Paint.SUBPIXEL_TEXT_FLAG|Paint.ANTI_ALIAS_FLAG);
    protected boolean isSelected = false;
    private int ScreenWidth;
    int dx, dy;
    public boolean isThisMonth = false;
    public int week;
    public int day;
    public Cell(int dayOfMon, Rect rect, float textSize, boolean bold, int ScreenWidth) {
        mDayOfMonth = dayOfMon;
        mBound = rect;
        mPaint.setTextSize(textSize);
        bgPaint.setTextSize(textSize);
        mPaint.setColor(Color.BLACK);
        if(bold) mPaint.setFakeBoldText(true);
        dx = (int) mPaint.measureText(String.valueOf(mDayOfMonth)) / 2;
        dy = (int) (-mPaint.ascent() + mPaint.descent()) / 2;
        this.ScreenWidth = ScreenWidth;
    }

    public Cell(int dayOfMon, Rect rect, float textSize, int ScreenWidth) {
        this(dayOfMon, rect, textSize, false, ScreenWidth);
    }

    protected void draw(Canvas canvas) {

        if(isSelected){
            canvas.drawCircle(mBound.centerX(),(mBound.centerY()+3),mBound.height()/3,bgPaint);
        }
        canvas.drawText(String.valueOf(mDayOfMonth), mBound.centerX() - dx, mBound.centerY() + dy, mPaint);
    }
    public int getDayOfMonth() {
        return mDayOfMonth;
    }

    public boolean hitTest(int x, int y) {
        return mBound.contains(x, y);
    }

    public Rect getBound() {
        return mBound;
    }

    public String toString() {
        return String.valueOf(mDayOfMonth)+"("+mBound.toString()+")";
    }
}

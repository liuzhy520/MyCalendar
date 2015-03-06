package wayne.mycalendar.CalendarView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.MonthDisplayHelper;
import android.view.MotionEvent;
import android.widget.ImageView;

import java.util.Calendar;

/**
 * Created by Wayne on 2015/3/3.
 */
public class CalendarView extends ImageView {
    private static int CELL_WIDTH = 50;
    private static int CELL_HEIGH = 50;
    private static int CELL_MARGIN_TOP = 0;
    private static int CELL_MARGIN_LEFT = 0;
    private static float CELL_TEXT_SIZE;
    private int ScreenWidth = getContext().getResources().getDisplayMetrics().widthPixels;
    private static final String TAG = "CalendarView";
    private Calendar mRightNow = null;

    public final static int DEFAULT_SELECTED_TEXT_COLOR = Color.parseColor("#ffffff");
    public final static int DEFAULT_SELECTED_BG_COLOR = Color.parseColor("#fe5c03");
    private int mTextColor, mSelectedBGColor;
    private boolean isSelectedDefault = true;
    private Cell mToday = null;
    private Cell[][] mCells = new Cell[6][7];
    protected int selectedDate = 0, selectWeek, selectDay;
    private OnCellTouchListener mOnCellTouchListener = null;
    MonthDisplayHelper mHelper;

    private Context context;
    public interface OnCellTouchListener {
        public void onTouch(Cell cell);
    }

    public CalendarView(Context context) {
        this(context, null);
        this.context = context;

    }

    public CalendarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        this.context = context;
    }

    public CalendarView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initCalendarView();
        this.context = context;
    }

    private void initCalendarView() {
        mRightNow = Calendar.getInstance();

        CELL_WIDTH = (ScreenWidth / 7  ) ;
        CELL_HEIGH = CELL_WIDTH;
        CELL_TEXT_SIZE = 22;

        mHelper = new MonthDisplayHelper(mRightNow.get(Calendar.YEAR), mRightNow.get(Calendar.MONTH));

    }

    private void initCells() {

        class _calendar {
            public int day;
            public boolean thisMonth;
            public _calendar(int d, boolean b) {
                day = d;
                thisMonth = b;
            }
            public _calendar(int d) {
                this(d, false);
            }
        };
        _calendar tmp[][] = new _calendar[6][7];

        for(int i=0; i<tmp.length; i++) {
            int n[] = mHelper.getDigitsForRow(i);
            for(int d=0; d<n.length; d++) {
                if(mHelper.isWithinCurrentMonth(i,d))
                    tmp[i][d] = new _calendar(n[d], true);
                else
                    tmp[i][d] = new _calendar(n[d]);
            }
        }

        Calendar today = Calendar.getInstance();
        int thisDay = 0;
        mToday = null;
        if(mHelper.getYear()==today.get(Calendar.YEAR) && mHelper.getMonth()==today.get(Calendar.MONTH)) {
            thisDay = today.get(Calendar.DAY_OF_MONTH);
        }
        // build cells
        Rect Bound = new Rect(CELL_MARGIN_LEFT, CELL_MARGIN_TOP, CELL_WIDTH+CELL_MARGIN_LEFT, CELL_HEIGH+CELL_MARGIN_TOP);
        for(int week=0; week<mCells.length; week++) {
            for(int day=0; day<mCells[week].length; day++) {
                if(tmp[week][day].thisMonth) {

                    if(day==0 || day==6 ){
                        mCells[week][day] = new RedCell(tmp[week][day].day, new Rect(Bound), CELL_TEXT_SIZE, this.ScreenWidth);
                        mCells[week][day].isThisMonth = true;
                        mCells[week][day].week = week;
                        mCells[week][day].day = day;
                    }
                    else{
                        mCells[week][day] = new Cell(tmp[week][day].day, new Rect(Bound), CELL_TEXT_SIZE, this.ScreenWidth);
                        mCells[week][day].isThisMonth = true;
                        mCells[week][day].week = week;
                        mCells[week][day].day = day;
                    }
                } else {
                    mCells[week][day] = new GrayCell(tmp[week][day].day, new Rect(Bound), CELL_TEXT_SIZE, this.ScreenWidth);
                    mCells[week][day].week = week;
                    mCells[week][day].day = day;

                }
                if(selectedDate != 0){
                    if(week == selectWeek && day == selectDay)
                        mCells[selectWeek][selectDay] = new SelectedCell(selectedDate, new Rect(Bound), CELL_TEXT_SIZE, this.ScreenWidth);
                }


                Bound.offset(CELL_WIDTH, 0); // move to next column

                // get today
                if(tmp[week][day].day==thisDay && tmp[week][day].thisMonth) {
                    mToday = mCells[week][day];
                    mCells[week][day].week = week;
                    mCells[week][day].day = day;
                }
            }
            Bound.offset(0, CELL_HEIGH); // move to next row and first column
            Bound.left = CELL_MARGIN_LEFT;
            Bound.right = CELL_MARGIN_LEFT+CELL_WIDTH;
        }
    }

    @Override
    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
        initCells();
        super.onLayout(changed, left, top, right, bottom);
    }

    public void setTimeInMillis(long milliseconds) {
        mRightNow.setTimeInMillis(milliseconds);
        initCells();
        this.invalidate();
    }
    public void updateCalendar(){
        initCells();
        this.invalidate();
    }
    public int getYear() {
        return mHelper.getYear();
    }

    public int getMonth() {
        return mHelper.getMonth();
    }

    public void nextMonth() {
        mHelper.nextMonth();
        selectedDate = 0;
        initCells();
        invalidate();

    }

    public void previousMonth() {
        mHelper.previousMonth();
        selectedDate = 0;
        initCells();
        invalidate();

    }


    public boolean firstDay(int day) {
        return day==1;
    }

    public boolean lastDay(int day) {
        return mHelper.getNumberOfDaysInMonth()==day;
    }
    public void clearCalendarSelection(){
        selectedDate = 0;
        initCells();
        invalidate();
    }

    public void goToday() {
        Calendar cal = Calendar.getInstance();
        mHelper = new MonthDisplayHelper(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH));
        initCells();
        selectedDate = mToday.getDayOfMonth() ;
        selectWeek = mToday.week;
        selectDay = mToday.day;
        initCells();
        invalidate();
    }

    public Calendar getDate() {
        return mRightNow;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(mOnCellTouchListener!=null){
            for(Cell[] week : mCells) {
                for(Cell day : week) {
                    if(day.hitTest((int)event.getX(), (int)event.getY())) {
                        mOnCellTouchListener.onTouch(day);
                    }
                }
            }
        }

        return super.onTouchEvent(event);
    }

    public void setOnCellTouchListener(OnCellTouchListener p) {
        mOnCellTouchListener = p;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // draw cells
        for(Cell[] week : mCells) {
            for(Cell day : week) {
                day.draw(canvas);
            }
        }
    }
    public void setSelectedCellColor(int textColor, int bgColor){
        mTextColor = textColor;
        mSelectedBGColor = bgColor;
        isSelectedDefault = false;
    }
    public class GrayCell extends Cell {
        public GrayCell(int dayOfMon, Rect rect, float s, int ScreenWidth) {
            super(dayOfMon, rect, s, ScreenWidth);
            mPaint.setColor(Color.LTGRAY);
        }
    }
    public class SelectedCell extends Cell{
        public SelectedCell(int dayOfMon, Rect rect, float s, int ScreenWidth) {
            super(dayOfMon, rect, s, ScreenWidth);
            if(isSelectedDefault){
                mPaint.setColor(DEFAULT_SELECTED_TEXT_COLOR);
                bgPaint.setColor(DEFAULT_SELECTED_BG_COLOR);
            }else{
                mPaint.setColor(mTextColor);
                bgPaint.setColor(mSelectedBGColor);
            }

            isSelected = true;
        }
    }
    private class RedCell extends Cell {
        public RedCell(int dayOfMon, Rect rect, float s, int ScreenWidth) {
            super(dayOfMon, rect, s, ScreenWidth);
            mPaint.setColor(0xdddd0000);
        }

    }
}

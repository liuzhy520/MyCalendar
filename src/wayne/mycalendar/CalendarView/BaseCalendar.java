package wayne.mycalendar.CalendarView;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateUtils;
import android.util.AttributeSet;
import android.view.*;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import wayne.mycalendar.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Wayne on 2015/3/2.
 */
public class BaseCalendar extends LinearLayout {
    private LinearLayout parent, ll_title_bar;
    private ImageView iv_previous, iv_next;
    private Context context;
    private CalendarView calendarView;
    private TextView tv_month;
    private Date mDate;
    private Date startValidDate;
    private static final int GET_ON_SELECTED_DATE = 0X00;
    private static final int SET_TITLE_BAR_COLOR = 0X001;
    private static final int SET_START_VALID_DATE = 0X02;
    //Handler
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GET_ON_SELECTED_DATE:
                    mDate = (Date) msg.obj;
                    if(listener != null){
                        listener.onResult(mDate);
                    }

                    break;
                case SET_TITLE_BAR_COLOR:
                    int color = Integer.valueOf(msg.obj.toString());
                    ll_title_bar.setBackgroundColor(color);
                    break;
                case SET_START_VALID_DATE:
                    startValidDate = (Date) msg.obj;
                    calendarView.setStartValidDate(startValidDate);
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);

        }

    };
    public BaseCalendar(Context context) {
        super(context);
        this.context = context;
        setCalendar();
    }
    public BaseCalendar(Context context, AttributeSet attrs) {
        super(context,attrs);
        this.context = context;
        setCalendar();
    }

    private void setCalendar(){
        parent = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.calendar_view, this);
        ll_title_bar = (LinearLayout) findViewById(R.id.base_calendar_title_bar);
        calendarView = (CalendarView) parent.findViewById(R.id.calendar_view);
        mDate = new Date();
        calendarView.setOnCellTouchListener(new CalendarView.OnCellTouchListener() {
            @Override
            public void onTouch(Cell cell) {
                if(cell.isThisMonth){
//                    Toast.makeText(context, DateUtils.getMonthString(calendarView.getMonth(), DateUtils.LENGTH_LONG) + String.valueOf(cell.getDayOfMonth()), Toast.LENGTH_LONG).show();
                    calendarView.selectedDate = cell.getDayOfMonth();
                    calendarView.selectDay = cell.day;
                    calendarView.selectWeek = cell.week;
                    calendarView.updateCalendar();
                    mDate.setYear(calendarView.getYear() - 1900);
                    mDate.setMonth(calendarView.getMonth());
                    mDate.setDate(cell.getDayOfMonth());
                    SendMessage(GET_ON_SELECTED_DATE, mDate);
                }

            }
        });
        calendarView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getContext().getResources().getDisplayMetrics().widthPixels * 6 / 7));
        tv_month = (TextView) parent.findViewById(R.id.base_calendar_month_text);
        tv_month.setText(DateUtils.getMonthString(calendarView.getMonth(), DateUtils.LENGTH_LONG));
        iv_previous = (ImageView) parent.findViewById(R.id.base_calendar_back_btn);
        iv_previous.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                calendarView.previousMonth();
                tv_month.setText(DateUtils.getMonthString(calendarView.getMonth(), DateUtils.LENGTH_LONG));
            }
        });
        iv_next = (ImageView) parent.findViewById(R.id.base_calendar_next_btn);
        iv_next.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                calendarView.nextMonth();
                tv_month.setText(DateUtils.getMonthString(calendarView.getMonth(), DateUtils.LENGTH_LONG));
            }
        });
        /** swipe calender **/
        final GestureDetector gestureDetector = new GestureDetector(context, new GestureDetector.OnGestureListener() {
            private static final int SWIPE_THRESHOLD = 100;
            private static final int SWIPE_VELOCITY_THRESHOLD = 100;
            @Override
            public boolean onDown(MotionEvent motionEvent) {
                return false;
            }

            @Override
            public void onShowPress(MotionEvent motionEvent) {

            }

            @Override
            public boolean onSingleTapUp(MotionEvent motionEvent) {
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {

                return false;

            }

            @Override
            public void onLongPress(MotionEvent motionEvent) {

            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

                /** from right to left **/
                if (e1.getX() - e2.getX() > SWIPE_THRESHOLD
                        && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                    onSwipeLeft();
                }
                /** from left to right **/
                if (e2.getX() - e1.getX() > SWIPE_THRESHOLD
                        && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                    onSwipeRight();
                }
                return false;
            }
            public void onSwipeRight() {
                calendarView.previousMonth();
                if(startValidDate != null){
                    calendarView.setStartValidDate(startValidDate);
                }
                tv_month.setText(DateUtils.getMonthString(calendarView.getMonth(), DateUtils.LENGTH_LONG));
            }

            public void onSwipeLeft() {
                calendarView.nextMonth();
                if(startValidDate != null){
                    calendarView.setStartValidDate(startValidDate);
                }
                tv_month.setText(DateUtils.getMonthString(calendarView.getMonth(), DateUtils.LENGTH_LONG));
            }
        });
        setLongClickable(true);
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return gestureDetector.onTouchEvent(motionEvent);

            }
        });
        /** swipe calender end **/
    }
    public void setStartValidDate(Date date){
        SendMessage(SET_START_VALID_DATE, date);
    }
    public void setTitleBarColor(int color){
        SendMessage(SET_TITLE_BAR_COLOR, color);
    }
    public void setToday(){
        calendarView.goToday();
        mDate = calendarView.getToday().getTime();
        tv_month.setText(DateUtils.getMonthString(calendarView.getMonth(), DateUtils.LENGTH_LONG));
    }
    public void refreshCalendar(){
        calendarView.clearCalendarSelection();
    }

    public OnCellClickListener listener = null;
    public interface OnCellClickListener {
        public void onResult(Date date);
    }
    public void setOnCellClickListener(OnCellClickListener l) {
        listener = l;
    }
    public Date getDate(){
        return this.mDate;
    }
    public String getDateString(){
       SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(mDate);
//       return this.mDate.getDate() + "/" +  DateUtils.getMonthString(this.mDate.getMonth(), DateUtils.LENGTH_LONG) + "/" + this.mDate.getYear() ;

    }
    private void SendMessage(int what, Object obj){
        Message msg = new Message();
        msg.what = what;
        msg.obj = obj;
        handler.sendMessage(msg);
    }

}

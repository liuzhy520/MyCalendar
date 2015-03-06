package wayne.mycalendar;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;
import wayne.mycalendar.CalendarView.BaseCalendar;

import java.util.Date;

public class MainActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    private BaseCalendar baseCalendar;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        initView();
    }
    public void initView(){
        baseCalendar = (BaseCalendar) findViewById(R.id.my_calendar_view);
        baseCalendar.setOnCellClickListener(new BaseCalendar.OnCellClickListener() {
            @Override
            public void onResult(Date date) {
                Toast.makeText(MainActivity.this, date.toString(),Toast.LENGTH_LONG).show();
            }
        });
    }
}

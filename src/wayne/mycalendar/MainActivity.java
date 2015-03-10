package wayne.mycalendar;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import wayne.mycalendar.CalendarView.BaseCalendar;

import java.util.Date;

public class MainActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    private BaseCalendar baseCalendar;
    private TextView output_string;
    private Button today,getString,refresh;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        initView();
        setViews();
    }
    private void initView(){
        baseCalendar = (BaseCalendar) findViewById(R.id.my_calendar_view);
        output_string = (TextView) findViewById(R.id.output_string);
        today = (Button) findViewById(R.id.today);
        getString = (Button) findViewById(R.id.getString);
        refresh = (Button) findViewById(R.id.refresh);

    }
    private void setViews(){
        Date d = new Date();
        baseCalendar.setStartValidDate(d);
        baseCalendar.refreshCalendar();
        baseCalendar.setOnCellClickListener(new BaseCalendar.OnCellClickListener() {
            @Override
            public void onResult(Date date) {
                Toast.makeText(MainActivity.this, date.toString(),Toast.LENGTH_SHORT).show();
                output_string.setText(date.toString());
            }
        });
        today.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                baseCalendar.setToday();
                output_string.setText(baseCalendar.getDateString());
            }
        });
        getString.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                output_string.setText(baseCalendar.getDateString());
            }
        });
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                baseCalendar.refreshCalendar();
                output_string.setText("");
            }
        });
    }
}

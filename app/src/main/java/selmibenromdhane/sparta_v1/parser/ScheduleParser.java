package selmibenromdhane.sparta_v1.parser;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import selmibenromdhane.sparta_v1.activity.DetailScheduleActivity1;
import selmibenromdhane.sparta_v1.activity.HomeActivity;
import selmibenromdhane.sparta_v1.adapter.ScheduleAdapter;
import selmibenromdhane.sparta_v1.adapter.SessionAdapter;
import selmibenromdhane.sparta_v1.fragment.ScheduleGymFragment;
import selmibenromdhane.sparta_v1.manager.Session;

/**
 * Created by sooheib on 11/8/16.
 */

public class ScheduleParser extends AsyncTask<Void,Void,Integer> implements SwipeRefreshLayout.OnRefreshListener {

    Context c;
    String jsonData;
    ListView lv;
    GridView lvv;
    public static String scheduleday1;
    public static String nameCourse="";
    public static String COURSE_EXTRA="course_code";
    public static String COURSE_COVER="course_cover";
    public static String COURSE_DESC="course_desc";
    public static String COURSE_CAPACITY="course_maxCapacity";

    public static String TRAINER_EXTRA="last_name";
    public static String TRAINER_PHOTO="photo";

    public static String SCHEDULE_ID ="schedule_id";
    public static String SCHEDULE_HOUR ="startTime";
    public static String SCHEDULE_DATE ="day";

    public static  String ROOM_NUMBER="room_number";
    public static  String COUNTMEMBER="countMumber";
    public static  String MAX_CAPACITY="max_capacity";

    SessionAdapter adapter;


    SwipeRefreshLayout swipeRefreshLayout;

    ProgressDialog pd;
    ArrayList<Object> schedules=new ArrayList<>();

    ScheduleGymFragment scheduleGymFragment=new ScheduleGymFragment();
    String todayy;
    String monthh;

    Calendar calendar = Calendar.getInstance();

    String currentday;
    String selectedday="";



    public ScheduleParser() {

    }

    public ScheduleParser(Context c, String jsonData, ListView lv) {
        this.c = c;
        this.jsonData = jsonData;
        this.lv = lv;
    }

    public ScheduleParser(Context c, String jsonData, ListView lv, SwipeRefreshLayout swipeRefreshLayout) {
        this.c = c;
        this.jsonData = jsonData;
        this.lv = lv;
        this.swipeRefreshLayout=swipeRefreshLayout;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        pd=new ProgressDialog(c);
        pd.setTitle("Parse");
        pd.setMessage("Parsing...Please wait");
        pd.show();

        swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    protected Integer doInBackground(Void... params) {

        return this.parseData();

    }

    @Override
    protected void onPostExecute(Integer result) {
        super.onPostExecute(result);

       pd.dismiss();
        //swipeRefreshLayout.setRefreshing(true);

        if(result==0)
        {
            Toast.makeText(c,"Unable To Parse", Toast.LENGTH_SHORT).show();
        }else {
            //BIND DATA TO LISTVIEW

            swipeRefreshLayout.setOnRefreshListener(this);

            adapter=new SessionAdapter(c,schedules);

            lv.setAdapter(adapter);
          //  adapter.notifyDataSetChanged();

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    System.out.println("ssssssssssss");
                    Toast.makeText(c,"SSSS",Toast.LENGTH_LONG).show();

                    Session item = (Session) adapter.getItem(position);

                    Intent intent=new Intent(c, DetailScheduleActivity1.class);

                    intent.putExtra(SCHEDULE_ID, item.getSchedule_i());
                    intent.putExtra(SCHEDULE_DATE, item.getDay());
                    intent.putExtra(COURSE_EXTRA, item.getCourse());
                    intent.putExtra(TRAINER_EXTRA, item.getTrainer());
                    intent.putExtra(SCHEDULE_HOUR, item.getStartTime());
                    intent.putExtra(COURSE_CAPACITY, item.getCourse_maxC());//int
                    intent.putExtra(COURSE_COVER, item.getCourse_cover());
                    intent.putExtra(COURSE_DESC, item.getCourse_desc());
                    intent.putExtra(TRAINER_PHOTO, item.getTrainer_photo());
                    intent.putExtra(ROOM_NUMBER, item.getRoom_name());
                    intent.putExtra(COUNTMEMBER,item.getCountMumber());

                    c.startActivity(intent);

                }
            });
        }

        swipeRefreshLayout.setRefreshing(false);

    }

    private int parseData()
    {
//        swipeRefreshLayout.setRefreshing(false);

        SimpleDateFormat sdftoday = new SimpleDateFormat("dd");
        SimpleDateFormat sdfmonth = new SimpleDateFormat("MM");
        SimpleDateFormat sdfyear = new SimpleDateFormat("yyyy");

        currentday=sdfmonth.format(calendar.getTime())+"/"+sdftoday.format(calendar.getTime())+"/"+sdfyear.format(calendar.getTime());


        selectedday=scheduleGymFragment.selecteddayGS;
        System.out.println("bb"+selectedday);

        if(!selectedday.equalsIgnoreCase("99")){
            currentday=selectedday;
        }




        try {

            JSONArray ja = new JSONArray(jsonData);
            JSONObject jo = null;

            schedules.clear();
            Session schedule;

            String schedule1="12/12/2222";
            for (int i = 0; i < ja.length(); i++) {
                jo = ja.getJSONObject(i);

                int id = jo.getInt("0");


                System.out.println("************currentday" + currentday);

                String scheduleday = jo.getString("1");
                System.out.println(scheduleday);
                scheduleday1 = scheduleday;

                String datee = jo.getString("12");
                System.out.println("date" + datee);

                        if(currentday.compareTo(scheduleday)==0)
                    {
                        String schedule_id = jo.getString("0");
                        System.out.println(schedule_id);

                        //intent.putExtra(SCHEDULE_HOUR, item.getStartTime());
                        String hour = jo.getString("2");
                        System.out.println(hour);

                        //intent.putExtra(COURSE_EXTRA, item.getCourse());


                        String coursename = jo.getString("3");
                        System.out.println(coursename);

                        nameCourse = coursename;

                        //intent.putExtra(COURSE_DESC, item.getCourse_desc());
                        String coursdesc = jo.getString("4");
                        System.out.println(coursdesc);


                        // intent.putExtra(COURSE_COVER, item.getCourse_cover());
                        String coursecover = jo.getString("5");

                        coursecover = "https://spartaapp.azurewebsites.net/Backend/partials/user_images/" + coursecover;

                        System.out.println(coursecover);

                        //intent.putExtra(COURSE_CAPACITY, item.getCourse_maxC());//int
                        String coursecapacity = jo.getString("6");
                        System.out.println("coursecapacity" + coursecapacity);


                        //intent.putExtra(TRAINER_EXTRA, item.getTrainer());
                        String trainername = jo.getString("7");

                        System.out.println(trainername);

                        // intent.putExtra(TRAINER_PHOTO, item.getTrainer_photo());
                        String trainerphoto = jo.getString("8");
                        trainerphoto = "https://spartaapp.azurewebsites.net/Backend/partials/teacher_photos/" + trainerphoto;

                        System.out.println(trainerphoto);

                        //intent.putExtra(ROOM_NUMBER, item.getRoom_name());
                        String roomname = jo.getString("9");
                        System.out.println(roomname);

                        String countMumber = jo.getString("10");
                        System.out.println("countMumber" + countMumber);


                        schedule = new Session();

                        schedule.setSchedule_i(schedule_id);
                        schedule.setStartTime(hour);
                        schedule.setCourse(coursename);
                        schedule.setTrainer(trainername);
                        schedule.setTrainer_photo(trainerphoto);
                        schedule.setCourse_cover(coursecover);
                        schedule.setCourse_desc(coursdesc);
                        schedule.setDay(scheduleday);
                        schedule.setRoom_name(roomname);
                        schedule.setCountMumber(Integer.parseInt(countMumber));
                        schedule.setCourse_maxC(Integer.parseInt(coursecapacity));
                        schedule.setStartDate(datee);

                        if(!schedule1.equalsIgnoreCase(scheduleday)){
                            schedules.add(scheduleday);

                            schedule1=scheduleday;

                        }
                        schedules.add(schedule);

                        System.out.println("asasas" + schedules);
                    }

                }
            return 1;


        } catch (JSONException e) {
            e.printStackTrace();
        }


        return 0;
    }

    @Override
    public void onRefresh() {
        doInBackground();
        onPostExecute(1);
    }
}

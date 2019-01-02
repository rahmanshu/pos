package com.trimitrasis.finosapps.Views.UtilView;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.widget.DatePicker;
import java.util.Calendar;

/**
 * Created by rahman on 20/04/2017.
 */

public class DateUtils {

    private static int lastPickYear;
    private static int lastPickMonth;
    private static int lastPickDay;

    public interface DateDialogPickerListener{
        void onDatePick(String date, String month, String year);
    }

    public static void showDatePickerDialog(FragmentManager fragmentManager
            , DateDialogPickerListener dateDialogPickerListener,int typeDate){
        DatePickerFragment datePickerDialog = new DatePickerFragment(typeDate);
        datePickerDialog.setDateDialogPickerListener(dateDialogPickerListener);
        datePickerDialog.show(fragmentManager, "date picker");
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        static DateDialogPickerListener dateDialogPickerListener;
        public static final int TYPE_CURRENT_DATE = 0;
        public static final int TYPE_BIRTH_DATE = 1;

        static int type_date ;

        public DatePickerFragment(){}

        @SuppressLint("ValidFragment")
        public DatePickerFragment(int type_date){
            this.type_date = type_date;
        }

        public static void setDateDialogPickerListener(DateDialogPickerListener listener){
            dateDialogPickerListener = listener;
        }

        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it

            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), this, year, month, day);
            if (type_date == TYPE_CURRENT_DATE){
                c.set(year,month,day);
                c.set(Calendar.HOUR_OF_DAY, c.getMinimum(Calendar.HOUR_OF_DAY));
                c.set(Calendar.MINUTE, c.getMinimum(Calendar.MINUTE));
                c.set(Calendar.SECOND, c.getMinimum(Calendar.SECOND));
                c.set(Calendar.MILLISECOND, c.getMinimum(Calendar.MILLISECOND));
                datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());
            }else if(type_date == TYPE_BIRTH_DATE){
                c.set(year,month,day);
                c.set(Calendar.HOUR_OF_DAY, c.getMaximum(Calendar.HOUR_OF_DAY));
                c.set(Calendar.MINUTE, c.getMaximum(Calendar.MINUTE));
                c.set(Calendar.SECOND, c.getMaximum(Calendar.SECOND));
                c.set(Calendar.MILLISECOND, c.getMaximum(Calendar.MILLISECOND));
                datePickerDialog.getDatePicker().setMaxDate(c.getTimeInMillis());
                if (lastPickYear == 0) lastPickYear = 1950;
                datePickerDialog.getDatePicker().updateDate(lastPickYear,lastPickMonth,lastPickDay);
            }

            return datePickerDialog;
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            lastPickYear = year;
            lastPickMonth = month;
            lastPickDay = day;
            dateDialogPickerListener.onDatePick(String.valueOf(day),String.valueOf(month),String.valueOf(year));
        }

    }

    public static int getMonth(String month){

        String[] months = {"Januari","Februari","Maret","April","Mei","Juni","Juli","Agustus",
                "September","Oktober","November","Desember"};

        int index = -1;
        for (int i=0;i<months.length;i++) {
            if (months[i].equals(month)) {
                index = i;
                break;
            }
        }
        return index+1;
    }

    public static String getMonthString(String monthNumber){
        String[] month = {"Januari","Februari","Maret","April","Mei","Juni","Juli","Agustus",
                "September","Oktober","November","Desember"};

        return month[Integer.parseInt(monthNumber)];
    }

    public static String getMonthNumber(String month){
        int monthInt = Integer.parseInt(month)+1;
        String result;
        if (monthInt > 9){
            result = ""+monthInt;
        }else result = "0"+monthInt;
        return result;
    }

    public static String getDateNumberString(String date){
        int dateInt = Integer.parseInt(date);
        String result;
        if (dateInt > 9){
            result = ""+dateInt;
        }else result = "0"+dateInt;
        return result;
    }

    public static String getDateFromServer(String rawDate){
        String[] splitDate = rawDate.split(" ");
        String date = splitDate[0];

        String[] splitDateFormat = date.split("-");
        String year = splitDateFormat[0];
        String month = splitDateFormat[1];
        String date1 = splitDateFormat[2];

        String getMonthString = getMonthString(String.valueOf(Integer.parseInt(month)-1));
        return date1+" "+getMonthString+" "+year;
    }

    public static String getTimeFromServer(String rawDate){
        String[] splitDate = rawDate.split(" ");
        String timeRaw = splitDate[1];

        String[] splitTimeFormat = timeRaw.split(":");
        String hour = splitTimeFormat[0];
        String minutes = splitTimeFormat[1];

        return hour+":"+minutes;
    }
}

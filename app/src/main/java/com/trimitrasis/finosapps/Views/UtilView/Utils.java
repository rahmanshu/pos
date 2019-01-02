package com.trimitrasis.finosapps.Views.UtilView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.Toast;
import com.trimitrasis.finosapps.Connection.Models.UserInfoModel;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.RoundingMode;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by rahman on 01/03/2017.
 */

public class Utils {

    public static boolean debug = true;
    private static String saldoUpdate = "0";
    public static boolean isUsingFirstBaseUrl = true;

    public static String getSignKey(String jenis){
        return getSHA1(md5(Constants.API_KEY+jenis+getCurrentDate("hh:mm:ss")));
    }


    public static void saveDataUser(Context context, UserInfoModel userModel){
        Utils.saveObjToFileExternal(userModel);
    }


    public static UserInfoModel getUserInfoModelFromFile(Context context){
        Object object = Utils.getObjFromFileExternal();
        UserInfoModel userModel = new UserInfoModel();
        if (object != null){
            userModel = (UserInfoModel) object;
        }
        return userModel;
    }


    public static String getSHA1(String string){
        byte[] hash;

        if (string!=null){
            try {
                hash = MessageDigest.getInstance("sha-1").digest(string.getBytes("UTF-8"));
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException("Huh, MD5 should be supported?", e);
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("Huh, UTF-8 should be supported?", e);
            }

            StringBuilder hex = new StringBuilder(hash.length * 2);

            for (byte b : hash){
                int i = (b & 0xFF);
                if (i < 0x10) hex.append('0');
                hex.append(Integer.toHexString(i));
            }

            return hex.toString();
        }else return "";

    }

    public static String md5(String string) {
        byte[] hash;

        try {
            hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Huh, UTF-8 should be supported?", e);
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);

        for (byte b : hash) {
            int i = (b & 0xFF);
            if (i < 0x10) hex.append('0');
            hex.append(Integer.toHexString(i));
        }

        return hex.toString();
    }

    public String getEncriptPin(String pin){
        return md5(getSHA1(pin));
    }

    public static String getTime(){
        return getCurrentDate("hh:mm:ss");
    }


    public static String getDevId(Context context){
        String android_id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        Utils.showLogI("android id : "+ android_id);
        //return "742c11bc3aff9dbc";
        return android_id;
    }

    public static String getEncriptedPin(String pin){
        return md5(getSHA1(pin));
    }


    public static void showLogI(String message){
        if (message == null) message = "no message";
        if (debug) Log.i("GRES INFO",message);
    }

    public static void saveObjToFile(Context context,Object obj,String name){
        FileOutputStream outputStream;
        try {
            outputStream = context.openFileOutput(name, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(outputStream);
            oos.writeObject(obj);
            oos.close();
            //outputStream.write(userPass.getBytes());
            outputStream.close();

            //Utils.showToast(context,"Data berhasil disimpan.");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            //Utils.showToast(context, "Gagal menyimpan data : " + e.getMessage());
        } catch (IOException e){
            e.printStackTrace();
            //Utils.showToast(context, "Gagal menyimpan data : " + e.getMessage());
        } catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    public static void saveObjToFileExternal(Object obj){
        FileOutputStream outputStream;
        File file = new File(Environment.getExternalStorageDirectory(),"ObjCache");
        try {
            outputStream = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(outputStream);
            oos.writeObject(obj);
            oos.close();
            outputStream.close();
            Utils.showLogI("Data berhasil disimpan.");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Utils.showLogI("Gagal menyimpan data : " + e.getMessage());
        } catch (IOException e){
            e.printStackTrace();
            Utils.showLogI("Gagal menyimpan data : " + e.getMessage());
        }
    }

    public static Object getObjFromFileExternal(){
        FileInputStream fis;
        Object returnlist = null;
        File file = new File(Environment.getExternalStorageDirectory(),"ObjCache");
        try {
            fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            returnlist = ois.readObject();
            ois.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnlist;
    }

    public static Object getObjFromFile(Context context, String name){
        FileInputStream fis;
        Object returnlist = null;
        try {
            fis = context.openFileInput(name);
            ObjectInputStream ois = new ObjectInputStream(fis);
            returnlist = ois.readObject();
            ois.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnlist;
    }
    public static String getCurrentDate(String format){
        // "dd-MM-yyyy hh:mm:ss"
        Calendar calendar = Calendar.getInstance();
        showLogI("Current time = "+calendar.getTime());
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        String fullDateString = dateFormat.format(calendar.getTime());
        showLogI("full date = "+fullDateString);
        return fullDateString;

    }


    public static String getCustomDate(int when){
        if (when != 0){
            Calendar calendar = Calendar.getInstance();
            calendar.roll(Calendar.DAY_OF_YEAR,when);
            showLogI("Current time = "+calendar.getTime());
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String fullDateString = dateFormat.format(calendar.getTime());
            showLogI("full date = "+fullDateString);
            return fullDateString;
        }else return getCurrentDate("yyyy-MM-dd");
    }

    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public static String getRupiahDisplay(String raw, boolean withCommas){
        if (raw != null){
            String result = "";
            ArrayList<String> splitString = new ArrayList<>();
            int divider = (raw.length()/3)+1;
            int stringLastIdx = raw.length();
            int stringBeginIdx;

            for (int i = 0; i < divider; i++) {
                if (stringLastIdx > 3 )stringBeginIdx = stringLastIdx - 3;
                else stringBeginIdx = 0;
                Utils.showLogI("split string "+i+" = "+raw.substring(stringBeginIdx,stringLastIdx));
                splitString.add(raw.substring(stringBeginIdx,stringLastIdx));
                stringLastIdx = stringLastIdx - 3;
            }

            for (int i = 0; i < splitString.size(); i++) {
                if (!splitString.get(i).equals("")){
                    if (i == 0)result = splitString.get(i) + result ;
                    else result = splitString.get(i) + "." + result;
                }
            }
            if (withCommas)return "Rp "+result+",-";
            else return "Rp "+result;

        }else {
            if (withCommas)return "Rp 0,-";
            else return "Rp 0";
        }
    }

    public static String getRupiahDisplay(String raw){
        return getRupiahDisplay(raw,true);
    }

    public static void showInfoDialog(Context context,String msg){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(msg);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    public static void showChoiceDialog(Context context,String title, String message
            , DialogInterface.OnClickListener positifListener){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message);
        builder.setTitle(title);
        builder.setPositiveButton("OK", positifListener);
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    public static String getPhoneNumber(Context context){
        if (context != null){
            String phoneNumber = "-";
            TelephonyManager tMgr =(TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
            try {
                phoneNumber = tMgr.getLine1Number();
                showLogI("phone number exist: "+phoneNumber);
            } catch (NullPointerException e){
                showLogI("error : "+e.toString());
            }
            showLogI("phone number : "+phoneNumber);
            return phoneNumber;
        }else return "-";
    }

    public static void showDialog(final Context context,String msg){
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(msg);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setCancelable(true);
        builder.show();
    }

    public static void closeVirtualKeyboard(Context context, View view){
        InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void openLink(Activity activity, String url){
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        activity.startActivity(i);
    }

    public static void showToast(String message , Context context){
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
    }

    public static void showToastLong(String message, Context context){
        Toast.makeText(context,message,Toast.LENGTH_LONG).show();
    }

    public static void openContactPicker(Activity activity){
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
        activity.startActivityForResult(intent, Constants.CONTACT_PICKER_RESULT);
    }

    public static String getFineNumberContact(String raw){
        String result = raw;
        if (result.contains("+62")){
            result = result.replace("+62","0");
        }
        if (result.contains(" ")){
            result = result.replace(" ","");
        }
        if (result.contains("-")){
            result = result.replace("-","");
        }
        return result;
    }


    public static String getPhoneNumberWithoutSuffix(String number){
        String noHp = number;
        if (noHp.contains("_")){
            noHp = noHp.substring(0,noHp.indexOf("_"));
        }
        return noHp;
    }


    public static String getResponseErrorConnection(){
        Utils.isUsingFirstBaseUrl = !Utils.isUsingFirstBaseUrl;
        return "Error Connection";
    }

    public static String getTimeStringAMPM(Date date){
        Calendar endTimeCalendar = Calendar.getInstance();
        endTimeCalendar.setTime(date);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("dd MM yyyy");
        String dateMessageString = simpleDateFormat2.format(date);
        Utils.showLogI("date format date message : "+dateMessageString);
        String lastCheckString = simpleDateFormat.format(date);

        Calendar currentDate = Calendar.getInstance();
        Date dateCurrent = currentDate.getTime();
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("dd MM yyyy");
        String dateCurrentString = simpleDateFormat1.format(dateCurrent);
        long diff = dateCurrent.getTime() - date.getTime();
        long diffDay = diff/(1000*60*60*24);

        if (dateMessageString.equals(dateCurrentString)){
            //lastCheckString = "Today";
        }else {
            String monthStringMessage = dateMessageString.substring(2, dateMessageString.length());
            String monthStringCurrent = dateCurrentString.substring(2,dateCurrentString.length());
            Utils.showLogI(monthStringMessage+" : "+monthStringCurrent);
        }

        Utils.showLogI("date : "+diffDay);
        Utils.showLogI("date current : "+simpleDateFormat1.format(dateCurrent));

        return lastCheckString;
    }

    public static String getDateString(Date date){
        Calendar currentTime = Calendar.getInstance();
        SimpleDateFormat simpleDateFormatCurrent = new SimpleDateFormat("dd MMMM");
        String currentCheckString = simpleDateFormatCurrent.format(currentTime.getTime());

        Calendar endTimeCalendar = Calendar.getInstance();
        endTimeCalendar.setTime(date);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM");
        String lastCheckString = simpleDateFormat.format(endTimeCalendar.getTime());


        //Utils.showLogI("date : "+lastCheckString);
        //Utils.showLogI("date current: "+currentCheckString);

        if (lastCheckString.equals(currentCheckString)){
            return "Today";
        }else return lastCheckString;
    }

    public static String getTimeString(Date date){
        Calendar endTimeCalendar = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        endTimeCalendar.setTime(date);
        long rangeTime = startDate.getTimeInMillis()-endTimeCalendar.getTimeInMillis();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MM yyyy hh:mm:ss");
        Utils.showLogI("date : "+simpleDateFormat.format(date));
        Utils.showLogI("range long : "+rangeTime);
        long minutes = rangeTime / Constants.MINUTE;
        long hours = rangeTime / Constants.HOUR;
        long month = rangeTime / Constants.MONTH;
        long day = rangeTime / Constants.DAY;
        String lastCheckString;

        if (rangeTime < Constants.MINUTE){
            lastCheckString = "last second ago..";
        } else if(rangeTime < Constants.HOUR){
            lastCheckString = minutes+" minute(s) ago";
        } else if (rangeTime < Constants.DAY){
            lastCheckString = hours+" hour(s) ago";
        } else if (rangeTime < Constants.MONTH){
            lastCheckString = day+" day(s) ago";
        } else if (rangeTime < Constants.YEAR){
            lastCheckString = month+" month(s) ago";
        } else {
            SimpleDateFormat format = new SimpleDateFormat("dd MMMM yyyy");
            lastCheckString = format.format(date);
        }
        Utils.showLogI("string time : "+lastCheckString);

        return lastCheckString;
    }

    public static void dispatchPhoto(Activity context){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(context.getPackageManager())!=null){
            File photoFile = null;
            try {
                photoFile = createImageFile(context);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (photoFile !=null){
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                context.startActivityForResult(takePictureIntent,Constants.RESULT_TAKE_PICTURE);
            }
        }
    }

    public static String photoPath;
    public static File createImageFile(Context context) throws IOException{
        String imageFileName = "chat_take_picture";
        File file = new File(context.getCacheDir().getAbsolutePath());
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,".jpeg",storageDir
        );
        photoPath = image.getAbsolutePath();
        return image;
    }

    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    public static void pickGalery(Activity activity){
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        activity.startActivityForResult(i,Constants.PICTURE_PICKER_RESULT);
    }

    public static String getMymeTypeStringFromPath(String url){
        String type = null;
        String extention = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extention != null){
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extention);
        }
        return type;
    }

    public static Bitmap getResizeBitmap(Bitmap source, int scaledRatio){
        Bitmap bitmap = null;
        float divider = Math.min((float) scaledRatio / source.getWidth(), (float) scaledRatio / source.getHeight());
        if (source.getWidth() == source.getHeight()){
            bitmap = bitmap.createScaledBitmap(source, scaledRatio, scaledRatio, false);
        }else {
            int width = Math.round((float)divider * source.getWidth());
            int height = Math.round((float)divider * source.getHeight());
            bitmap = bitmap.createScaledBitmap(source, width, height, false);
        }
        return bitmap;
    }

    public static void saveBitmapStream(Context context, InputStream is, File file)
            throws IOException{
        try {
            //File file = new File(context.getCacheDir(), "stream_file");
            OutputStream output = new FileOutputStream(file);
            try {
                try {
                    byte[] buffer = new byte[4 * 1024]; // or other buffer size
                    int read;

                    while ((read = is.read(buffer)) != -1) {
                        output.write(buffer, 0, read);
                    }
                    output.flush();
                } finally {
                    output.close();
                }
            } catch (Exception e) {
                e.printStackTrace(); // handle exception, define IOException and others
            }
        } finally {
            is.close();
        }
    }

    public static void dispatchCamera(Activity activity, Uri uri){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,uri);
        if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {
            activity.startActivityForResult(takePictureIntent, Constants.PHOTO_RESULT);
        }
    }



    public static Object toObjFromByte(byte[] data) throws IOException,ClassNotFoundException{
        Object obj = null;
        ByteArrayInputStream bis = null;
        ObjectInputStream ois = null;
        try {
            bis = new ByteArrayInputStream(data);
            ois = new ObjectInputStream(bis);
            obj = ois.readObject();
        } finally {
            if (bis != null) {
                bis.close();
            }
            if (ois != null) {
                ois.close();
            }
        }
        return obj;
    }

    public static void showMaps(Context context,String lat,String lon){
        Uri gmmIntentUri = Uri.parse(String.format("geo:%s,%s?q=%s,%s",lat,lon,lat,lon));
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(mapIntent);
        }
    }

    public static void copyToClipboard(Context context,String text){
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("cp_to_clipboard", text);
        clipboard.setPrimaryClip(clip);
    }

    public static String roundingFormat(String format,double value){
        DecimalFormat df = new DecimalFormat(format);
        df.setRoundingMode(RoundingMode.HALF_UP);
        return df.format(value);
    }


    public static String getCurrencyRupiah(double harga){
        DecimalFormat indoKurs = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        DecimalFormatSymbols formatRp = new DecimalFormatSymbols();
        formatRp.setCurrencySymbol("Rp. ");
        formatRp.setMonetaryDecimalSeparator(',');
        formatRp.setGroupingSeparator('.');
        indoKurs.setDecimalFormatSymbols(formatRp);
        return indoKurs.format(harga);
    }

    public static String getCurrencyRupiahTanpaSimbol(double harga){
        DecimalFormat indoKurs = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        DecimalFormatSymbols formatRp = new DecimalFormatSymbols();
        formatRp.setCurrencySymbol(" ");
        formatRp.setMonetaryDecimalSeparator(',');
        formatRp.setGroupingSeparator('.');
        indoKurs.setDecimalFormatSymbols(formatRp);
        return indoKurs.format(harga);
    }


    public static boolean cek_status(Context cek){

        ConnectivityManager cm = (ConnectivityManager) cek.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();

        if(info != null && info.isConnected()){
            return true;
        }else{
            return false;
        }
    }


    public static Date getDateRange(String stringDate, int intDate){
        Date dateResult = null;
        try{
            Date date_ = new SimpleDateFormat("yyyy-MM-dd").parse(stringDate);
            SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
            Calendar cal = Calendar.getInstance();
            cal.setTime(date_);
            cal.add(Calendar.DATE, intDate);
            String stringDateTo = formatDate.format(cal.getTime());
            dateResult = new SimpleDateFormat("yyyy-MM-dd").parse(stringDateTo);
        } catch (ParseException e){
            e.printStackTrace();
        }
        return dateResult;
    }


    public static String getIdJual(Context context){
        String key_id_jual = "";
        String device_id = Utils.getDevId(context);
        SimpleDateFormat dateFormatIdJual = new SimpleDateFormat("yyMMddHHmmss");
        Date date = new Date();
        key_id_jual = device_id.substring(0, 4).toUpperCase() + "-" + dateFormatIdJual.format(date);
        return key_id_jual;
    }




}

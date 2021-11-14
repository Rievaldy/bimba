package com.example.bimba;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.loader.content.CursorLoader;

import com.example.bimba.Model.Siswa;
import com.example.bimba.Model.User;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Util {

    public static void loadImage(String imageSource, ImageView bindOn, Context context){
        if(imageSource != null && !imageSource.equals("")){
            Target target = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    Drawable drawableBitmap = new BitmapDrawable(context.getResources(), bitmap);
                    bindOn.setImageDrawable(drawableBitmap);
                }

                @Override
                public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                    bindOn.setImageResource(R.drawable.ic_launcher_foreground);
                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {
                    bindOn.setImageResource(R.drawable.progress);
                }
            };

            String source = imageSource.replace("/storage/ssd2/020/17895020/public_html/", "https://bimbaindonesia.000webhostapp.com/");
            Picasso.get().load(source).memoryPolicy(MemoryPolicy.NO_CACHE)
                    .networkPolicy(NetworkPolicy.NO_CACHE).into(target);
            bindOn.setTag(target);
            Picasso.get().setLoggingEnabled(true);
            Log.d("tes", "loadImage: " + source);
            Log.d("test", "loadImage: true running");
        }
    }


    public static void showMessage(Context context, String message){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }


    public static String getPathFromUri(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    public static Object showDialogEditText(Context context, String title, String targetPath, View AttachResult, Object model){
        final BottomSheetDialog dialog = new BottomSheetDialog(context,R.style.BottomSheetDialogTheme);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.custom_dialog_edit_user);
        TextView titleDialog = dialog.findViewById(R.id.tvTitleDialog);
        LinearLayout target = dialog.findViewById(R.id.frameCustomDialog);
        String selectedDate = null;
        titleDialog.setText(title);

        putframe(context,target, targetPath, model);
        if(targetPath.equals("calendar")) {
            View v = target.findViewWithTag("dialog_calendar");
            CalendarView tanggalLahir = v.findViewById(R.id.calendarView);
            Log.d("tes", "showDialogEditText:  running");
            tanggalLahir.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                @Override
                public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
                    Log.d("tes", "showDialogEditText:  running");
                    if(AttachResult instanceof EditText ){
                        ((EditText) AttachResult).setText(day + "-" + (month+1) + "-" + year);
                    }else{
                        ((TextView) AttachResult).setText(day + "-" + (month+1) + "-" + year);
                    }
                    Log.d("tes", "onSelectedDayChange: ");
                    ((Siswa) model).setTanggalLahir(day + "-" + (month+1) + "-" + year);
                }
            });
        }

        Button btnSimpan = dialog.findViewById(R.id.btnDialogSimpan);
        btnSimpan.setOnClickListener(view -> {
            if(targetPath.equals("alamat")){
                View v = target.findViewWithTag("dialog_alamat");
                TextInputEditText alamat = v.findViewById(R.id.etDialogAlamat);
                TextInputEditText rt = v.findViewById(R.id.etDialogRT);
                TextInputEditText rw = v.findViewById(R.id.etDialogRW);
                ((TextView) AttachResult).setText(alamat.getText().toString()+", RT."+rt.getText().toString()+"/RW."+rw.getText().toString());
                ((User) model).setAlamat(alamat.getText().toString());
                ((User) model).setRt(rt.getText().toString());
                ((User) model).setRw(rw.getText().toString());
            }else if( targetPath.equals("jeniskelamin")){
                View v = target.findViewWithTag("dialog_jenis_kelamin");
                RadioGroup radioGroup = v.findViewById(R.id.rgKelaminDialog);
                Integer idchecked = radioGroup.getCheckedRadioButtonId();
                RadioButton rb = v.findViewById(idchecked);
                String value = rb.getText().toString();
                ((TextView)AttachResult).setText(value);
                if(model instanceof User){
                    ((User) model).setJenisKelamin(value);
                }else{
                    ((Siswa) model).setJenisKelamin(value);
                }
            }else if(targetPath.equals("nama")){
                View v = target.findViewWithTag("dialog_nama");
                TextInputEditText etFirstName = v.findViewById(R.id.etCustom1);
                TextInputEditText etLastName = v.findViewById(R.id.erCustom2);
                String value1 = etFirstName.getText().toString();
                String value2 = etLastName.getText().toString();
                ((TextView)AttachResult).setText(value1 + " " +value2);
                if(model instanceof User){
                    ((User) model).setFirstName(value1);
                    ((User) model).setLastName(value2);
                }else{
                    ((Siswa) model).setFirstName(value1);
                    ((Siswa) model).setLastName(value2);
                }
            }else if(targetPath.equals("nohp")){
                View v = target.findViewWithTag("dialog_notelp");
                TextInputEditText etData = v.findViewById(R.id.etDataEdited);
                String value = etData.getText().toString();
                ((TextView)AttachResult).setText(value);
                ((User) model).setNoHp(value);
            }else if(targetPath.equals("spinner")){
                View v = target.findViewWithTag("dialog_spinner");

            }
            dialog.dismiss();
        });
        Button btnBatal = dialog.findViewById(R.id.btnDialogBatal);
        btnBatal.setOnClickListener(view -> dialog.dismiss());
        dialog.show();

        return model;
    }

    private static void putframe(Context context, LinearLayout target, String targetPath, Object model){
        View child = null;

        if(targetPath.equals("calendar")){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
            child = inflater.inflate(R.layout.frame_calendar, null);
            child.setTag("dialog_calendar");
        }else if(targetPath.equals("jeniskelamin")){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
            child = inflater.inflate(R.layout.frame_custom_dialog_jenis_kelamin, null);
            child.setTag("dialog_jenis_kelamin");
            RadioGroup rgKelamin = child.findViewById(R.id.rgKelaminDialog);
            String jenisKelamin = null;
            if(model instanceof User){
                jenisKelamin = ((User) model).getJenisKelamin();
            }else{
                jenisKelamin = ((Siswa) model).getJenisKelamin();
            }

            if(jenisKelamin != null && jenisKelamin.toLowerCase().equals("perempuan")){
                rgKelamin.check(R.id.rdDialogPerempuan);
            }else{
                rgKelamin.check(R.id.rdDialogLakilaki);
            }
        }else if(targetPath.equals("alamat")){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
            child = inflater.inflate(R.layout.frame_multi_edit_custom_dialog_, null);
            child.setTag("dialog_alamat");
            TextInputEditText etAlamat = child.findViewById(R.id.etDialogAlamat);
            TextInputEditText etRT = child.findViewById(R.id.etDialogRT);
            TextInputEditText etRW = child.findViewById(R.id.etDialogRW);

            etAlamat.setText(((User)model).getAlamat());
            etRT.setText(((User)model).getRt());
            etRW.setText(((User)model).getRw());

        }else if(targetPath.equals("nama")){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
            child = inflater.inflate(R.layout.frame_custom_dialog_double_edit, null);
            String firstname = null;
            String lastname = null;
            child.setTag("dialog_nama");
            TextInputEditText etFirstName = child.findViewById(R.id.etCustom1);
            TextInputEditText etLastName = child.findViewById(R.id.erCustom2);

            if(model instanceof User){
                firstname = ((User) model).getFirstName();
                lastname = ((User) model).getLastName();
            }else{
                firstname = ((Siswa) model).getFirstName();
                lastname = ((Siswa) model).getLastName();
            }
            etFirstName.setText(firstname);
            etLastName.setText(lastname);
        }else{
            LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
            child = inflater.inflate(R.layout.frame_custom_dialog_edit, null);
            child.setTag("dialog_notelp");
            TextInputEditText editdata = child.findViewById(R.id.etDataEdited);
            editdata.setText(((User)model).getNoHp());
        }
        target.addView(child);
    }

    public static long findDifference(String start_date, String end_date)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        try {

            Date d1 = sdf.parse(start_date);
            Date d2 = sdf.parse(end_date);

            long difference_In_Time = d2.getTime() - d1.getTime();

            long difference_In_Days = TimeUnit.MILLISECONDS.toDays(difference_In_Time) % 365;


            return difference_In_Days;
        }
        catch ( ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }
}

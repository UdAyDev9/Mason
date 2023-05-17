package narasimhaa.com.mitraservice.Utility;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.media.ThumbnailUtils;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore.Images.Thumbnails;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;


import androidx.core.app.ActivityCompat;

import com.kinda.alert.KAlertDialog;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import narasimhaa.com.mitraservice.Adapater.ADPTFormsPopup;
import narasimhaa.com.mitraservice.Application.AppController;
import narasimhaa.com.mitraservice.Listener.GenericSelectListner;
import narasimhaa.com.mitraservice.Model.FormFieldOptionT;
import narasimhaa.com.mitraservice.R;


/**
 *
 * @author Srinivas
 *
 */
public class MyUtilities {

	static KAlertDialog pDialog;
	static Context context;
	public static final String KAlertDialogTitleLoding= "Loading...";
	public static final String KAlertDialogTitleError =  "Please check your connection or try again in sometime.";
	public static final String KAlertDialogTitleSwitch =  "Could not update status. Please try again in sometime";
	public static final String TOAST_SERVICE_NOT_ADDED_YET = "It seems Developer Service not added yet. Please add Developer Service.";
	public static final String TOAST_MATERIAL_NOT_ADDED_YET = "It seems no Materials not added yet. Please add Material.";
	public static final String PREF_EMAIL = "EMAIL_ID";
	public static final String PREF_USER_NAME = "USER_NAME";
	public static final String PREF_BOD_SEQ_NO = "BOD_SEQ_NO";
	public static final String PREF_USER_TYPE = "USER_TYPE";
	public static final String IS_DEVELOPER = "IS_DEVELOPER";
	public static final String PREF_USER_MOBILE_NO = "PREF_MOBILE_NO";
	public static final String PREF_USER_CITY = "PREF_CITY";
	public static final String PREF_USER_ADDRESS = "PREF_ADDRESS";
	public static final String PREF_USER_PINCODE = "PREF_PINCODE";
	public static final String PREF_SER_PER_SEQ_ID = "PREF_SER_PER_SEQ_ID";
	public static final String PREF_SERVICE_SEARCH = "PREF_SERVICE_SEARCH";
	public static final String PREF_MATERIAL_BUSINESS_TYPE = "PREF_MATERIAL_BUSINESS_TYPE";
	public static final String PREF_CITY_SEARCH = "PREF_CITY_SEARCH";
	public static final String PREF_BRAND_SEARCH = "PREF_BRAND_SEARCH";
	public static final String INTENT_KEY_MATERIAL_TYPE = "INTENT_KEY_MATERIAL_TYPE";
	public static final String INTENT_KEY_BUSINESS_TYPE = "INTENT_KEY_BUSINESS_TYPE";
	public static final String INTENT_KEY_BUSINESS_NAME = "INTENT_KEY_BUSINESS_NAME";
	public static final String INTENT_KEY_BRAND_NAME = "INTENT_KEY_BRAND_NAME";
	public static final String INTENT_KEY_DESC = "INTENT_KEY_DESC";
	public static final String INTENT_KEY_DOOR_DELIVERY = "INTENT_KEY_DOOR_DELIVERY";
	public static final String INTENT_KEY_PRICE = "INTENT_KEY_PRICE";
	public static final String INTENT_KEY_OFFER_PRICE = "INTENT_KEY_OFFER_PRICE";
	public static final String INTENT_KEY_ID = "INTENT_KEY_ID";
	public static final String PLACES_API_KEY = "AIzaSyDsGDyeOF-dCOwuQgTMvNEXLV1zicuG8_Q";
	public static final String ORDER_STATUS_PENDING = "Pending";
	public static final String ORDER_STATUS_QUOTED = "Quoted";
	public static final String ORDER_STATUS_PROCESSED = "Processed";
	public static final String ORDER_STATUS_DELIVERED = "Delivered";


	public static void showAlertDialog(Context context,int type,String title){

		pDialog = new KAlertDialog(context,type);
		pDialog.getProgressHelper().setBarColor(Color.parseColor("#1e88e5"));
		pDialog.setTitleText(title);
		pDialog.setCancelable(true);
		if (pDialog.isShowing()){
			pDialog.cancel();
			pDialog.show();
		}else {
			pDialog.show();
		}

	}

public static void cancelAlertDialog(Context context){

		//pDialog = new KAlertDialog(context, KAlertDialog.PROGRESS_TYPE);

	try{
		if (pDialog.isShowing())
			pDialog.cancel();

	}catch (Exception e){
		e.printStackTrace();
	}

	}

	public static Dialog showMyPoup(Activity mContext, final ArrayList<?> projects, final GenericSelectListner projectChangeListener, String title, final String type, String selected) {
		if (projects != null) { //&& title.equalsIgnoreCase("Lab Names") && title.equalsIgnoreCase("Assigned To")
			try {
				final ArrayList<FormFieldOptionT> list = new ArrayList<FormFieldOptionT>();
				final Dialog dialog = new Dialog((Activity) mContext);
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog.setContentView(R.layout.projectsdialog);
				// Collections.sort(projects);
				final ADPTFormsPopup adapter = new ADPTFormsPopup(mContext, list, type, selected, projectChangeListener);
				final ListView projectsListView = (ListView) dialog.findViewById(R.id.projects_list);
				list.addAll((List<FormFieldOptionT>) projects);


				if (type.equals("radiobutton") || type.equalsIgnoreCase("dropdown"))
					((Button) dialog.findViewById(R.id.projects_close)).setText("CLOSE");

				if (type.equals("radiobutton"))
					projectsListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

				projectsListView.setAdapter(adapter);

				final EditText search = (EditText) dialog.findViewById(R.id.projects_search);
				final TextView noData = (TextView) dialog.findViewById(R.id.searchEmpty);
				Button close = (Button) dialog.findViewById(R.id.projects_close);

				TextView header = (TextView) dialog.findViewById(R.id.projects_header);
				header.setText(MyUtilities.capitalizeFirstLetter(title));
				if (projects.size() <= 5) {
					((LinearLayout) dialog.findViewById(R.id.search_lay)).setVisibility(View.GONE);
				}

				if (list.size() == 0) {
					noData.setVisibility(View.VISIBLE);
				} else noData.setVisibility(View.GONE);
				projectsListView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
						projectChangeListener.setItem(list.get(arg2).getOptionValue() + ";" + list.get(arg2).getFieldId());

						dialog.dismiss();
					}
				});

				close.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						int value = 0;
						if (type.equals("checkbox")) {
							value = 1;
						}

						switch (value) {

							case 1:

								List<String> list = new ArrayList<String>();

								int children = projectsListView.getChildCount();

								for (int i = 0; i < children; i++) {

									CheckBox cb = ((CheckBox) projectsListView.getChildAt(i).findViewById(R.id.ui_CB));

									if (cb.isChecked()) {
										list.add("" + cb.getText());

									}

								}
								projectChangeListener.setItem(list.toString().replace("[", "").replace("]", ""));
								break;


						}

						dialog.dismiss();

					}
				});
				search.addTextChangedListener(new TextWatcher() {

					@Override
					public void onTextChanged(CharSequence s, int start, int before, int count) {
						// TODO Auto-generated method stub

					}

					@Override
					public void beforeTextChanged(CharSequence s, int start, int count, int after) {
						// TODO Auto-generated method stub

					}

					@Override
					public void afterTextChanged(Editable s) {
						// TODO Auto-generated method stub

						String searchValue = search.getText().toString();

						if (!searchValue.equals("")) {

							list.clear();

							for (int i = 0; i < projects.size(); i++) {
								if ((((FormFieldOptionT) projects.get(i)).getOptionValue().toLowerCase()).contains(searchValue.toLowerCase())) {

									list.add((FormFieldOptionT) projects.get(i));

								}

							}
						} else {
							list.clear();
							list.addAll((List<FormFieldOptionT>) projects);
						}
						projectsListView.setAdapter(adapter);
						if (list.size() == 0) {
							noData.setVisibility(View.VISIBLE);
						} else noData.setVisibility(View.GONE);
					}
				});


				return dialog;
			} catch (Exception e) {

			}
		}

		return null;
	}


	/**
	 * @param context
	 * @return
	 */
	public static String getIMEINO(Context context) {


		if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {

			return null;
		}
		if (((TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId() == null)
			return "000000000000000"; // 15 digits
		else
			return ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
	}

			// TODO: Consider calling
			//    ActivityCompat#requestPermissions
			// here to request the missing permissions, and then overriding
			//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
			//                                          int[] grantResults)
			// to handle the case where the user grants the permission. See the documentation
			// for ActivityCompat#requestPermissions for more details.


	public static int getColorsList(int position){

		ArrayList<Integer> list = new ArrayList<>();


		list.add(0xff424242);
		list.add(0xff455a64);
		list.add(0xff000000);
		list.add(0xffe91e63);
		list.add(0xff9c27b0);

		list.add(0xff673ab7);
		list.add(0xff3f51b5);
		list.add(0xff2196f3);
		list.add(0xff03a9f4);
		list.add(0xff4caf50);

		list.add(0xff827717);
		list.add(0xfff9a825);
		list.add(0xffffb74d);
		list.add(0xffe65100);
		list.add(0xff4e342e);


		list.add(0xff009688);
		list.add(0xffE91E63);
		list.add(0xff9C27B0);
		list.add(0xff673AB7);
		list.add(0xff29B6F6);


		list.add(0xff26C6DA);
		list.add(0xff26A69A);
		list.add(0xff66BB6A);
		list.add(0xffFFEE58);
		list.add(0xff8D6E63);
		list.add(0xff78909C);

		return list.get(position);


	}


	 
	/**
	 * 
	 * @param context
	 * @param
	 */
	public static void showToastNoInternet(Context context) {
		Toast toast;
		toast = Toast.makeText(context, "Please check internet connection.", Toast.LENGTH_LONG);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}
 
	

	public static String getNoOfDaysBackDate(int noofdays){

		Calendar cal= Calendar.getInstance();
		int currentDay=cal.get(Calendar.DAY_OF_MONTH);
		cal.set(Calendar.DAY_OF_MONTH, currentDay-noofdays);
     	Date date=cal.getTime();
		SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
		return ft.format(date);
	}







	public static void  writeContentToFile(String log) {
		try {

			FileWriter fw = new FileWriter(Environment.getExternalStorageDirectory()+ File.separator + "SpearOnLog.txt", true);
			BufferedWriter bw = new BufferedWriter(fw);
			PrintWriter out = new PrintWriter(bw);
			out.println(log);
			out.close();

		} catch (Exception e) {
			Log.v("Response","Problem writing to the file content log.");
		}
	}



	public static final String getExtension(final String filename) {

		if (filename == null) return null;
		final String afterLastSlash = filename.substring(filename.lastIndexOf('/') + 1);
		final int afterLastBackslash = afterLastSlash.lastIndexOf('\\') + 1;
		final int dotIndex = afterLastSlash.indexOf('.', afterLastBackslash);
		return (dotIndex == -1) ? "" : afterLastSlash.substring(dotIndex + 1);
	}
	
	/**
	 * 
	 * @param context
	 * @param
	 */
	public static void showToastTop(Context context) {
		Toast toast;
		toast = Toast.makeText(context, "\n		No Internet.	\n", Toast.LENGTH_LONG);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}



	/**
	 *
	 * @return yyyy-MM-dd
	 */
	public static String getDayUniqueNum() {

		Date dNow = new Date();
		SimpleDateFormat ft = new SimpleDateFormat("yyyyMMdd");
		return ft.format(dNow);
	}
	
	
	
	/**
	 * 
	 * @return yyyy-MM-dd
	 */
	public static String getTodayDate() {

		Date dNow = new Date();
		SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
		return ft.format(dNow);
	}



	/**
	 *
	 * @return yyyy-MM-dd
	 */
	public static String getTodayDateByMonthMMM() {

		Date dNow = new Date();
		SimpleDateFormat ft = new SimpleDateFormat("yyyy-MMM-dd");
		return ft.format(dNow);
	}
	

	
	
	public static String getAppVersionCode(Context context) {
		String app_ver = "";
		try {
			app_ver = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode
					+ "";
		} catch (Exception e) {

		}
		return app_ver;
	}

	public static void hideTitleBar(Activity activity) {
		activity.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
	}

	public static String replaceAllSqureBrace(String str) {
		try {
			str = str.replaceAll("\\[", "").replaceAll("\\]", "");
			if (!isNull(str)) {
				str = "";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;

	}
	
	
	public static String replaceFirstNLastSqureBrace(String str) {
		try {
			
			
			if(isNull(str)){
				str = str.substring(1, str.length()-1);
			}else if (!isNull(str)) {
				str = "";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;

	}
	
	/**
	 * 
	 * @return yyyy-MM-dd'_'HH:mm:ss
	 */
	public static String getTodaysDate() {

		Date dNow = new Date();
		SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
		return ft.format(dNow);
	}
	public static void showsoftKeyboard(View v) {
		InputMethodManager imm = (InputMethodManager) AppController.getInstance().getSystemService(Context.INPUT_METHOD_SERVICE);
		 imm.showSoftInput(v, InputMethodManager.SHOW_IMPLICIT);
	}
	
	
	public static void hidesoftKeyboard(View v) {
		InputMethodManager imm = (InputMethodManager)  AppController.getInstance().getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
	}

	public static void hidesoftKeyboardWindow(View v) {
		InputMethodManager imm = (InputMethodManager) AppController.getInstance().getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
	}

		
	public static String capitalizeFirstLetter(String original){
		
		return original.length() == 0 ? original : original.substring(0, 1).toUpperCase() + original.substring(1).toLowerCase();
	}
	public static ArrayList<String> FilesInFolder(final String Folder) {
		ArrayList<String> listFile = new ArrayList<String>();
		try {

			File file = new File(Folder + "/");
			File[] f = file.listFiles();
			for (int i = 0; i < f.length; i++) {
				listFile.add(f[i].getAbsolutePath());
			}
		
			Comparator<? super String> myComparator = new Comparator<String>() {

				public int compare(String name1, String name2) {
					name1 = name1.replaceAll(Folder, "")
							.replaceAll(".jpg", "");
					name2 = name2.replaceAll(Folder, "")
							.replaceAll(".jpg", "");
					int num1 = Integer.parseInt(name1);
					int num2 = Integer.parseInt(name2);
					// > : Increasing order
					// < : Decreasing order
					if (num1 < num2)
						return 1;
					else
						return -1;
				}

			};
			Collections.sort(listFile, myComparator);
			Collections.reverse(listFile);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return listFile;
	}

	/**
	 * 
	 * @param data
	 * @return
	 */

	public static boolean isNull(String data) {

		boolean isnull = false;
		if (data != null) {
			if (!data.equals("") && !data.equals("null") && !data.equals("-1")) {
				isnull = true;
			}
		} else {
			isnull = false;
		}

		return isnull;
	}



	/**
	 * @param context context
	 * @return boolean
	 **/
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
	@SuppressWarnings({ "deprecation" })
	public static boolean isAirplaneModeOn(Context context) {
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1){
        /* API 17 and above */
			return Settings.Global.getInt(context.getContentResolver(), Settings.Global.AIRPLANE_MODE_ON, 0) != 0;
		} else {
        /* below */
			return Settings.System.getInt(context.getContentResolver(), Settings.System.AIRPLANE_MODE_ON, 0) != 0;
		}
	}



	public static boolean isMobileDataEnabled(Context context){

		try {

			ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo info = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			return info != null && info.isConnected();
		}catch (Exception e){

			e.printStackTrace();

		}

		return  false;
	}

	/**
	 *
	 * @param context
	 * @return
	 */
	public static boolean isOnline(Context context) {
		try {

			ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (connectivity != null) {
				NetworkInfo[] info = connectivity.getAllNetworkInfo();
				if (info != null) {

					int length=info.length;

					for (int i = 0; i < length; i++) {
						if (info[i].getState() == NetworkInfo.State.CONNECTED) {
							return true;
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}




	/**
	 *
	 * @param context
	 * @param fontPath
	 * @return
	 */
	public static Typeface getTypeFaceByFontPath(Context context,
                                                 String fontPath) {
		Typeface custom_font = Typeface.createFromAsset(context.getAssets(),
				fontPath);
		return custom_font;
	}

	/**
	 *
	 * @param group
	 * @param font
	 */
	public static void setFont(ViewGroup group, Typeface font) {
		int count = group.getChildCount();
		View v;
		for (int i = 0; i < count; i++) {
			v = group.getChildAt(i);
			if (v instanceof TextView || v instanceof EditText
					|| v instanceof Button) {
				((TextView) v).setTypeface(font);
			} else if (v instanceof CheckBox) {
				((CheckBox) v).setTypeface(font);
			} else if (v instanceof RadioButton) {
				((RadioButton) v).setTypeface(font);

			} else if (v instanceof ViewGroup)
				setFont((ViewGroup) v, font);
		}
	}

	/**
	 *
	 * @param context
	 * @param title
	 * @param msg
	 */
	public static void showDailog(Context context, String title, String msg) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				context);
		alertDialogBuilder.setMessage(msg);
		alertDialogBuilder.setTitle(title);
		alertDialogBuilder.setPositiveButton("OK",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// alertDialogBuilder.d

					}
				});

		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}

	/**
	 *
	 * @param Folder
	 */
	public static void createDirs(String[] Folder) {
		try {


			String[] dirs = Folder;

			for (String dirName : dirs) {
				File dir = new File(dirName);
				if (!dir.exists()) {
					dir.mkdirs();
				} else {
					if (!dir.isDirectory()) {
						dir.mkdirs();
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	/**
	 *
	 * @param Folder
	 */
	public static void deleteFilesInFolder(String Folder) {
		try {
			File file = new File(Folder + "/");
			File[] f = file.listFiles();
			for (int i = 0; i < f.length; i++) {
				f[i].delete();
			}

		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	/**
	 *
	 * @param context
	 * @return
	 */
	public static String getAppVersionName(Context context) {
		String app_ver = "";
		try {
			app_ver = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0).versionName;
		} catch (Exception e) {

		}
		return app_ver;
	}

	/**
	 *
	 * @param path
	 * @return
	 */
	public static Drawable getDrawableFromPath(String path) {
		Drawable d = null;
		try {

			d = Drawable.createFromPath(path);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return d;
	}

	/**
	 *
	 * @param url
	 * @return
	 */
	public static Bitmap getImageFormVideo(String url) {
		Bitmap bmThumbnail = null;
		try {
			bmThumbnail = ThumbnailUtils.createVideoThumbnail(url,
					Thumbnails.MINI_KIND);

		} catch (Exception e) {
			// TODO: handle exception
		}

		return bmThumbnail;
	}

	/**
	 *
	 * @param fileName
	 * @return
	 */
	public static Bitmap getThumnail(File fileName) {
		Bitmap imageBitmap = null;
		try {

			FileInputStream fis = new FileInputStream(fileName);
			imageBitmap = BitmapFactory.decodeStream(fis);

			imageBitmap = Bitmap.createScaledBitmap(imageBitmap, 50, 50, false);

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return imageBitmap;

	}


	/**
	 *
	 * @param path
	 * @return
     */
	public static boolean deleteFile(String path) {



		if(MyUtilities.isNull(path)) {
			try {
				File file = new File(path);
				if (file.exists()) return file.delete();
			} catch (Exception e) {
				e.printStackTrace();
				return false;

			}
		}
				return false;

	}


	/**
	 *
	 * @param msg Message
	 */

	public static void showLog(Context context, String msg){

		Log.v(context.getPackageName(),msg);
	}

	/**
	 *
	 * @param tag TAG Name
	 * @param msg Message
     */

	public static void showLog(String tag, String msg){

		Log.v(tag,msg);
	}

	/**
	 *
	 * @param context
	 * @param mssg
	 */
	public static void showToast(Context context, String mssg) {
		Toast toast;
		toast = Toast.makeText(context, mssg + "", Toast.LENGTH_LONG);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}


	/**
	 *
	 * @param context
	 * @param mssg
	 */
	public static void showToast1(Context context, String mssg) {
		Toast toast;
		toast = Toast.makeText(context, mssg + "", Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}



	/**
	 *
	 * @return yyyy-MM-dd
	 */
	public static String getCurrentDateYearFirst() {

		Date dNow = new Date();
		SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
		return ft.format(dNow);
	}


	/**
	 *
	 * @return yyyy-MM-dd'_'HH:mm:ss
	 */
	public static String getCurrentDateNTime() {

		Date dNow = new Date();
		SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss");
		return ft.format(dNow);
	}



	/**
	 *
	 * @return dd-MM-yyyy
	 */
	public static String getCurrentDate() {

		Date dNow = new Date();
		SimpleDateFormat ft = new SimpleDateFormat("dd-MM-yyyy");
		return ft.format(dNow);
	}

	/**
	 *
	 * @return HH:MM
	 */
	public static String getCurrentTime() {
		SimpleDateFormat smdf = new SimpleDateFormat("HH:MM");
		Date d = new Date(System.currentTimeMillis());
		return smdf.format(d);
	}

	/**
	 *
	 * @param emailAddress
	 * @return
	 */
	public static boolean validateEmailAddress(String emailAddress) {
		String expression = "^[\\w\\-]([\\.\\w])+[\\w]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
		CharSequence inputStr = emailAddress;
		Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(inputStr);
		return matcher.matches();
	}

/*	*//**
	 *
	 * @param context
	 * @return
	 *//*
	public static String getIMEINO(Context context) {



			if (((TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId() == null)
				return "000000000000000"; // 15 digits
			else
				return ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();

	}*/

	/**
	 *
	 * @return dd
	 */
	public static int getCurrentDay() {
		SimpleDateFormat smdf = new SimpleDateFormat("dd");
		Date d = new Date(System.currentTimeMillis());
		return Integer.parseInt(smdf.format(d));
	}

	/**
	 *
	 * @return MM
	 */
	public static int getCurrentMonth() {
		SimpleDateFormat smdf = new SimpleDateFormat("MM");
		Date d = new Date(System.currentTimeMillis());
		return Integer.parseInt(smdf.format(d));
	}

	/**
	 *
	 * @return yyyy
	 */
	public static int getCurrentYear() {
		SimpleDateFormat smdf = new SimpleDateFormat("yyyy");
		Date d = new Date(System.currentTimeMillis());
		return Integer.parseInt(smdf.format(d));
	}


	public static  ArrayList<FormFieldOptionT> getRegistrationType(Context  context){

		ArrayList<FormFieldOptionT>  list=new ArrayList<>();


		FormFieldOptionT one=new FormFieldOptionT();
		one.setFieldId("1");
		one.setOptionValue("Telugu");


		FormFieldOptionT two=new FormFieldOptionT();
		two.setFieldId("2");
		two.setOptionValue("English");



		FormFieldOptionT three=new FormFieldOptionT();
		three.setFieldId("3");
		three.setOptionValue("Hindi");


		list.add(one);
		list.add(two);
		list.add(three);

		return  list;


	}


	private Bitmap getCircleBitmap(Bitmap bitmap) {
		final Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Bitmap.Config.ARGB_8888);
		final Canvas canvas = new Canvas(output);

		final int color = Color.RED;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawOval(rectF, paint);

		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		bitmap.recycle();

		return output;
	}

	/**
	 *
	 * @param colorCode
	 * @return
	 */

	public static Drawable getCircleBitmapByColorCode(String colorCode) {
		int size = 60;
		String txt = "HI";

		ShapeDrawable biggerCircle = new ShapeDrawable(new OvalShape());
		biggerCircle.setIntrinsicHeight(size);
		biggerCircle.setIntrinsicWidth(size);
		biggerCircle.setBounds(new Rect(0, 0, size, 60));
		biggerCircle.getPaint().setColor(Color.parseColor(colorCode));

		return biggerCircle;
	}

	public static String getColorCodes() {
		// String[] lis = new String[] { "#F44336", "#E91E63", "#9C27B0",
		// "#673AB7", "#3F51B5", "#2196F3", "#03A9F4", "#00BCD4",
		// "#009688", "#4CAF50", "#8BC34A", "#CDDC39", "#FFC107",
		// "#FF9800", "#FF5722", "#795548","#9E9E9E","#607D8B" };

		String[] lis = new String[] { "#e91e63", "#9c27b0", "#3f51b5",
				"#0054ff", "#009688", "#80b60a", "#d81a60", "#c1ca33",
				"#ff9800" };

		return (lis[new Random().nextInt(lis.length)]);

	}





	/**
	 *
	 * @param param
	 * @return encoded string
	 */
	public static String urlEncode(String param) {
		String encodedString = param;
		try {
			encodedString = URLEncoder.encode(param, "UTF-8");
		} catch (Exception e) {
			// TODO: handle exception
		}

		return encodedString;
	}

	/**
	 *
	 * @param param
	 * @return decodedString
	 */

	public static String urlDecode(String param) {
		String decodedString = param;
		try {
			decodedString = URLDecoder.decode(param, "UTF-8");
		} catch (Exception e) {
			// TODO: handle exception
		}

		return decodedString;
	}

	public static Drawable getCircleWithText(String text) {
		Bitmap canvasBitmap = Bitmap.createBitmap(350, 350,
				Bitmap.Config.ARGB_8888);
		BitmapShader shader = new BitmapShader(canvasBitmap, TileMode.CLAMP,
				TileMode.CLAMP);
		Paint paint;
		Paint circlePaint;

		paint = new Paint();
		circlePaint = new Paint();

		paint.setColor(Color.WHITE);
		paint.setTextSize(12f);
		paint.setAntiAlias(true);
		paint.setTextAlign(Paint.Align.CENTER);
		Canvas canvas = new Canvas(canvasBitmap);
		Rect bounds = new Rect();
		paint.getTextBounds(text, 0, text.length(), bounds);

		circlePaint.setColor(Color.GRAY);
		circlePaint.setAntiAlias(true);

		canvas.drawCircle(-3, 15 - (bounds.height() / 2), bounds.width() + 5,
				circlePaint);

		canvas.drawText(text, -3, 15, paint);
		Drawable d = new BitmapDrawable(canvasBitmap);
		return d;
	}

	public static boolean isDeviceOSaboveLollipop(){

		try{


			if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
				return true;

			}else{
				return false;
			}

		}catch (Exception e){
			e.printStackTrace();
		}


	return false;
	}

	/**
	 * Return date in specified format.
	 * @param milliSeconds Date in milliseconds
	 * @param dateFormat Date format
	 * @return String representing date in specified format
	 */
	public static String getDate(long milliSeconds, String dateFormat)
	{
		SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(milliSeconds);
		return formatter.format(calendar.getTime());
	}



}

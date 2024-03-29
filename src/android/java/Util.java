package cn.wps.moffice.demo.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import org.apache.http.util.EncodingUtils;

import java.io.File;
import java.io.InputStream;

public class Util
{
	private static SettingPreference settingPreference;

  	// 检测该包名所对应的应用是否存在
  	public static boolean checkPackage(Context context, String packageName) 
  	{  
	    if (packageName == null || "".equals(packageName))  
	        return false;  
	    
	    try 
	    {  
	    	context.getPackageManager().getApplicationInfo(packageName, PackageManager.GET_UNINSTALLED_PACKAGES);  
	        return true;  
	    } 
	    catch (NameNotFoundException e) 
	    {  
	        return false;  
	    }  
	}  
  	
  	public static void showToast(Context context, String content)
  	{
  		showToast(context, content, Toast.LENGTH_LONG);
  	}
  	
  	public static void showToast(final Context context, final String content, final int length_short)
  	{
  		Handler handle = new Handler(Looper.getMainLooper());
  		handle.post(new Runnable()
  		{
			@Override
			public void run()
			{
				Toast.makeText(context, content, length_short).show();
			}
  		});
  		
  	}

  	//获得文档打开需要设置的参数，参数都是由settingPreference存放的
  	public static Intent getOpenIntent(Context context, String path, boolean isAIDL)
  	{
  		 settingPreference = new SettingPreference(context);
  		
  		 //获得上次打开的文件信息
  		String 	closeFilePath 		= settingPreference.getSettingParam(Define.CLOSE_FILE, "null");  
  		String 	packageName   		= settingPreference.getSettingParam(Define.THIRD_PACKAGE, context.getPackageName());
  		float 		ViewProgress 	= settingPreference.getSettingParam(Define.VIEW_PROGRESS, (float)0.0);
  	    float 		ViewScale 		= settingPreference.getSettingParam(Define.VIEW_SCALE, (float)0.0);
  	    int 		ViewScrollX 	= settingPreference.getSettingParam(Define.VIEW_SCROLL_X, 0);
  	    int 		ViewScrollY 	= settingPreference.getSettingParam(Define.VIEW_SCROLL_Y ,0);
  		String 	userName        	= settingPreference.getSettingParam(Define.USER_NAME, "newUser");
  		
  		 //获取用户设置的参数信息
	    String		OpenMode		= settingPreference.getSettingParam(Define.OPEN_MODE, null);
	    boolean   	SendSaveBroad   = settingPreference.getSettingParam(Define.SEND_SAVE_BROAD, false);
	    boolean   	SendCloseBroad  = settingPreference.getSettingParam(Define.SEND_CLOSE_BROAD, true);
	    boolean   	SendBackBroad  = settingPreference.getSettingParam(Define.BACK_KEY_DOWN, false);
	    boolean   	SendHomeBroad  = settingPreference.getSettingParam(Define.HOME_KEY_DOWN, true);
	    boolean   	IsIsClearBuffer = settingPreference.getSettingParam(Define.IS_CLEAR_BUFFER, false);
	    boolean   	IsClearTrace 	= settingPreference.getSettingParam(Define.IS_CLEAR_TRACE, false);
	    boolean   	IsClearFile 	= settingPreference.getSettingParam(Define.IS_CLEAR_FILE, false);
	    boolean  	IsViewScale     = settingPreference.getSettingParam(Define.IS_VIEW_SCALE ,false);
	    boolean   	AutoJump		= settingPreference.getSettingParam(Define.AUTO_JUMP, false);
	    boolean   	EnterReviseMode = settingPreference.getSettingParam(Define.ENTER_REVISE_MODE, true);
	    boolean   	CacheFileVisible = settingPreference.getSettingParam(Define.CACHE_FILE_INVISIBLE, false);

		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		//打开文档参数
//		bundle.putString(Define.OPEN_MODE, OpenMode);			     //打开模式
//		bundle.putString(Define.OPEN_MODE, "ReadOnly");			     //只读模式
//		bundle.putString(Define.OPEN_MODE, "Normal");			     //正常模式
//		bundle.putString(Define.OPEN_MODE, "ReadMode");			     //打开直接进入阅读模式
		bundle.putString(Define.OPEN_MODE, "EditMode");			     //打开直接进入编辑模式

		//广播相关参数
		bundle.putBoolean(Define.SEND_SAVE_BROAD, SendSaveBroad);    //保存文件的广播boolean
		bundle.putBoolean(Define.SEND_CLOSE_BROAD, SendCloseBroad);	 //关闭文件的广播boolean
		bundle.putBoolean(Define.BACK_KEY_DOWN, SendBackBroad);		//监听back键并发广播boolean
		bundle.putBoolean(Define.HOME_KEY_DOWN, SendHomeBroad);		//监听home键并发广播boolean
//		bundle.putString(Define.THIRD_PACKAGE, packageName);    //第三方的包名，关闭的广播会包含该项。

		//文档记录
//		bundle.putBoolean(Define.CLEAR_BUFFER, IsIsClearBuffer);	 //清除临时文件boolean
		bundle.putBoolean(Define.CLEAR_TRACE, true);		 //清除使用记录boolean
//		bundle.putBoolean(Define.CLEAR_FILE, IsClearFile);           //删除打开文件boolean
//		bundle.putBoolean(Define.CACHE_FILE_INVISIBLE, !CacheFileVisible);    //WPS生成的缓存文档是否可见boolean

		//文档初始化参数
//		bundle.putBoolean(Define.AUTO_JUMP, AutoJump);				//自动跳转到上次查看的进度boolean，包括页数和xy坐标
//		bundle.putString(Define.WATERMASK_TEXT, "1233211233211233211123321");		//设置文档水印内容
//		bundle.putInt(Define.WATERMASK_COLOR, Color.BLUE);		//设置文档水印颜色
// 		bundle.putBoolean(Define.IS_SCREEN_SHOTFORBID, true);		// 是否禁止截屏
		bundle.putBoolean(Define.AUTO_PLAY, true);		//打开演示文档进入自动播放模式
		bundle.putBoolean("PagePlay",true);
		bundle.putInt(Define.AUTO_PLAY_INTERNAL, 300);	//演示文档自动播放设置时间间隔
//		bundle.putInt(Define.SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);	//文档打开方向
//		bundle.putFloattrue(Define.ZOOM_OTHER,(float)4.25 );		//打开文档默认缩放比例（仅针对doc文字组件文档）

		//修订相关参数
		bundle.putBoolean(Define.SHOW_REVIEWING_PANE_RIGHT_DEFAULT, false); 		// 打开文档是否显示修订批注面板
		bundle.putBoolean(Define.ENTER_REVISE_MODE, true);    //修订模式打开文档
//		bundle.putBoolean(Define.REVISION_NOMARKUP, true);		//修订无水印

		//批注
//		bundle.putString(Define.USER_NAME, userName);    //批注作者

		//激活相关
//		bundle.putString(Define.SERIAL_NUMBER_OTHER, "909290816199");
//		bundle.putString(Define.SERIAL_NUMBER_OTHERPC, "H8F6R-AFMPQ-6QQCU-BQ67X-AC2NE");

		//其他
//		bundle.putString(Define.SAVE_PATH, "/sdcard/SavePath另存文档.doc");		//文件保存路径
//		bundle.putString(Define.DISPLAY_OPEN_FILE_NAME, "Test.doc"); // 自定义菜单显示名称
		bundle.putBoolean("DisplayView", true);    //是否显示wps界面
//		bundle.putString(Define.MENU_XML, Util.assetsFileRead("menu.xml" , context));		//自定义菜单功能

		//讯飞参数
//		bundle.putBoolean(Define.BROAD_WPS_ONPAUSE, true);
//		bundle.putBoolean(Define.BROAD_WPS_ONRESUME, true);
//		bundle.putBoolean(Define.BROAD_WPS_VIEWMODE, true);
//		bundle.putBoolean(Define.BROAD_WPS_VIEWMODE_PLAY, true);

//		华为参数
		bundle.putBoolean("huawei_print_enable",true);

		if (path.equals(closeFilePath))						       //如果打开的文档时上次关闭的
		{
			if (IsViewScale)
				bundle.putFloat(Define.VIEW_SCALE, ViewScale);				//视图比例
			if (AutoJump)
			{
				bundle.putFloat(Define.VIEW_PROGRESS, ViewProgress);		//阅读进度
				bundle.putInt(Define.VIEW_SCROLL_X, ViewScrollX);			//x
				bundle.putInt(Define.VIEW_SCROLL_Y, ViewScrollY);			//y
			}
		}
		
		if (isAIDL)
			bundle.putBoolean(Define.SEND_CLOSE_BROAD, true);	 //aidl打开需要发送关闭广播，这样关闭文档时候才能把浮窗关闭
		
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(android.content.Intent.ACTION_VIEW);
		if (checkPackage(context, Define.PACKAGENAME_KING_PRO))
		{
			intent.setClassName(Define.PACKAGENAME_KING_PRO, Define.CLASSNAME);
		}
		else if (checkPackage(context, Define.PACKAGENAME_PRO_DEBUG))
		{
			intent.setClassName(Define.PACKAGENAME_PRO_DEBUG, Define.CLASSNAME);
		}
		else if (checkPackage(context, Define.PACKAGENAME_ENG))
		{
			intent.setClassName(Define.PACKAGENAME_ENG, Define.CLASSNAME);
		}
		else if (checkPackage(context, Define.PACKAGENAME_KING_PRO_HW))
		{
			intent.setClassName(Define.PACKAGENAME_KING_PRO_HW, Define.CLASSNAME);
		}
		else if (checkPackage(context, Define.PACKAGENAME_K_ENG))
		{
			intent.setClassName(Define.PACKAGENAME_K_ENG, Define.CLASSNAME);
		}
		else
		{
			showToast(context, "文件打开失败，请安装WPS Office专业版");
			return null;
		}
		
		File file = new File(path);
		if (file == null || !file.exists())
		{			
			showToast(context, "打开失败，文件不存在！");
			return null;
		}
		
		Uri uri = Uri.fromFile(file);
		intent.setData(uri);
		intent.putExtras(bundle);
        String type = Util.getMIMEType(file);  
        intent.setDataAndType(Uri.fromFile(file), type); 
        
  		settingPreference = null;
  		return intent;
  	}
  	
  	public static Intent getPDFOpenIntent(Context context, String path, boolean isAIDL)
  	{
  		Intent intent = new Intent();
		Bundle bundle = new Bundle();

		settingPreference = new SettingPreference(context);
		
	    String 	packageName   	= settingPreference.getSettingParam(Define.THIRD_PACKAGE,  context.getPackageName());
	    boolean FairCopy        = settingPreference.getSettingParam(Define.FAIR_COPY, true);
	    String userName         = settingPreference.getSettingParam(Define.USER_NAME, "");
	    boolean   	CacheFileInvisible = settingPreference.getSettingParam(Define.CACHE_FILE_INVISIBLE, false);
	
	    bundle.putString(Define.USER_NAME, userName);	
		bundle.putBoolean(Define.SEND_CLOSE_BROAD, true);       //关闭文件的广播,由于demo的浮窗需要根据关闭广播来关闭，请设置该值为true
		bundle.putBoolean(Define.FAIR_COPY, FairCopy);
		bundle.putString(Define.USER_NAME,userName);
		bundle.putString(Define.THIRD_PACKAGE, packageName);
		bundle.putBoolean(Define.BACK_KEY_DOWN, settingPreference.getSettingParam(Define.BACK_KEY_DOWN, true));
		bundle.putBoolean(Define.HOME_KEY_DOWN, settingPreference.getSettingParam(Define.HOME_KEY_DOWN, true));
		bundle.putBoolean(Define.CACHE_FILE_INVISIBLE, CacheFileInvisible);    //
//		bundle.putString(Define.WATERMASK_TEXT, "Test");
//		bundle.putInt(Define.WATERMASK_COLOR, Color.RED);
//		bundle.putBoolean("huawei_print_enable",true);


		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(android.content.Intent.ACTION_VIEW);


		if (checkPackage(context, Define.PACKAGENAME_KING_PRO))
		{
			intent.setClassName(Define.PACKAGENAME_KING_PRO, Define.CLASSNAME);
		}
		else if (checkPackage(context, Define.PACKAGENAME_KING_PRO_HW))
		{
			intent.setClassName(Define.PACKAGENAME_KING_PRO_HW, Define.CLASSNAME);
		}
		else
		{
			showToast(context, "文件打开失败，移动WPS专业版未安装");
			return null;
		}
		
		File file = new File(path);
		if (file == null || !file.exists())
		{			
			showToast(context, "打开失败，文件不存在！");
			return null;
		}
		
		Uri uri = Uri.fromFile(file);
		intent.setData(uri);
		intent.putExtras(bundle);
  		
  		if (isAIDL)
			bundle.putBoolean(Define.SEND_CLOSE_BROAD, true);	 //aidl打开需要发送关闭广播，这样关闭文档时候才能把浮窗关闭
  		
  		return intent;
  	}
  	
    public static String getMIMEType(File f)
    {  
        String end = f.getName().substring(f.getName().lastIndexOf(".") + 1,  
                		f.getName().length()).toLowerCase();  
        String type = "";  
        if (end.equals("mp3") || end.equals("aac") || end.equals("aac") 
        		|| end.equals("amr") || end.equals("mpeg") || end.equals("mp4"))  
        {  
          type = "audio";  
        }
        else if (end.equals("jpg") || end.equals("gif") || end.equals("png") || end.equals("jpeg"))  
        {  
          type = "image";  
        } 
        else if (end.equals("doc") || end.equals("docx") || end.equals("pdf")
        		|| end.equals("txt"))
        {  
          type = "application/msword";  
          return type;
        }  
        else
        {
        	type = "*";
        }
        type += "/*";  
        return type;  
    } 
    
	/**
  	 * 判断是否是wps能打开的文件
  	 * @param file
  	 * @return
  	 */
  	public static boolean IsWPSFile(File file)
  	{
  		String end = file.getName().substring(file.getName().lastIndexOf(".") + 1,  
        		file.getName().length()).toLowerCase();  
  		if (end.equals("doc") || end.equals("dot") || end.equals("wps") 
  			|| end.equals("wpt") || end.equals("docx") || end.equals("dotx")
  			|| end.equals("docm") || end.equals("dotm") || end.equals("rtf") 
			|| end.equals("xls") || end.equals("xlt") || end.equals("et")
			|| end.equals("ett") || end.equals("xlsx") || end.equals("xltx")
			|| end.equals("csv") || end.equals("xlsb") || end.equals("xlsm")
			|| end.equals("xml") || end.equals("html") || end.equals("htm")
			|| end.equals("ppt") || end.equals("pptx") || end.equals("dps")
			|| end.equals("pot") || end.equals("pps") || end.equals("dpt")
			|| end.equals("potx") || end.equals("ppsx") || end.equals("pptm")
			|| end.equals("txt") || end.equals("pdf") 
			|| end.equals("potm") || end.equals("ppsm"))
  			return true;
  		
  		return true;
  	}
  	
  	public static boolean isPDFFile(String filePath)
  	{
  		String path = filePath.toLowerCase();
		return path.endsWith(".pdf");
  	}
  	
  	public static boolean isPptFile(String filePath) {
  		filePath = filePath.toLowerCase();
  		return filePath.endsWith(".ppt")
  				|| filePath.endsWith("pptx");
  	}
  	
  	public static boolean isExcelFile(String filePath) {
  		filePath = filePath.toLowerCase();
  		return filePath.endsWith(".xls")
  				|| filePath.endsWith(".xlsx")
  				||filePath.endsWith(".et");
  	}

	public static boolean isImagelFile(String filePath) {
		filePath = filePath.toLowerCase();
		return filePath.endsWith(".jpg")
				|| filePath.endsWith(".jpeg")
				|| filePath.endsWith(".jpe")
				|| filePath.endsWith(".jpeg");
	}
  	
	private static String assetsFileRead(String fileName, Context context) {
		String ret = "";
		try {
			InputStream is = context.getAssets().open(fileName);
			int len = is.available();
			byte[] buffer = new byte[len];
			is.read(buffer);
			ret = EncodingUtils.getString(buffer, "utf-8");
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}
}

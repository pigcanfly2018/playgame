package bsz.exch.utils;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;




public class DateUtil {
	
	
	
	private static SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyyMMdd");
	private static SimpleDateFormat yyyyMM = new SimpleDateFormat("yyyyMM");
	//private static SimpleDateFormat yyyy-MMdd = new SimpleDateFormat("yyyyMMdd");
	private static SimpleDateFormat HHmmss = new SimpleDateFormat("HH:mm:ss");
	private static SimpleDateFormat yyyy_MM_ddhhmmss = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static SimpleDateFormat yyyyMMddhhmmss = new SimpleDateFormat("yyyyMMddHHmmss");
	private static SimpleDateFormat yyyy_MM_dd = new SimpleDateFormat("yyyy-MM-dd");

	/**
	 * 
	 * @param dateStr
	 *            日期类型的字符串
	 * @param format
	 *            指明dateStr的格式
	 * @return java.sql.Date
	 */
	public static Date stringToDate(String dateStr, String format) {
		java.util.Date date = null;
		try {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
			date = simpleDateFormat.parse(dateStr);
		} catch (Exception ex) {
			// ex.printStackTrace();
			return null;
		}
		return new java.sql.Date(date.getTime());
	}

	/**
	 * 
	 * @param dateStr
	 *            日期类型的字符串
	 * @param format
	 *            指明dateStr的格式数组
	 * @return java.sql.Date
	 */
	public static Date stringToDate(String dateStr, String[] formats) {

		java.util.Date date = null;
		SimpleDateFormat simpleDateFormat = null;

		for (int i = 0; i < formats.length; i++) {
			simpleDateFormat = new SimpleDateFormat(formats[i]);
			try {
				date = simpleDateFormat.parse(dateStr);
				break;
			} catch (Exception ex) {
				continue;
			}
		}
		return date != null ? new java.sql.Date(date.getTime()) : null;
	}
	
	public static String fmtyyyyMMdd(java.util.Date date) {
		return yyyyMMdd.format(date);
	}
	
	public static String fmtyyyyMM(java.util.Date date) {
		return yyyyMM.format(date);
	}
	
	
	public static String fmtyyyy_MM_dd(java.util.Date date) {
		return yyyy_MM_dd.format(date);
	}

	/**
	 * 
	 * @param date
	 *            转化的日期
	 * @param format转换的格式
	 * @return 格式化的字符串
	 */
	public static String dateToString(java.util.Date date, String format) {

		if (date == null)
			return null;
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		String str = sdf.format(date);
		return str;
	}

	/**
	 * 
	 * @param timestampStr
	 *            日期类型的字符串
	 * @param format
	 *            指明timestampStr的格式
	 * @return java.sql.Timestamp
	 */
	public static Timestamp stringToTimestamp(String timestampStr, String format) {
		if (format == null) {
			format = "yy-MM-dd";
		}

		try {

			return new java.sql.Timestamp(stringToDate(timestampStr, format)
					.getTime());
		} catch (Exception e) {

			return null;
		}

	}
	
	
	
	/**
	 * 
	 * @param timestampStr
	 *            日期类型的字符串
	 * @param format
	 *            指明timestampStr的格式
	 * @return java.sql.Timestamp
	 */
	public static Timestamp useastDatestringToTimestamp(String timestampStr, String format) {
		if (format == null) {
			format = "yy-MM-dd";
		}

		try {

			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(stringToDate(timestampStr, format).getTime()+43200000L);
			return new java.sql.Timestamp(cal.getTimeInMillis());
		} catch (Exception e) {

			return null;
		}

	}
	

	/**
	 * 
	 * @param timestamp
	 *            转化的日期
	 * @param format转换的格式
	 * @return 格式化的字符串
	 */
	public static String timestampToString(Timestamp timestamp, String format) {
		try{
		return dateToString(new java.util.Date(timestamp.getTime()), format);
		}catch(Exception e){
			
			return null;
		}
	}

	/**
	 * 
	 * @param timestamp
	 *            转化的日期
	 * @param format转换的格式
	 * @return 格式化的字符串
	 */
	public static String DateToString(Date date, String format) {
		if (date == null) {
			return null;
		}
		return dateToString(new java.util.Date(date.getTime()), format);
	}
	
	public static String getDateString(){
		return DateToString(new java.sql.Date(System.currentTimeMillis()),"yyyy-MM-dd HH:mm:ss");
	}
	public static String getDateString(String format){
		return DateToString(new java.sql.Date(System.currentTimeMillis()),format);
	}

	
	/**
	 * 
	 * @param timeStr
	 *            时间类型的字符串
	 * @param format
	 *            指明timeStr的格式
	 * @return java.sql.Time
	 */
	public static Time stringToTime(String timeStr, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Time time = null;
		try {
			time = new Time((sdf.parse(timeStr)).getTime());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return time;
	}

	/**
	 * 
	 * @param time
	 *            转化的时间
	 * @param format转换的格式
	 * @return 格式化的字符串
	 */
	public static String timeToString(Time time, String format) {
		SimpleDateFormat sdftime = new SimpleDateFormat(format);
		return sdftime.format(time);
	}

	public synchronized static boolean isTimestamp(String key) {

		boolean flag = true;
		//java.util.Date date = null;
		try {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			simpleDateFormat.setLenient(false);
			simpleDateFormat.parse(key);
		} catch (Exception ex) {
			ex.printStackTrace();
			flag = false;
		}

		return flag;

	}

	public synchronized static boolean isDate(String key) {

		boolean flag = true;
		//java.util.Date date = null;
		try {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
					"yyyy-MM-dd");
			simpleDateFormat.setLenient(false);
			simpleDateFormat.parse(key);
		} catch (Exception ex) {
			ex.printStackTrace();
			flag = false;
		}

		return flag;

	}
	/**
	 * 比较两个日期的大小
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static int compareDate(Date date1, Date date2) {

		if (date1 == null) {

			return -1;

		}
		if (date2 == null) {

			return 1;
		}

		if (date1.getTime() == date2.getTime()) {

			return 0;

		}

		if (date1.getTime() > date2.getTime()) {

			return 1;
		} else {

			return -1;
		}

	}

	/**
	 * 比较两个日期的大小
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static int compareTimestamp(Timestamp date1, Timestamp date2) {

		if (date1 == null) {

			return -1;

		}
		if (date2 == null) {

			return 1;
		}

		if (date1.getTime() == date2.getTime()) {

			return 0;

		}

		if (date1.getTime() > date2.getTime()) {

			return 1;
		} else {

			return -1;
		}

	}

	/**
	 * 返回两个时间之差
	 * 
	 * @param arg
	 */

	public static long betweenDate(Date startDate, Date endDate) {

		if (startDate == null)
			return 0;

		if (endDate == null)
			return 0;

		return endDate.getTime() - startDate.getTime();
	}

	/**
	 * 两种date的转换 sql-->util
	 * 
	 * @param date
	 * @return
	 */
	public static java.util.Date convertUtilDate(Date date) {

		if (date == null) {
			return null;
		} else {

			return new java.util.Date(date.getTime());
		}

	}

	/**
	 * 两种date的转换 util-->sql
	 * 
	 * @param date
	 * @return
	 */
	public static Date convertSqlDate(java.util.Date date) {

		if (date == null) {
			return null;
		} else {

			return new Date(date.getTime());
		}

	}
	
	
	public static boolean bt(String startTime,String endTime){
		if(startTime==null||endTime==null){
			return false;
		}
		long now = System.currentTimeMillis();
		String date00 = DateUtil.dateToString(new Date(now), "yyyy-MM-dd");
		String date11 = date00 + " "+startTime;
		String date12 = date00 + " "+endTime;
		Date date21 = DateUtil.stringToDate(date11, "yyyy-MM-dd HH:mm:ss");
		Date date22 = DateUtil.stringToDate(date12, "yyyy-MM-dd HH:mm:ss");
		if (now > date21.getTime() && now < date22.getTime()) {
			return true;
		}
		return false;
	}

	public static java.util.Date nextMonth(java.util.Date date){
		
		Calendar calendar = Calendar.getInstance();//日历对象

		calendar.setTime(date);//设置当前日期

		calendar.add(Calendar.MONTH, 1);//月份减一
		
		return calendar.getTime();
	}
	
	public static String nextFifteenMinute(String time){
		try {  
             //HHmmss.parse(time);  
             Calendar cal = Calendar.getInstance();
             //System.out.println("time    "+time);
             cal.setTime(yyyy_MM_ddhhmmss.parse(time));
             cal.add(Calendar.MINUTE, 15);
             //System.out.println(cal.getTime());
             String hour=cal.get(Calendar.HOUR_OF_DAY)>10?String.valueOf(cal.get(Calendar.HOUR_OF_DAY)):("0"+cal.get(Calendar.HOUR_OF_DAY));
             String minute = cal.get(Calendar.MINUTE)>10?String.valueOf(cal.get(Calendar.MINUTE)):("0"+cal.get(Calendar.MINUTE));
             return hour+":"+minute+":00";
        } catch (Exception ex) {  
            ex.printStackTrace();  
        } 
		return null;
	}
	
	public static String nextDay(String date){
		try {  
			//yyyy_MM_dd.parse(date);  
             //System.out.println("front:" + HHmmss.parse(time));
             Calendar cal = Calendar.getInstance();
             cal.setTime(yyyy_MM_ddhhmmss.parse(date));
             cal.add(Calendar.DATE, 1);
             System.out.println(cal.getTime());
             
             return cal.get(Calendar.YEAR)+"-"+(cal.get(Calendar.MONTH)+1)+"-"+cal.get(Calendar.DATE);
        } catch (Exception ex) {  
            ex.printStackTrace();  
        } 
		return null;
	}
	
	public static String lastDay(String date){
		try {  
			//yyyy_MM_dd.parse(date);  
             //System.out.println("front:" + HHmmss.parse(time));
             Calendar cal = Calendar.getInstance();
             cal.setTime(yyyy_MM_ddhhmmss.parse(date));
             cal.add(Calendar.DATE, -1);
            // System.out.println(cal.getTime());
             SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy",Locale.US);
             java.util.Date d=sdf.parse(cal.getTime().toString());
             sdf=new SimpleDateFormat("yyyyMMdd");
             return sdf.format(d);
        } catch (Exception ex) {  
            ex.printStackTrace();  
        } 
		return null;
	}
	
	public static String getDate(){
		try{
			Calendar cal = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy",Locale.US);
            java.util.Date d=sdf.parse(cal.getTime().toString());
            sdf=new SimpleDateFormat("yyyyMMdd");
            return sdf.format(d);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	public static String getOrderTime(){
		try{
			Calendar cal = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy",Locale.US);
            java.util.Date d=sdf.parse(cal.getTime().toString());
            return yyyy_MM_ddhhmmss.format(d);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	public static String getOrderTimeyyyyMMddhhmmss(){
		try{
			Calendar cal = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy",Locale.US);
            java.util.Date d=sdf.parse(cal.getTime().toString());
            return yyyyMMddhhmmss.format(d);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	public static String getYesterdayDate(){
		try{
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DATE, -1);
			SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy",Locale.US);
            java.util.Date d=sdf.parse(cal.getTime().toString());
            sdf=new SimpleDateFormat("yyyyMMdd");
            return sdf.format(d);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	public static String getTime(){
		try{
			Calendar cal = Calendar.getInstance();
			 String hour=cal.get(Calendar.HOUR_OF_DAY)>=10?String.valueOf(cal.get(Calendar.HOUR_OF_DAY)):("0"+cal.get(Calendar.HOUR_OF_DAY));
             String minute = cal.get(Calendar.MINUTE)>=10?String.valueOf(cal.get(Calendar.MINUTE)):("0"+cal.get(Calendar.MINUTE));
             return hour+":"+minute+":00";
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	
	public static String getUsEastTime(){
		try{
			long time = new java.util.Date().getTime() - 43200000L;
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(time);
			 String hour=cal.get(Calendar.HOUR_OF_DAY)>=10?String.valueOf(cal.get(Calendar.HOUR_OF_DAY)):("0"+cal.get(Calendar.HOUR_OF_DAY));
             String minute = cal.get(Calendar.MINUTE)>=10?String.valueOf(cal.get(Calendar.MINUTE)):("0"+cal.get(Calendar.MINUTE));
             return hour+":"+minute+":00";
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	public static String csttonormal(String date){
		try{
			SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
			java.util.Date d=sdf.parse(date);
			String formatStr2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
			return formatStr2;
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return null;
	}
	
	
	public static String getUsEastDate() {
		long time = new java.util.Date().getTime() - 43200000L;
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(time);
		return DateUtil.fmtyyyyMMdd(calendar.getTime());
	}
	
	public static String getCurrentyyyy_mm_ddDate() {
		long time = new java.util.Date().getTime();
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(time);
		return DateUtil.fmtyyyy_MM_dd(calendar.getTime());
	}
	
	public static String getUsEastYesterDayDate() {
		long time = new java.util.Date().getTime() - 43200000L-86400000;
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(time);
		return DateUtil.fmtyyyyMMdd(calendar.getTime());
	}
	
	public static String getTodaySuffix() {
		long time = new java.util.Date().getTime();
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(time);
		return DateUtil.fmtyyyyMM(calendar.getTime());
	}
	
	public static String getUsEastYesterdaySuffix() {
		long time = new java.util.Date().getTime() - 43200000L-86400000;
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(time);
		return DateUtil.fmtyyyyMM(calendar.getTime());
	}
	
	
	public static String getUsEastSuffix() {
		long time = new java.util.Date().getTime() - 43200000L;
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(time);
		return DateUtil.fmtyyyyMM(calendar.getTime());
	}
	
	
	public static String getSuffixFromDate(java.util.Date date) {
		long time = date.getTime();
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(time);
		return DateUtil.fmtyyyyMM(calendar.getTime());
	}
	
	
	public static String getSuffixFromNextMonth() {
		
		Calendar calendar = Calendar.getInstance();
		calendar.add(calendar.MONTH, 1);;
		return DateUtil.fmtyyyyMM(calendar.getTime());
	}
	
	
	
	

	public static void main(String[] arg) {
		//System.out.println(DateUtil.lastDay("2015-05-10 13:28:39"));
		//System.out.println(DateUtil.nextFifteenMinute("2015-05-10 23:00:00"));
		System.out.println(DateUtil.getOrderTime());
		System.out.println(DateUtil.getSuffixFromNextMonth());
		System.out.println(DateUtil.useastDatestringToTimestamp("2015-05-28 22:39:50","yyyy-MM-dd HH:mm:ss"));
		//System.out.println(DateUtil.fmtyyyyMM(new java.util.Date()));
//		String dateStr = "2010515";
//		Date endDate = DateUtil.stringToDate(dateStr, "yyyyMMdd");
//		System.out.println(endDate);
		// // 字符串转化java.sql.Date类型
		// String dateStr = "1988年12月25日";
		// String formate = "yyyy年MM月dd日";
		// Date date = stringToDate(dateStr, formate);
		// System.out.println(date.toString());
		//
		// // java.util.Date转换为字符串
		// java.util.Date uDate = new java.util.Date(date.getTime());
		// String uDateStr = dateToString(uDate, formate);
		// System.out.println(uDateStr);
		//
		// // 字符串转化为java.sql.Timestamp类型
		// String timestampStr = "2010-03-22";
		// formate = "yy-MM-dd";
		// Timestamp timestamp = stringToTimestamp(timestampStr, formate);
		// System.out.println(timestamp.toString());
		//
		// // java.sql.Timestamp转换为字符串
		//
		// String timestampS = timestampToString(timestamp, formate);
		// System.out.println(timestampS);
		//
		// // 字符串转化为java.sql.Time
		// String timeStr = "12点13分14秒";
		// formate = "HH点mm分ss秒";
		// Time time = stringToTime(timeStr, formate);
		// System.out.println(time);
		//
		// // java.sql.Time转换为字符串
		//
		// System.out.println(timeToString(time, formate));
		//
		// String temp = "11/02/2010";
		// if (temp.contains("/")) {
		// temp = temp.substring(6, 10) + "-" + temp.substring(3, 5) + "-"
		// + temp.substring(0, 2);
		// System.out.println(temp);
		// }

//		String[] str = new String[] { "yyyy-mm-dd", "MM/dd/yyyy", "yyyyMMdd" };
//
//		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
//		try {
//			java.util.Date date = simpleDateFormat.parse("1999-11-02");
//			System.out.println(date.toLocaleString());
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}

}

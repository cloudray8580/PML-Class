package com.example.timemanager.tools;

public class DATE {
		private int year_;
		private int month_;
		private int day_;
		private int hour_;
		private int minute_;
		private int second_;
        
		public int getY(){return year_;}
		public int getM(){return month_;}
		public int getD(){return day_;}
		public int getH(){return hour_;}
		public int getMin(){return minute_;}
		public int getS(){return second_;}
		
		public void setY(int year){year_ = year;}
		public void setM(int month){month_ = month;}
		public void setD(int day){day_ = day;}
		public void setH(int hour){hour_ = hour;}
		public void setMin(int minute){minute_ = minute;}
		public void setS(int second){second_ = second;}
		
		public void StringToDate(String timestamp) {
			String records[] = timestamp.split("\\D");
			int year = Integer.parseInt(records[ 0 ]);
			int month = Integer.parseInt(records[ 1 ]);
			int day = Integer.parseInt(records[ 2 ]);
			int hour = Integer.parseInt(records[ 3 ]);
			int minute = Integer.parseInt(records[ 4 ]);
			int second = Integer.parseInt(records[ 5 ]);
			
			setY(year);
			setM(month);
			setD(day);
			setH(hour);
			setMin(minute);
			setS(second);
		}
		
		public DATE sub(DATE other){
			DATE result = new DATE();
			result.setY(year_ - other.getY());
			result.setM(month_ - other.getM());
			result.setD(day_ - other.getD());
			result.setH(hour_ - other.getH());
			result.setMin(minute_ - other.getMin());
			result.setS(second_ - other.getS());
			
			return result;
		}
		
		public double interval() {
			return year_* 360 * 24 * 60 + month_ * 30 * 24 * 60 + day_ * 24 * 60 +  hour_ * 60 + minute_  + (double)second_ * 1 / 60;
		}
		
		
}

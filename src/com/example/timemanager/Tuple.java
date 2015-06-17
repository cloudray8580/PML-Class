package com.example.timemanager.tools;

public class Tuple {
  private int hour_;
  private int minute_;
  private int second_;
  private int action_;
  
  public Tuple(int H, int min, int second, int A) {
	  setH(H);
	  setMin(min);
	  setS(second);
	  setA(A);
  }
  public Tuple(){}
  
  public int getH(){return hour_;}
  public int getMin(){return minute_;}
  public int getS(){return second_;}
  public int getA(){return action_;}
  
  public void setH(int hour){hour_ = hour;}
  public void setMin(int minute){minute_ = minute;}
  public void setS(int second){second_ = second;}
  public void setA(int action){action_ = action;}
  public Tuple sub(Tuple other){
		Tuple result = new Tuple();
		//result.setY(year_ - other.getY());
		//result.setM(month_ - other.getM());
		//result.setD(day_ - other.getD());
		result.setH(hour_ - other.getH());
		result.setMin(minute_ - other.getMin());
		result.setS(second_ - other.getS());
		
		return result;
	}
	
	public double interval() {
		return   hour_ * 60 + minute_  + (double)second_ * 1 / 60;
	}
  public String toString(){return hour_ + " " + minute_ + " "  + second_ +" " + action_;}

}

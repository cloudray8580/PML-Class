package com.example.timemanager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.example.timemanager.tools.TimeManager;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.view.LineChartView;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

/**
 * @author sony-pc
 * 绘制图表Activity
 * 参考 https://github.com/lecho/hellocharts-android/blob/master/hellocharts-samples/src/lecho/lib/hellocharts/samples/ComboLineColumnChartActivity.java
 * 的使用例子
 */

// ！！！！！！！！！！！！！！！！！！！！！！测试状态！！！！！！！！！！！！！！！！！！！！！！！！！！！！！
public class ChartActivity extends Activity {

	LineChartView chart;
	TimeManager timeManager;
	int taskType;
	
	public void findView() {
		chart = (LineChartView)findViewById(R.id.chart);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chart);
		Intent intent = getIntent();
		taskType = intent.getIntExtra("TaskType", 1);
		findView();
		timeManager = new TimeManager(this); // 调用这个类里的方法获得数据！
		init();
	}
	
	public void init() {
		chart.setInteractive(true);
	//	chart.setZoomType(zoomType);
	//	chart.setContainerScrollEnabled(true, );
	//  coder 可根据自身需求定义更多成员函数	
		
		List<PointValue> values = new ArrayList<PointValue>();
		Date date = new Date(); // current time? YES
		
		/**
		 * 下面是出问题的地方，无法调用 getAvailableTimeByPeriod 方法。
		 */
		
		int[] availableTime = new int[6];
		availableTime = timeManager.getAvailableTimeByPeriod(date, taskType);
//		try{
//			if (timeManager.getAvailableTimeByPeriod(date, taskType) == null)
//				Log.e("availableTime", "NULL !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
//		} catch (Exception e){
//			Log.e("availableTime", "Exception !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"); // 结果显示这个
//		}
	//	Log.e("availableTime", availableTime.length+"");
	//	Log.e("availableTime", availableTime.toString()+"");
	//	int[] totalTime = timeManager.getUsageTimeByPeriod(date);
		for (int i = 0; i < availableTime.length; i++) {
			values.add(new PointValue(i, availableTime[i]));
		}
		
		Line line = new Line(values).setColor(Color.BLUE).setCubic(true);
		List<Line> lines = new ArrayList<Line>();
		lines.add(line);
		
		Axis axisX = new Axis();
		Axis axisY = new Axis().setHasLines(true);
		
		LineChartData data = new LineChartData();
		data.setAxisXBottom(axisX);
		data.setAxisYLeft(axisY);
		data.setLines(lines);
		
		chart.setLineChartData(data);
	}

}

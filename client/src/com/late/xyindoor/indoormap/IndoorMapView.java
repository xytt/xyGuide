package com.late.xyindoor.indoormap;

import java.text.DecimalFormat;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.TextView;

public class IndoorMapView extends ImageView {

	private int imageWidth;
	private int imageHeight;

	private MarkerView marker;
	private TextView text;
	
	public IndoorMapView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public IndoorMapView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public IndoorMapView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		// return super.onTouchEvent(event);
		// Log.d("imageView Location", String.valueOf(this.getX()) +", " +
		// String.valueOf(this.getY()));
		if (!(event.getX() > this.getLeft() && event.getX() < this.getRight()
				&& event.getY() > this.getTop() && event.getY() < this
				.getBottom())) {
			return super.onTouchEvent(event);
		}
		marker.setXY(event.getX(), event.getY());
		marker.invalidate();
		
		

		// imageWidth = this.getDrawable().getBounds().width();
		// imageHeight = this.getDrawable().getBounds().height();

		imageWidth = this.getWidth();
		imageHeight = this.getHeight();

		float x = (float) (Math.round((event.getX() / imageWidth) * 10000.0) / 10000.0);
		float y = (float) (Math.round((event.getY() / imageHeight) * 10000.0) / 10000.0);

		DecimalFormat df = new DecimalFormat("0.0000");
		
		marker.setInMapXY(df.format(x), df.format(y));

		text.setText(marker.getInMapX() + ", " + marker.getInMapY());
		
		return true;
	}

	public void addMarker(MarkerView marker) {
		this.marker = marker;
	}

	public void setTextView(TextView text){
		this.text = text;
	} 
}


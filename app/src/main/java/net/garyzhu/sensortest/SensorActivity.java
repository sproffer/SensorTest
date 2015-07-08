package net.garyzhu.sensortest;

import java.text.NumberFormat;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.support.v7.app.AppCompatActivity;

public class SensorActivity extends AppCompatActivity implements SensorEventListener {

	private SensorManager sensorManager = null;
	 
	//for accelerometer values
	private TextView outputX;
	private TextView outputY;
	private TextView outputZ;
	 
	//for orientation values
	private TextView outputX2;
	private TextView outputY2;
	private TextView outputZ2;
	
	private Sensor mAccelerometer;
	private Sensor mMagnetometer;
	
	private float[] mLastAccelerometer = new float[3];
    private float[] mLastMagnetometer = new float[3];
    private boolean hasAccelerometerSet = false;

    private float[] mR = new float[9];
    private float[] mOrientation = new float[3];
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sensor);
		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
	 
	    //just some textviews, for data output
	    outputX = (TextView) findViewById(R.id.textView01);
	    outputY = (TextView) findViewById(R.id.textView02);
	    outputZ = (TextView) findViewById(R.id.textView03);
	 
	    outputX2 = (TextView) findViewById(R.id.textView04);
	    outputY2 = (TextView) findViewById(R.id.textView05);
	    outputZ2 = (TextView) findViewById(R.id.textView06);
	    
	    outputX.setText("initial test string");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.sensor, menu);
		return true;
	}
	@Override
	protected void onResume() {
	    super.onResume();
	    Log.d("OnRESUME", "called");
	    hasAccelerometerSet = false;
	    mAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
	    sensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
	    
	    mMagnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
	    sensorManager.registerListener(this, mMagnetometer, SensorManager.SENSOR_DELAY_NORMAL);
	}
	
	@Override
	protected void onStop() {
	    super.onStop();
	    Log.d("OnSTOP", "called");
	    hasAccelerometerSet = false;
	    sensorManager.unregisterListener(this, mAccelerometer);
	    sensorManager.unregisterListener(this, mMagnetometer);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		synchronized (this) {
			NumberFormat nf = NumberFormat.getNumberInstance();
			nf.setMaximumFractionDigits(2); // rough out the precisions
	        if (event.sensor == mAccelerometer){
            	System.arraycopy(event.values, 0, mLastAccelerometer, 0, event.values.length);
                outputX.setText("x: "+ nf.format(mLastAccelerometer[0]));
                outputY.setText("y: "+ nf.format(mLastAccelerometer[1]));
                outputZ.setText("z: "+nf.format(mLastAccelerometer[2]));
                hasAccelerometerSet = true;
	        }
	        else if (event.sensor == mMagnetometer) {
            	System.arraycopy(event.values, 0, mLastMagnetometer, 0, event.values.length);
            	if (hasAccelerometerSet) {
            		SensorManager.getRotationMatrix(mR, null, mLastAccelerometer, mLastMagnetometer);
                    SensorManager.getOrientation(mR, mOrientation);
	                outputX2.setText("x: "+nf.format(mOrientation[0]));
	                outputY2.setText("y: "+nf.format(mOrientation[1]));
	                outputZ2.setText("z: "+nf.format(mOrientation[2]));
            	}
	        } else {
	            Log.d("sensor", "got other sensor event " + event.sensor.getType());
	        }
	    }
	}
}

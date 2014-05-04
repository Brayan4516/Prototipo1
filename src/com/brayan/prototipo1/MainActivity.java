package com.brayan.prototipo1;

import android.os.Bundle;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class MainActivity extends Activity {
	BluetoothAdapter adaptadorBlue = BluetoothAdapter.getDefaultAdapter();
	int RES = 1;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		final Switch EstadoBlu = (Switch) findViewById(R.id.switchBluetooth);
		Button EstadoVisi =  (Button)findViewById(R.id.btnVisible);
		if (TieneBluetooht()){
			if (estaActivadoBluetooth()){
				EstadoBlu.setChecked(true);
			} else {
				EstadoBlu.setChecked(false);
			}
			
		} else {
			Toast.makeText(getApplicationContext(), R.string.NoBlueTooth, Toast.LENGTH_LONG).show();
			return;
		}
		//cambio de estado de bluetooth y visibilidad
		EstadoBlu.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				
				if (estaActivadoBluetooth()){
					adaptadorBlue.disable();
					EstadoBlu.setChecked(false);
				} else {
						activarBluetooth();
						EstadoBlu.setChecked(true);
				}
			}
		} );
		//termina primer switch
		EstadoVisi.setOnClickListener( new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(estaActivadoBluetooth()){
					HacerVisibleBluetooht();
				}else {
					Toast.makeText(getApplicationContext(), R.string.activalo, Toast.LENGTH_SHORT).show();
				}
				
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	//******************SERIVICOS DE BLUETOOTH***********************/
		public boolean TieneBluetooht(){
			if (adaptadorBlue == null){
				//no hay bluetooth
				//Toast.makeText, "Su dispositivo no cuenta con Bluethoot", Toast.LENGTH_LONG);
				return false;
			}
			return true;
		}
		
		public void activarBluetooth(){
			 RES = 1;
			if (!adaptadorBlue.isEnabled()){
				Intent iniciarBlue = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
				startActivityForResult(iniciarBlue,RES);		
			}

		}
		
		public boolean estaActivadoBluetooth(){
			if (!adaptadorBlue.isEnabled()){
				return false;
			}
			return true;
		}
		
		public void HacerVisibleBluetooht(){
			Intent intentoVisible = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
			intentoVisible.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 240);
			startActivity(intentoVisible);
		}

}

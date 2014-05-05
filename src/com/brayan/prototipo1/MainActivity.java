package com.brayan.prototipo1;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

import android.os.Bundle;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class MainActivity extends Activity {
	BluetoothAdapter adaptadorBlue = BluetoothAdapter.getDefaultAdapter();
	int RES = 1;
	ListView lista;
	ArrayList<String> nombres;
	LinkedList<BluetoothDevice> CONTACTOS;
	
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		lista = (ListView) findViewById(R.id.listCon);
		final Switch EstadoBlu = (Switch) findViewById(R.id.switchBluetooth);
		Button EstadoVisi =  (Button)findViewById(R.id.btnVisible);
		if (TieneBluetooht()){
			if (estaActivadoBluetooth()){
				EstadoBlu.setChecked(true);
				ActualizarLista(true);
			} else {
				EstadoBlu.setChecked(false);
				ActualizarLista(false);
			}
			
		} else {
			Toast.makeText(getApplicationContext(), R.string.NoBlueTooth, Toast.LENGTH_LONG).show();
			EstadoBlu.setChecked(false);
			EstadoVisi.setClickable(false);
			
		}
		//cambio de estado de bluetooth y visibilidad
		EstadoBlu.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if (!TieneBluetooht()){return;}
				if (estaActivadoBluetooth()){
					adaptadorBlue.disable();
					EstadoBlu.setChecked(false);
					ActualizarLista(false);
				} else {
						activarBluetooth();
						EstadoBlu.setChecked(true);
						ActualizarLista(true);
				}
			}
		} );
		//termina primer switch
		EstadoVisi.setOnClickListener( new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!TieneBluetooht()){return;}
				if(estaActivadoBluetooth()){
					HacerVisibleBluetooht();
				}else {
					Toast.makeText(getApplicationContext(), R.string.activalo, Toast.LENGTH_SHORT).show();
				}
				
			}
		});
		if (TieneBluetooht()){
			
			
			
			
		}
		//modifico la lista
		//obtengo la lista de los dispositivos que se agregaron
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void ActualizarLista(boolean T){
		lista = (ListView) findViewById(R.id.listCon);
		if (!T){
			 nombres = new ArrayList<String>();
			 ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.view_text,nombres);
			 lista.setAdapter(adapter);
			 return;
		} 
		CONTACTOS = (LinkedList<BluetoothDevice>) ListaDeAmigos();
		if (CONTACTOS.size() < 1){
			ArrayList<String> txt = new ArrayList<String>();
			txt.add("+ Agregar");
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,txt);
			lista.setAdapter(adapter);
			
		} else {
			ArrayList<String> nombres = new ArrayList<String>();
			for (BluetoothDevice BTD : CONTACTOS){
				nombres.add(BTD.getName());
			}
			nombres.add("Agregar");
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.view_text,nombres);
			lista.setAdapter(adapter);
		}
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
		
		public LinkedList<BluetoothDevice> ListaDeAmigos(){
			
			Set<BluetoothDevice> vinculados = adaptadorBlue.getBondedDevices();
			
			LinkedList<BluetoothDevice> CONTACTOS = new LinkedList<BluetoothDevice>();

			if (vinculados.size() > 0) {
				Toast.makeText(getApplicationContext(), "mayor", Toast.LENGTH_LONG).show();
				for (BluetoothDevice dispo : vinculados){
					//crear un arreglo, dispo.getName() , dispo.getAdress()
					CONTACTOS.add(dispo);
				}
				
			}
			return CONTACTOS;

		}

}

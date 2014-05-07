package com.brayan.prototipo1;

import java.util.ArrayList;
import java.util.UUID;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class AgregarNuevoActivity extends Activity implements OnItemClickListener {
	
	ListView listN;
	boolean Buscando;
	ArrayList<String> contenido;
	 ArrayList<BluetoothDevice> nuevos;
	// UUID MY_UUID = UUID.fromString("4416359a-5bf3-473b-b394-e297e93376e3");
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_agregar_nuevo);
		listN = (ListView) findViewById(R.id.listaNuevos);
		listN.setOnItemClickListener(this);
		//Button BotonBus = (Button) findViewById(R.id.btnBuscar);
		/*
		btnBus.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				buscarDispoNuevos();
				
			}
		});*/
		buscarDispoNuevos();
		Toast.makeText(this, Integer.toString(nuevos.size()), Toast.LENGTH_LONG).show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.agregar_nuevo, menu);
		return true;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int posicion, long id) {
		// TODO Auto-generated method stub
		if (nuevos == null){return;}
		if (nuevos.isEmpty()){return;}
		BluetoothDevice BTD = nuevos.get(posicion);
		String menj = ((TextView) view).getText().toString();
		if (!menj.equals(BTD.getName())) {
			Toast.makeText(getApplicationContext(), "ERROR NO COINCIDEN", Toast.LENGTH_SHORT).show();
			return;
		}
		//empezar a conentar con BTD
		Toast.makeText(getApplicationContext(), BTD.getAddress(), Toast.LENGTH_SHORT).show();
		new ConnectAsyncTask().execute(BTD.getAddress());
		
	}
	
	//busca nuevos dispositivos
	public void buscarDispoNuevos(){
		
		BluetoothAdapter BTadapter = BluetoothAdapter.getDefaultAdapter();
		if (BTadapter.isDiscovering()) {
	       // Toast.makeText(this, "\nCancel discovery...", Toast.LENGTH_LONG).show();
	        BTadapter.cancelDiscovery(); 
	    }else{
	       // Toast.makeText(this, "\nStarting discovery...", Toast.LENGTH_LONG).show();
	        //mBluetoothAdapter.
	        nuevos = new ArrayList<BluetoothDevice>();
	        BTadapter.startDiscovery(); 
	       // Toast.makeText(this, "\nDone with discovery...", Toast.LENGTH_LONG).show();
	    }
		//Toast.makeText(getApplicationContext(), "Buscando...", Toast.LENGTH_SHORT).show();
		 
		
		final BroadcastReceiver mReceiver = new BroadcastReceiver() {
		    public void onReceive(Context context, Intent intent) {
		        String action = intent.getAction();
		        // When discovery finds a device
		        if (BluetoothDevice.ACTION_FOUND.equals(action)) {
		            // Get the BluetoothDevice object from the Intent
		            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
		            // Add the name and address to an array adapter to show in a ListView
		            nuevos.add(device);
		           // Toast.makeText(getApplicationContext(), "ENCONTRE UNO", Toast.LENGTH_LONG).show();
		            ActualizarLista();
		        }
		    }
		};
		// Register the BroadcastReceiver
		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		registerReceiver(mReceiver, filter); // Don't forget to unregister during onDestroy
		//ya tengo los nuevos
		
		if (nuevos.isEmpty()){
			Toast.makeText(this, Integer.toString(nuevos.size()), Toast.LENGTH_LONG).show();
			contenido = new ArrayList<String>();
			contenido.add(getText(R.string.NoNuevosFind).toString()); //puede haber error creo
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.view_text,contenido);
			listN.setAdapter(adapter);
			
			//Toast.makeText(getApplicationContext(), "NO SE ENCONTRARON DISPOSITIVOS", Toast.LENGTH_SHORT).show();
			return;
		}
		contenido = new ArrayList<String>();
		for (BluetoothDevice BTD : nuevos){
			contenido.add(BTD.getName());
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.view_text,contenido);
		listN.setAdapter(adapter);
	}
	
	public void ActualizarLista(){
		if (nuevos.isEmpty()){
			Toast.makeText(this, Integer.toString(nuevos.size()), Toast.LENGTH_LONG).show();
			contenido = new ArrayList<String>();
			contenido.add(getText(R.string.NoNuevosFind).toString()); //puede haber error creo
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.view_text,contenido);
			listN.setAdapter(adapter);
			
			//Toast.makeText(getApplicationContext(), "NO SE ENCONTRARON DISPOSITIVOS", Toast.LENGTH_SHORT).show();
			return;
		}
		contenido = new ArrayList<String>();
		for (BluetoothDevice BTD : nuevos){
			contenido.add(BTD.getName());
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.view_text,contenido);
		listN.setAdapter(adapter);
	}
	
	
	
	public class ConnectAsyncTask extends AsyncTask<String, Void, Boolean> {
		 
	      
	        private final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
	        @Override
	        protected void onPostExecute(Boolean isConnected) {
	            Toast.makeText(getApplicationContext(), "Connecting...",
	                    Toast.LENGTH_SHORT).show();
	        }

	        @Override
	        protected Boolean doInBackground(String... param) {
	            BluetoothDevice device = BluetoothAdapter
	                    .getDefaultAdapter().getRemoteDevice(param[0]);
	            try {
	                BluetoothSocket mmSocket = device
	                        .createRfcommSocketToServiceRecord(MY_UUID);
	                mmSocket.connect();
	            } catch (Exception e) {
	                e.printStackTrace();
	                Log.d("BluetoothConnectivity", "ERROR:" + e.getMessage());
	                return false;
	            }
	            return true;
	        }

	        protected void onPostExecute1(Boolean isConnected) {
	            if (isConnected) {
	                Toast.makeText(getApplicationContext(), "Connected",
	                        Toast.LENGTH_SHORT).show();
	            } else {
	                Toast.makeText(getApplicationContext(), "Failed to connected",
	                        Toast.LENGTH_SHORT).show();
	            }
	        }

	 }
}

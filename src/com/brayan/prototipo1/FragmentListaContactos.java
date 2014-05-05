package com.brayan.prototipo1;

import java.util.LinkedList;
import java.util.Set;


import android.app.Fragment;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;


public class FragmentListaContactos extends Fragment implements OnItemClickListener{

	BluetoothAdapter adaptadorBlue = BluetoothAdapter.getDefaultAdapter();
	
	ListView Lista;
	
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		
	}




	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View vista = inflater.inflate(R.id.listView1, container,false);
		Lista = (ListView) vista.findViewById(R.id.listView1 );
		return vista;
	}




	public LinkedList<BluetoothDevice> ListaDeAmigos(){
		
		Set<BluetoothDevice> vinculados = adaptadorBlue.getBondedDevices();
		LinkedList<BluetoothDevice> CONTACTOS = new LinkedList<BluetoothDevice>();

		if (vinculados.size() > 0) {

			for (BluetoothDevice dispo : vinculados){
				//crear un arreglo, dispo.getName() , dispo.getAdress()
				CONTACTOS.add(dispo);
			}
			
		}
		return CONTACTOS;

	}




	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		
	}
	

}

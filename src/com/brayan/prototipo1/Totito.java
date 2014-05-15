package com.brayan.prototipo1;

import java.util.Random;
import java.util.Vector;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Totito extends Activity implements OnClickListener {

	private static final String TAG = "BluetoothChat";
    private static final boolean D = true;
    // Message types sent from the BluetoothConectChanel Handler
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;
    // Key names received from the BluetoothConectChanel Handler
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";
    // Intent request codes
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;
    // Layout Views
    private TextView TxtInfo;
    private EditText mOutEditText;
    
   
	private ImageButton bt1, bt2,bt3,bt4,bt5,bt6,bt7,bt8,bt9;
	
    // Name of the connected device
    private String mConnectedDeviceName = null;
    // Array adapter for the conversation thread
    private ArrayAdapter<String> mConversationArrayAdapter;
    // String buffer for outgoing messages
    private StringBuffer mOutStringBuffer;
    // Local Bluetooth adapter
    private BluetoothAdapter mBluetoothAdapter = null;
    // Member object for the chat services
    private BluetoothConectChanel mChatService = null;
    
    String CONTRINCANTE;
    
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(D) Log.e(TAG, "+++ ON CREATE +++");
        // Set up the window layout
       
        setContentView(R.layout.activity_totito);
       
        Intent iin= getIntent();
        Bundle b = iin.getExtras();
        String j = "";
        if(b!=null)
        {
             j =(String) b.get("direccion");
            
        }
        if(D) Log.e(TAG, "DIRECCION: " + j);
        CONTRINCANTE = j;
        inciarTotito();
        // Get local Bluetooth adapter
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        // If the adapter is null, then Bluetooth is not supported
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        
        
    }
    @Override
    public void onStart() {
        super.onStart();
        if(D) Log.e(TAG, "++ ON START ++");
        // If BT is not on, request that it be enabled.
        // setupChat() will then be called during onActivityResult
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        // Otherwise, setup the chat session
        } else {
            if (mChatService == null) setupChat();	//inicia 
        }
    }
    @Override
    public synchronized void onResume() {
        super.onResume();
        if(D) Log.e(TAG, "+ ON RESUME +");
        // Performing this check in onResume() covers the case in which BT was
        // not enabled during onStart(), so we were paused to enable it...
        // onResume() will be called when ACTION_REQUEST_ENABLE activity returns.
        if (mChatService != null) {
            // Only if the state is STATE_NONE, do we know that we haven't started already
            if (mChatService.getState() == BluetoothConectChanel.STATE_NONE) {
              // Start the Bluetooth chat services
              mChatService.start();
            }
        }
    }
    
    
    
    //TODO LO DEL JUEGO
    
    public int SYNC = 0;
    public boolean FINISH_GAME = false;
    boolean Jugador1 = true;
    Boolean Jugador2 = false;
    	
    	/**
    	 * !1!2!3
    	 * -------
    	 * !4!5!6
    	 * -------
    	 * !7!8!9
    	 */
    	 
    	int[] libres;
    	Vector<Integer> mios;
    	Vector<Integer> enemigo;
    	public void inciarTotito(){
    		libres = new int[]{1,2,3,4,5,6,7,8,9};
    		mios = new Vector<Integer>(9);
    		enemigo = new Vector<Integer>(9);
    		
    	}
    	
    	
    	public void marcar(int pos,boolean propio){
    		if (libres[pos - 1] != 0) {
    			libres[pos - 1] = 0;
    			boolean ganador = false;
    			if (propio){
    				mios.add(pos);
    				//enviar mensaje
    				cambiar(pos, true);
    				ganador = verificarGanador(mios);
    			} else {
    				enemigo.add(pos);
    				cambiar(pos, false);
    				ganador = verificarGanador(enemigo);
    				Log.d(TAG, "verifico enemigo!!!!!!!!!!!: " + ganador + enemigo.get(0));
    				if (ganador){
    					Log.d(TAG, "GANO ENEMIGO!!!!!!!!!!!: " + ganador + enemigo.get(0));
    					FINISH_GAME = true;
        				TxtInfo.setText(getText(R.string.perdiste));
    				}
    			}
    			
    			if (ganador && propio){
    				//yo gane
    				FINISH_GAME = true;
    				TxtInfo.setText(getText(R.string.ganaste));
    			} 
    			
    			
    		} else {
    			//ya se uso esa casilla
    			
    		}
    	}
    	
    	//comandos de juego
    	//CA=1  indicar casilla a la cula colocar
    	//restart  reinicia el juego
    	//finish  termino partida, desconectar
    	
    	
    	public boolean verificarGanador(Vector<Integer> analizar){
			
    		//posibles jugadas
    		//hacia abajo  741 (1)	852(2)	963(3)
    		//hacia lado   789(4) 	456(5)	123(6)
    		//cruzado		159(7)	357(8)
    		//casos hacia abajo
			if (analizar.contains(7) && analizar.contains(4) && analizar.contains(1)){
				bt7.setBackgroundResource(R.drawable.check);
				bt4.setBackgroundResource(R.drawable.check);
				bt1.setBackgroundResource(R.drawable.check);
				return true;
			} else if (analizar.contains(8) && analizar.contains(5) && analizar.contains(2)){
				bt8.setBackgroundResource(R.drawable.check);
				bt5.setBackgroundResource(R.drawable.check);
				bt2.setBackgroundResource(R.drawable.check);
				return true;
			} else if (analizar.contains(9) && analizar.contains(6) && analizar.contains(3)){
				bt9.setBackgroundResource(R.drawable.check);
				bt6.setBackgroundResource(R.drawable.check);
				bt3.setBackgroundResource(R.drawable.check);
				return true;
			} else if (analizar.contains(7) && analizar.contains(8) && analizar.contains(9)){
				bt7.setBackgroundResource(R.drawable.check);
				bt8.setBackgroundResource(R.drawable.check);
				bt9.setBackgroundResource(R.drawable.check);
				return true;
			} else if (analizar.contains(4) && analizar.contains(5) && analizar.contains(6)){
				bt4.setBackgroundResource(R.drawable.check);
				bt5.setBackgroundResource(R.drawable.check);
				bt6.setBackgroundResource(R.drawable.check);
				return true;
			} else if (analizar.contains(1) && analizar.contains(2) && analizar.contains(3)){
				bt1.setBackgroundResource(R.drawable.check);
				bt2.setBackgroundResource(R.drawable.check);
				bt3.setBackgroundResource(R.drawable.check);
				return true;
			} else if (analizar.contains(1) && analizar.contains(5) && analizar.contains(9)){
				bt1.setBackgroundResource(R.drawable.check);
				bt5.setBackgroundResource(R.drawable.check);
				bt9.setBackgroundResource(R.drawable.check);
				return true;
			} else if (analizar.contains(7) && analizar.contains(5) && analizar.contains(3)){
				bt7.setBackgroundResource(R.drawable.check);
				bt5.setBackgroundResource(R.drawable.check);
				bt3.setBackgroundResource(R.drawable.check);
				return true;
			}
    		return false;
    	}
    	
    	public void cambiar(int po, boolean mio){		
    		switch(po){
			case 1:
				if (mio){
					if (Jugador1){
    					bt1.setBackgroundResource(R.drawable.equis);
    				} else if (Jugador2){
    					bt1.setBackgroundResource(R.drawable.circulo);
    				}
				} else {
					if (Jugador1){
    					bt1.setBackgroundResource(R.drawable.circulo);
    				} else if (Jugador2){
    					bt1.setBackgroundResource(R.drawable.equis);
    				}
				}
				break;
			case 2:
				if (mio){
					if (Jugador1){
    					bt2.setBackgroundResource(R.drawable.equis);
    				} else if (Jugador2){
    					bt2.setBackgroundResource(R.drawable.circulo);
    				}
				} else {
					if (Jugador1){
    					bt2.setBackgroundResource(R.drawable.circulo);
    				} else if (Jugador2){
    					bt2.setBackgroundResource(R.drawable.equis);
    				}
				}
				break;
			case 3:
				if (mio){
					if (Jugador1){
    					bt3.setBackgroundResource(R.drawable.equis);
    				} else if (Jugador2){
    					bt3.setBackgroundResource(R.drawable.circulo);
    				}
				} else {
					if (Jugador1){
    					bt3.setBackgroundResource(R.drawable.circulo);
    				} else if (Jugador2){
    					bt3.setBackgroundResource(R.drawable.equis);
    				}
				}
				break;
			case 4:
				if (mio){
					if (Jugador1){
    					bt4.setBackgroundResource(R.drawable.equis);
    				} else if (Jugador2){
    					bt4.setBackgroundResource(R.drawable.circulo);
    				}
				} else {
					if (Jugador1){
    					bt4.setBackgroundResource(R.drawable.circulo);
    				} else if (Jugador2){
    					bt4.setBackgroundResource(R.drawable.equis);
    				}
				}
				break;
			case 5:
				if (mio){
					if (Jugador1){
    					bt5.setBackgroundResource(R.drawable.equis);
    				} else if (Jugador2){
    					bt5.setBackgroundResource(R.drawable.circulo);
    				}
				} else {
					if (Jugador1){
    					bt5.setBackgroundResource(R.drawable.circulo);
    				} else if (Jugador2){
    					bt5.setBackgroundResource(R.drawable.equis);
    				}
				}
				break;
			case 6:
				if (mio){
					if (Jugador1){
    					bt6.setBackgroundResource(R.drawable.equis);
    				} else if (Jugador2){
    					bt6.setBackgroundResource(R.drawable.circulo);
    				}
				} else {
					if (Jugador1){
    					bt6.setBackgroundResource(R.drawable.circulo);
    				} else if (Jugador2){
    					bt6.setBackgroundResource(R.drawable.equis);
    				}
				}
				break;
			case 7:
				if (mio){
					if (Jugador1){
    					bt7.setBackgroundResource(R.drawable.equis);
    				} else if (Jugador2){
    					bt7.setBackgroundResource(R.drawable.circulo);
    				}
				} else {
					if (Jugador1){
    					bt7.setBackgroundResource(R.drawable.circulo);
    				} else if (Jugador2){
    					bt7.setBackgroundResource(R.drawable.equis);
    				}
				}
				break;
			case 8:
				if (mio){
					if (Jugador1){
    					bt8.setBackgroundResource(R.drawable.equis);
    				} else if (Jugador2){
    					bt8.setBackgroundResource(R.drawable.circulo);
    				}
				} else {
					if (Jugador1){
    					bt8.setBackgroundResource(R.drawable.circulo);
    				} else if (Jugador2){
    					bt8.setBackgroundResource(R.drawable.equis);
    				}
				}
				break;
			case 9:
				if (mio){
					if (Jugador1){
    					bt9.setBackgroundResource(R.drawable.equis);
    				} else if (Jugador2){
    					bt9.setBackgroundResource(R.drawable.circulo);
    				}
				} else {
					if (Jugador1){
    					bt9.setBackgroundResource(R.drawable.circulo);
    				} else if (Jugador2){
    					bt9.setBackgroundResource(R.drawable.equis);
    				}
				}
				break;
			}
    	}
    	//termina el juego
    	
    	
    //
    private void setupChat() {
        Log.d(TAG, "setupChat()");
      
        // Initialize the array adapter for the conversation thread      
        bt1 = (ImageButton) findViewById(R.id.ImageButton01);
        bt2 = (ImageButton) findViewById(R.id.ImageButton02);
        bt3 = (ImageButton) findViewById(R.id.ImageButton03);
        bt4 = (ImageButton) findViewById(R.id.ImageButton04);
        bt5 = (ImageButton) findViewById(R.id.ImageButton05);
        bt6 = (ImageButton) findViewById(R.id.ImageButton06);
        bt7 = (ImageButton) findViewById(R.id.ImageButton07);
        bt8 = (ImageButton) findViewById(R.id.ImageButton08);
        bt9 = (ImageButton) findViewById(R.id.ImageButton09);
        bt1.setOnClickListener(this);
        bt2.setOnClickListener(this);
        bt3.setOnClickListener(this);
        bt4.setOnClickListener(this);
        bt5.setOnClickListener(this);
        bt6.setOnClickListener(this);
        bt7.setOnClickListener(this);
        bt8.setOnClickListener(this);
        bt9.setOnClickListener(this);
        TxtInfo = (TextView) findViewById(R.id.textinfo1);
        TxtInfo.setText(getText(R.string.Espernado_contrin));
        // Initialize the BluetoothConectChanel to perform bluetooth connections
        mChatService = new BluetoothConectChanel(this, mHandler);
        // Initialize the buffer for outgoing messages
        mOutStringBuffer = new StringBuffer("");
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(CONTRINCANTE);
        // Attempt to connect to the device
        mChatService.connect(device);
        
    }
    
    
    public boolean marcando = true;  //si esta esperando al otro a que se conecte o marque la opcion
    		
    @Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
    	if(FINISH_GAME){return;} //ya termino el juego
    	if (marcando) {
    		//ya envio su opcion
    		
    		return;
    	}
    	marcando = true;
    	
		if (v.getId() == bt1.getId()){
			if (libres[0] != 0){
				sendMessage("CA=1");
				
				marcar(1, true);
			}
			
		} else if (v.getId() == bt2.getId()){
			if (libres[1] != 0){
				sendMessage("CA=2");
				marcar(2, true);
			}
			
		}else if (v.getId() == bt3.getId()){
			if (libres[2] != 0){
				sendMessage("CA=3");
				marcar(3, true);
			}
			
		}else if (v.getId() == bt4.getId()){
			if (libres[3] != 0){
				sendMessage("CA=4");
				marcar(4, true);
			}
			
		}else if (v.getId() == bt5.getId()){
			if (libres[4] != 0){
				sendMessage("CA=5");
				marcar(5, true);
			}
			
		}else if (v.getId() == bt6.getId()){
			if (libres[5] != 0){
				sendMessage("CA=6");
				marcar(6, true);
			}
			
		}else if (v.getId() == bt7.getId()){
			if (libres[6] != 0){
				sendMessage("CA=7");
				marcar(7, true);
			}
			
		}else if (v.getId() == bt8.getId()){
			if (libres[7] != 0){
				sendMessage("CA=8");
				marcar(8, true);
			}
			
		}else if (v.getId() == bt9.getId()){
			if (libres[8] != 0){
				sendMessage("CA=9");
				marcar(9, true);
			}
		}
		
	}
    
    public void sincronizar(){
    	 Random rnd = new Random();
    	 SYNC = rnd.nextInt(10);
    	sendMessage("SY=" + SYNC);
    }
    
    
    @Override
    public synchronized void onPause() {
        super.onPause();
        if(D) Log.e(TAG, "- ON PAUSE -");
    }
    @Override
    public void onStop() {
        super.onStop();
        if(D) Log.e(TAG, "-- ON STOP --");
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        // Stop the Bluetooth chat services
        if (mChatService != null) mChatService.stop();
        if(D) Log.e(TAG, "--- ON DESTROY ---");
    }
    private void ensureDiscoverable() {
        if(D) Log.d(TAG, "ensure discoverable");
        if (mBluetoothAdapter.getScanMode() !=
            BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivity(discoverableIntent);
        }
    }
    /**
     * Sends a message.
     * @param message  A string of text to send.
     */
    private void sendMessage(String message) {
        // Check that we're actually connected before trying anything
    	 if(D) Log.e(TAG, "ENVIAR MENSAJE");
        if (mChatService.getState() != BluetoothConectChanel.STATE_CONNECTED) {
         //   Toast.makeText(this, R.string.not_connected, Toast.LENGTH_SHORT).show();
            return;
        }
        // Check that there's actually something to send
        if (message.length() > 0) {
            // Get the message bytes and tell the BluetoothConectChanel to write
            byte[] send = message.getBytes();
            mChatService.write(send);
            // Reset out string buffer to zero and clear the edit text field
            mOutStringBuffer.setLength(0);
           
        }
    }
    
    
    
    // The Handler that gets information back from the BluetoothConectChanel
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case MESSAGE_STATE_CHANGE:
                if(D) Log.i(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
                switch (msg.arg1) {
                case BluetoothConectChanel.STATE_CONNECTED:
                 //   mTitle.setText(R.string.title_connected_to);
                //    mTitle.append(mConnectedDeviceName);
                   // mConversationArrayAdapter.clear(); //NOVA
                    break;
                case BluetoothConectChanel.STATE_CONNECTING:
                //    mTitle.setText("Conectando");
                    break;
                case BluetoothConectChanel.STATE_LISTEN:
                case BluetoothConectChanel.STATE_NONE:
                //    mTitle.setText("No conectado");
                    break;
                }
                break;
            case MESSAGE_WRITE:
                byte[] writeBuf = (byte[]) msg.obj;
                // construct a string from the buffer
                if (!FINISH_GAME){
    				if (TxtInfo.getText().equals(getText(R.string.Su_turno))){
        				TxtInfo.setText(getText(R.string.turno_de));
        			} else {
        				TxtInfo.setText(getText(R.string.Su_turno));
        			}
    			}
                //SE VUELVE A MOSTRAR LO QUE SE ENVIA
                
             /*   String writeMessage = new String(writeBuf); //de valde lo alamaceno
                
                mConversationArrayAdapter.add("Me:  " + writeMessage);//NOVA*/
                break;
            case MESSAGE_READ:
                byte[] readBuf = (byte[]) msg.obj;
                // construct a string from the valid bytes in the buffer
                
                //SE OBIENE EL MENSAJE POR LO QUE SE EJECUTA LO QUE SE RECIBE
                
                String cm = new String(readBuf, 0, msg.arg1);
                
                if (cm.contains("CA=")){
        			//comando de colocacion
        			cm = cm.substring(3, 4);
        			marcar(Integer.parseInt(cm), false);
        			//cambio de turno
        			Log.d(TAG, "MENSAJE: " + cm);
        			Log.d(TAG, "ESTADO A CAMBIAR " + marcando);
        			if (marcando){
        				marcando = false;	
        			} else {
        				marcando = true;
        			}
        			
        			if (!FINISH_GAME){
        				if (TxtInfo.getText().equals(getText(R.string.Su_turno))){
            				TxtInfo.setText(getText(R.string.turno_de));
            			} else {
            				TxtInfo.setText(getText(R.string.Su_turno));
            			}
        			}
        			
        			Log.d(TAG, "ESTADO TRAS RECIBIR " + marcando);
        		} else if (cm.equals("reset")){
        			//reiniciar
        			
        			
        		} else if (cm.equals("finish")){
        			//terminar
        			
        			
        		} else if (cm.contains("SY=")){
        			cm = cm.substring(3, 4);
        			
        			if (SYNC ==Integer.parseInt(cm) )
        			{
        				sincronizar();
        				return;
        			} else if (SYNC > Integer.parseInt(cm)){
        				Jugador1 = true;
        				Jugador2 = false;
        				
        			} else {
        				Jugador2 = true;
        				Jugador1 = false;
        			}
        			//decide quien va primero
        			
        			if (Jugador2){
        				marcando = true;
        				TxtInfo.setText(getText(R.string.turno_de));
        			} else if (Jugador1) {
        				marcando = false;
        				TxtInfo.setText(getText(R.string.Su_turno));
        			}
        			Log.d(TAG, "ESTADO INICIAL: " + marcando);
        		}
                
                break;
            case MESSAGE_DEVICE_NAME:
                // save the connected device's name
                mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
                Toast.makeText(getApplicationContext(), "Connected to "
                               + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
               
                sincronizar();
                //sendMessage("SY=" + rnd.nextInt(10));
                
                break;
            case MESSAGE_TOAST:
                Toast.makeText(getApplicationContext(), msg.getData().getString(TOAST),
                               Toast.LENGTH_SHORT).show();
                break;
            }
        }
    };
    
    
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(D) Log.d(TAG, "onActivityResult " + resultCode);
        switch (requestCode) {
        case REQUEST_ENABLE_BT:
            // When the request to enable Bluetooth returns
            if (resultCode == Activity.RESULT_OK) {
                // Bluetooth is now enabled, so set up a chat session
                setupChat();
            } else {
                // User did not enable Bluetooth or an error occured
                Log.d(TAG, "BT not enabled");
               // Toast.makeText(this, R.string.bt_not_enabled_leaving, Toast.LENGTH_SHORT).show();
               // finish();
            }
        }
    }
    
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
      //  inflater.inflate(R.menu.option_menu, menu);
        return true;
    }
 
	
}


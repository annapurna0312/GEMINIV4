package com.example.geminiv4.bt;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.geminiv4.MainActivity;
import com.example.geminiv4.boundarydetails.IndianBoundaryDetails;
import com.example.geminiv4.boundarydetails.InternationalBoundaryUtils;
import com.example.geminiv4.boundarydetails.NearestCountry;
import com.example.geminiv4.boundarydetails.NearestLandingCenterDetails;
import com.example.geminiv4.boundarydetails.OtherCountryBoundaryUtils;
import com.example.geminiv4.boundarydetails.RegionFilter;
import com.example.geminiv4.devicedata.GPSInfo;
import com.example.geminiv4.sqlite.MessagesHandler;
import com.example.geminiv4.ui.home.HomeFragment;
import com.example.geminiv4.utils.Converter;
import com.example.geminiv4.utils.LatLonUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * This class does all the work for setting up and managing Bluetooth
 * connections with other devices. It has a thread that listens for
 * incoming connections, a thread for connecting with a device, and a
 * thread for performing data transmissions when connected.
 */
public class BluetoothChatService  extends Service {

    // Name for the SDP record when creating server socket
    private static final String NAME = "BluetoothChat";
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    LocalBroadcastManager localBroadcastManager ;
    MessagesHandler messagesHandler;

    // Member fields
    private final BluetoothAdapter mAdapter;
    private final Handler mHandler;
    private AcceptThread mAcceptThread;
    private ConnectThread mConnectThread;
    private ConnectedThread mConnectedThread;
    private int mState;
    public static boolean isDeviceConnected;
    LatLonUtils latLonUtils;
    Converter converter;



    // Constants that indicate the current connection state
    public static final int STATE_NONE = 0;       // we're doing nothing
    public static final int STATE_LISTEN = 1;     // now listening for incoming connections
    public static final int STATE_CONNECTING = 2; // now initiating an outgoing connection
    public static final int STATE_CONNECTED = 3;  // now connected to a remote device


    /**
     * Constructor. Prepares a new BluetoothChat session.
     *
     * @param context The UI Activity Context
     * @param handler A Handler to send messages back to the UI Activity
     */
    public BluetoothChatService(Context context, Handler handler) {
        mAdapter = BluetoothAdapter.getDefaultAdapter();
        mState = STATE_NONE;
        mHandler = handler;
        this.isDeviceConnected = false;
        localBroadcastManager = LocalBroadcastManager.getInstance(context);
        this.latLonUtils = new LatLonUtils();
        this.converter = new Converter();
        this.messagesHandler = new MessagesHandler(context);
    }

    /**
     * Set the current state of the chat connection
     *
     * @param state An integer defining the current connection state
     */
    private synchronized void setState(int state) {
        mState = state;
        // Give the new state to the Handler so the UI Activity can update
        mHandler.obtainMessage(HomeFragment.MESSAGE_STATE_CHANGE, state, -1).sendToTarget();
    }

    /**
     * Return the current connection state.
     */
    public synchronized int getState() {
        return mState;
    }

    /**
     * Start the chat service. Specifically start AcceptThread to begin a
     * session in listening (server) mode. Called by the Activity onResume()
     */
    public synchronized void start() {
        // Cancel any thread attempting to make a connection
        if (mConnectThread != null) {
            mConnectThread.cancel();
            mConnectThread = null;
        }
        // Cancel any thread currently running a connection
        if (mConnectedThread != null) {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }
        // Start the thread to listen on a BluetoothServerSocket
        if (mAcceptThread == null) {
            mAcceptThread = new AcceptThread();
            mAcceptThread.start();
        }
        setState(STATE_LISTEN);
    }

    /**
     * Start the ConnectThread to initiate a connection to a remote device.
     *
     * @param device The BluetoothDevice to connect
     */
    public synchronized void connect(BluetoothDevice device) {
        // Cancel any thread attempting to make a connection
        if (mState == STATE_CONNECTING) {
            if (mConnectThread != null) {
                mConnectThread.cancel();
                mConnectThread = null;
            }
        }
        // Cancel any thread currently running a connection
        if (mConnectedThread != null) {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }
        // Start the thread to connect with the given device
        mConnectThread = new ConnectThread(device);
        mConnectThread.start();
        setState(STATE_CONNECTING);
    }

    /**
     * Start the ConnectedThread to begin managing a Bluetooth connection
     *
     * @param socket The BluetoothSocket on which the connection was made
     * @param device The BluetoothDevice that has been connected
     */
    public synchronized void connected(BluetoothSocket socket, BluetoothDevice device) {
        // Cancel the thread that completed the connection
        if (mConnectThread != null) {
            mConnectThread.cancel();
            mConnectThread = null;
        }
        // Cancel any thread currently running a connection
        if (mConnectedThread != null) {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }
        // Cancel the accept thread because we only want to connect to one device
        if (mAcceptThread != null) {
            mAcceptThread.cancel();
            mAcceptThread = null;
        }
        // Start the thread to manage the connection and perform transmissions
        mConnectedThread = new ConnectedThread(socket);
        mConnectedThread.start();
        // Send the name of the connected device back to the UI Activity
        Message msg = mHandler.obtainMessage(HomeFragment.MESSAGE_DEVICE_NAME);
        Bundle bundle = new Bundle();
        bundle.putString(MainActivity.DEVICE_NAME, device.getName());
        msg.setData(bundle);
        mHandler.sendMessage(msg);
        setState(STATE_CONNECTED);
        isDeviceConnected = true;
    }

    /**
     * Stop all threads
     */
    public synchronized void stop() {
        if (mConnectThread != null) {
            mConnectThread.cancel();
            mConnectThread = null;
        }
        if (mConnectedThread != null) {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }
        if (mAcceptThread != null) {
            mAcceptThread.cancel();
            mAcceptThread = null;
        }
        setState(STATE_NONE);
    }




    /**
     * Write to the ConnectedThread in an unsynchronized manner
     *
     * @param out The bytes to write
     * @see ConnectedThread#write(byte[])
     */
    public void write(byte[] out) {
        // Create temporary object
        ConnectedThread r;
        // Synchronize a copy of the ConnectedThread
        synchronized (this) {
            if (mState != STATE_CONNECTED) return;
            r = mConnectedThread;
        }
        // Perform the write unsynchronized
        r.write(out);
    }

    /**
     * Indicate that the connection attempt failed and notify the UI Activity.
     */
    private void connectionFailed() {
        setState(STATE_LISTEN);
        // Send a failure message back to the Activity
        Message msg = mHandler.obtainMessage(HomeFragment.MESSAGE_TOAST);
        Bundle bundle = new Bundle();
        bundle.putString(MainActivity.TOAST, "Unable to connect device");
        msg.setData(bundle);
        mHandler.sendMessage(msg);
    }

    /**
     * Indicate that the connection was lost and notify the UI Activity.
     */
    private void connectionLost() {
        setState(STATE_LISTEN);
        // Send a failure message back to the Activity
        Message msg = mHandler.obtainMessage(HomeFragment.MESSAGE_TOAST);
        Bundle bundle = new Bundle();
        bundle.putString(MainActivity.TOAST, "Device connection was lost");
        msg.setData(bundle);
        mHandler.sendMessage(msg);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub
        sendMessage(null);
        return super.onStartCommand(intent, flags, startId);
    }

    // Send an Intent with an action named "custom-event-name". The Intent
    // sent should
    // be received by the ReceiverActivity.
    private void sendMessage(byte[] bytedata) {
        if(bytedata!=null && bytedata.length>0) {

            ByteDataParser byteDataParser = new ByteDataParser(this);
            Intent intent = new Intent("receiverdata");
            GPSInfo gpsInfo = byteDataParser.parseLocation(bytedata);
            updateGpsInfo(gpsInfo);
            intent.putExtra("gpsinfo", (Serializable) gpsInfo);
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);


            Type63DataParserV3 type63DataParserV3 = new Type63DataParserV3(this);
            HashMap<String, String> ret = type63DataParserV3.startParsingBytes(bytedata); //messages
            for(String prnid : ret.keySet()){ messagesHandler.storeFrame(prnid,ret.get(prnid)); }

        }
    }

    private void updateGpsInfo(GPSInfo gpsInfo) {
        if(!gpsInfo.getLatitude().equalsIgnoreCase("Fetching...") && !gpsInfo.getLongitude().equalsIgnoreCase("Fetching...")){

            gpsInfo.setCurrentregion(new RegionFilter().getCurrentRegion(gpsInfo.getLatitude(),gpsInfo.getLongitude()));
            NearestLandingCenterDetails nflc = new RegionFilter().getNearestLandingCenter(gpsInfo.getLatitude(), gpsInfo.getLongitude());
            gpsInfo.setNearestflc(nflc.getLandingcenter());
            gpsInfo.setNearestflc_dis(nflc.getDistance());
            gpsInfo.setNearestflc_dir(nflc.getBearing());
            double dist_kms = Double.parseDouble(nflc.getDistance());
            double speed_knots = Double.parseDouble(gpsInfo.getSpeed());
            if(speed_knots==0){
                gpsInfo.setNearestflc_eta("∞");
            }else{
                String eta_hours = converter.roundto2DecimalPlaces(dist_kms/(speed_knots*1.852));
                gpsInfo.setNearestflc_eta(eta_hours);
            }


            //International Boundary Details
            IndianBoundaryDetails indianboundarydetails = new InternationalBoundaryUtils().getBoundaryDetails(gpsInfo.getLatitude(), gpsInfo.getLongitude(), 'K');
            gpsInfo.setIndianBoundaryDetails(indianboundarydetails);

            NearestCountry nearestCountry = new OtherCountryBoundaryUtils().getOtherCoutryDetails(gpsInfo.getLatitude(),gpsInfo.getLongitude());
            gpsInfo.setNearestCountry(nearestCountry);
        }
    }





    /**
     * This thread runs while listening for incoming connections. It behaves
     * like a server-side client. It runs until a connection is accepted
     * (or until cancelled).
     */
    private class AcceptThread extends Thread {
        // The local server socket
        private final BluetoothServerSocket mmServerSocket;

        public AcceptThread() {
            BluetoothServerSocket tmp = null;
            // Create a new listening server socket
            try {
                tmp = mAdapter.listenUsingRfcommWithServiceRecord(NAME, MY_UUID);
            } catch (IOException e) {
            }
            mmServerSocket = tmp;
        }

        public void run() {
            setName("AcceptThread");
            BluetoothSocket socket = null;
            // Listen to the server socket if we're not connected
            while (mState != STATE_CONNECTED) {
                try {
                    // This is a blocking call and will only return on a
                    // successful connection or an exception
                    socket = mmServerSocket.accept();
                } catch (IOException e) {
                    break;
                }
                // If a connection was accepted
                if (socket != null) {
                    synchronized (BluetoothChatService.this) {
                        switch (mState) {
                            case STATE_LISTEN:
                            case STATE_CONNECTING:
                                // Situation normal. Start the connected thread.
                                connected(socket, socket.getRemoteDevice());
                                break;
                            case STATE_NONE:
                            case STATE_CONNECTED:
                                // Either not ready or already connected. Terminate new socket.
                                try {
                                    socket.close();
                                } catch (IOException e) {
                                }
                                break;
                        }
                    }
                }
            }
        }

        public void cancel() {
            try {
                mmServerSocket.close();
            } catch (IOException e) {
            }
        }
    }

    /**
     * This thread runs while attempting to make an outgoing connection
     * with a device. It runs straight through; the connection either
     * succeeds or fails.
     */
    private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;

        public ConnectThread(BluetoothDevice device) {
            mmDevice = device;
            BluetoothSocket tmp = null;
            // Get a BluetoothSocket for a connection with the
            // given BluetoothDevice
            try {
                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) {
            }
            mmSocket = tmp;
        }

        public void run() {
            setName("ConnectThread");
            // Always cancel discovery because it will slow down a connection
            mAdapter.cancelDiscovery();
            // Make a connection to the BluetoothSocket
            try {
                // This is a blocking call and will only return on a
                // successful connection or an exception
                mmSocket.connect();
            } catch (IOException e) {
                connectionFailed();
                // Close the socket
                try {
                    mmSocket.close();
                } catch (IOException e2) {
                }
                // Start the service over to restart listening mode
                BluetoothChatService.this.start();
                return;
            }
            // Reset the ConnectThread because we're done
            synchronized (BluetoothChatService.this) {
                mConnectThread = null;
            }
            // Start the connected thread
            connected(mmSocket, mmDevice);
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
            }
        }
    }

    /**
     * This thread runs during a connection with a remote device.
     * It handles all incoming and outgoing transmissions.
     */
    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;


        public ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;
            // Get the BluetoothSocket input and output streams
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
            }
            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer = new byte[1024];
            int bytes;
            // Keep listening to the InputStream while connected
            while (true) {
                try {
                    // Read from the InputStream
                    bytes = mmInStream.read(buffer);
                    // Send the obtained bytes to the UI Activity
                    if (bytes > 0) {
                        if (bytes > 1000) {
                            bytes = 1000;
                        }
                        //mByteDataParser.startParsing(buffer); //gps
                        //type63DataParserV3.startParsing(buffer); //messages
                    }
                    sendMessage(buffer);
                    mHandler.obtainMessage(HomeFragment.MESSAGE_READ, bytes, -1, buffer).sendToTarget();
                } catch (IOException e) {
                    connectionLost();
                    break;
                }
            }
        }



        /**
         * Write to the connected OutStream.
         *
         * @param buffer The bytes to write
         */
        public void write(byte[] buffer) {
            try {
                mmOutStream.write(buffer);
                // Share the sent message back to the UI Activity
                mHandler.obtainMessage(HomeFragment.MESSAGE_WRITE, -1, -1, buffer).sendToTarget();
            } catch (IOException e) {
            }
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
            }
        }
    }





}






class ByteDataParser {

    private static final String TAG = "BYTE_DATA_PARSER";
    private String checkSum = "";
    private boolean endHeaderFound = false;
    private String finalLine = "";
    private boolean startHeaderFound = false;
    Context context;
    NMEAParser nm = new NMEAParser();
    GPSInfo gpsInfo = new GPSInfo();

    public ByteDataParser(Context context) {
        this.context = context;
    }

    public GPSInfo parseLocation(byte[] data) {
        for (byte b : data) {
            char ch = (char) b;
            if (!(!this.startHeaderFound || this.endHeaderFound || ch == '*' || ch == '\n' || ch == '\r')) {
                this.finalLine += ch;
            }
            if (this.startHeaderFound && ch == '*') {
                this.endHeaderFound = true;
            } else {
                if (ch == '$') {
                    this.startHeaderFound = true;
                }
                if (this.endHeaderFound) {
                    this.checkSum += ch;
                    if (this.checkSum.length() == 2) {
                        int checkSumNow = DataIntegrity.checkSum(this.finalLine.getBytes());
                        int checkSumInternal = 0;
                        try {
                            checkSumInternal = Integer.parseInt(this.checkSum, 16);
                        } catch (Exception e) {
                        }
                        if (checkSumNow == checkSumInternal) {
                            nm.processMessage(this.finalLine);
                            gpsInfo = nm.getLog();
                            reset();
                        } else {
                            reset();
                        }
                    }
                }
            }
        }
        return gpsInfo;
    }



    private void reset() {
        this.finalLine = "";
        this.checkSum = "";
        this.startHeaderFound = false;
        this.endHeaderFound = false;
    }
}




class NMEAParser {
    static String[] Azumith = new String[7];
    static String[] Elevation = new String[7];
    private static Boolean GsvParseContinue = Boolean.FALSE;
    private static GPSInfo Log = new GPSInfo();
    static String[] Signal2Noise = new String[7];
    static String[] SvId = new String[7];
    public static boolean isActive = false;
    public static String[] tempAzumith;
    public static String[] tempElevation;
    static int tempI;
    public static String[] tempSnr;
    public static String[] tempSvid;
    int currSat = 0;
    int currentMsg = 0;
    int currentSat = 0;
    /* renamed from: i */
    int f3i;
    isNumeric isn = new isNumeric();
    /* renamed from: j */
    int f4j = 0;
    String[] last;
    int noOfMsgs = 0;
    int noOfSats = 0;
    String num;
    int processedSats = 0;
    int satsProcessed = 0;

    public GPSInfo getLog() {
        return Log;
    }

    private float Altitude2Decimal(String alt) {
        return Float.parseFloat(alt);
    }

    private float Latitude2Decimal(String lat, String NS) {
        float med = (Float.parseFloat(lat.substring(2)) / 60.0f) + Float.parseFloat(lat.substring(0, 2));
        if (NS.startsWith("S")) {
            return -med;
        }
        return med;
    }

    private float Longitude2Decimal(String lon, String WE) {
        float med = (Float.parseFloat(lon.substring(3)) / 60.0f) + Float.parseFloat(lon.substring(0, 3));
        if (WE.startsWith("W")) {
            return -med;
        }
        return med;
    }

    public void processMessage(String sentence) {
        if (sentence.startsWith("GPRMC")) {
            ParseGPRMC(sentence);
        } else if (sentence.startsWith("GPGGA")) {
            ParseGPGGA(sentence);
        } else if (sentence.startsWith("GPGSV")) {
            ParseGPGSV(sentence);
        } else if (sentence.startsWith("GPZDA")) {
            ParseGPZDA(sentence);
        } else if (sentence.startsWith("ACCPE")) {
            ParseACCPE(sentence);
        } else if (!sentence.startsWith("??") && sentence.startsWith("GPGSA")) {
            ParseGPGSA(sentence);
        }
    }

    public void ParseGPGSA(String sentence) {
        String[] strValues = sentence.split(",", -1);
        if (sentence.length() - sentence.replace(",", "").length() == 18) {
            Log.setFix(Integer.parseInt(strValues[2]));
            if (Integer.parseInt(strValues[2]) == 1) {
                Log.setFixAvailable("Fix not available");
            } else if (Integer.parseInt(strValues[2]) == 2) {
                Log.setFixAvailable("2D position fix or Altitude hold");
            } else if (Integer.parseInt(strValues[2]) == 3) {
                Log.setFixAvailable("3D position fix");
            }
            int arrayLength = Array.getLength(strValues);
            if (strValues[arrayLength - 1].length() != 0) {
                Log.setVdop(strValues[17]);
            }
            if (strValues[arrayLength - 2].length() != 0) {
                Log.setHdop(strValues[16]);
            }
            if (strValues[arrayLength - 3].length() != 0) {
                Log.setPdop(strValues[15]);
            }
        }
    }

    public void ParseACCPE(String sentence) {
        String[] strValues = sentence.split(",");
        int length = strValues.length;
        int i = 0;
        while (i < length) {
            if (strValues[i].length() != 0) {
                i++;
            } else {
                return;
            }
        }
        Log.setAccuracy(strValues[1]);
        if (strValues[3].equals("1")) {
            Log.setValidity("Fix Available");
        } else {
            Log.setValidity("Fix Unavailable");
        }
    }

    public void ParseGPZDA(String sentence) {
        try{
            String[] strValues = sentence.split(",");
            int length = strValues.length;
            int i = 0;
            while (i < length) {
                if (strValues[i].length() != 0) {
                    i++;
                } else {
                    return;
                }
            }
            //GPZDA,082912.27,30,06,2019,-00,00
            String hh = strValues[1].substring(0,2);
            String mm = strValues[1].substring(2,4);
            String ss = strValues[1].substring(4,6);
            String dd =  strValues[2];
            String MM =  strValues[3];
            String yyyy =  strValues[4];
            Log.setDate(yyyy+"-"+MM+"-"+dd);
            Log.setTime(hh+":"+mm+":"+ss);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void ParseGPGGA(String sentence) {
        String lat = "";
        String lng = "";
        String[] strValues = sentence.split(",");
        int length = strValues.length;
        int i = 0;
        while (i < length) {
            if (strValues[i].length() != 0) {
                i++;
            } else {
                return;
            }
        }
        if (sentence.length() - sentence.replace(",", "").length() == 14) {
            Log.setAltitude(String.format("%f", new Object[]{Float.valueOf(Altitude2Decimal(strValues[9]))}));
            if (isNumeric(strValues[2], strValues[4])) {
                lat = String.format("%f", new Object[]{Float.valueOf(Latitude2Decimal(strValues[2], strValues[3]))});
                lng = String.format("%f", new Object[]{Float.valueOf(Longitude2Decimal(strValues[4], strValues[5]))});
            }
            Log.setFix(Integer.valueOf(Integer.parseInt(strValues[6])));
            Log.setLatitude(lat);
            Log.setLongitude(lng);
            Log.setSatsInUse(strValues[7]);
        }
    }

    public void ParseGPGSV(String sentence) {
        try{
            if (sentence.length() - sentence.replace(",", "").length() == 20) {
                String[] strValues = sentence.split(",");
                int totalMessages = Integer.parseInt(strValues[1]);
                int currentMessage = Integer.parseInt(strValues[2]);
                try {
                    int tempNoOfSatsGsv;
                    this.noOfSats = Integer.parseInt(strValues[3]);
                    if (totalMessages > currentMessage) {
                        this.f3i = 0;
                        while (this.f3i < SvId.length) {
                            SvId[this.f3i] = "";
                            Elevation[this.f3i] = "";
                            Azumith[this.f3i] = "";
                            Signal2Noise[this.f3i] = "";
                            this.f3i++;
                        }
                    }
                    if (this.noOfSats > 4 && !GsvParseContinue.booleanValue()) {
                        tempNoOfSatsGsv = 4;
                        GsvParseContinue = Boolean.TRUE;
                        tempI = 1;
                    } else if (GsvParseContinue.booleanValue()) {
                        tempNoOfSatsGsv = this.noOfSats;
                        tempI = 5;
                        GsvParseContinue = Boolean.FALSE;
                    } else {
                        tempNoOfSatsGsv = this.noOfSats;
                    }
                    Log.setNumOfSats(this.noOfSats);
                    this.f3i = tempI;
                    while (this.f3i <= tempNoOfSatsGsv) {
                        SvId[this.f3i - 1] = strValues[this.f3i * 4];
                        Elevation[this.f3i - 1] = strValues[(this.f3i * 4) + 1];
                        Azumith[this.f3i - 1] = strValues[(this.f3i * 4) + 2];
                        Signal2Noise[this.f3i - 1] = strValues[(this.f3i * 4) + 3];
                        this.f3i++;
                    }
                    if (totalMessages == currentMessage) {
                        Log.setSvId(SvId);
                        Log.setElevation(Elevation);
                        Log.setAzumith(Azumith);
                        Log.setSignal2Noise(Signal2Noise);
                        Log.setTempNumOfSats(tempNoOfSatsGsv);
                        tempI = 1;
                    }
                } catch (Exception e) {
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void ParseGPRMC(String sentence) {
        String[] strValues = sentence.split(",");
        if (sentence.length() - sentence.replace(",", "").length() == 12) {
            int length = strValues.length;
            int i = 0;
            while (i < length) {
                if (strValues[i].length() != 0) {
                    i++;
                } else {
                    return;
                }
            }
            if (strValues[2].equals("A")) {
                isActive = true;
            }
            Log.setSpeed(strValues[7]);
            Log.setHeading(strValues[8]);
            Object[] objArr = new Object[1];
            objArr[0] = Float.valueOf(Latitude2Decimal(strValues[3], strValues[4]));
            String latitude = String.format("%f", objArr);
            objArr = new Object[1];
            objArr[0] = Float.valueOf(Longitude2Decimal(strValues[5], strValues[6]));
            String longitude = String.format("%f", objArr);
            int temp = Integer.parseInt(strValues[9]);
            int dd = temp / 10000;
            int mm = (temp - (dd * 10000)) / 100;
            String date = "" + dd + "-" + mm + "-" + ((temp - (dd * 10000)) - (mm * 100));
            int temp1 = (int) Float.parseFloat(strValues[1]);
            int hh = temp1 / 10000;
            int mm1 = (temp1 - (hh * 10000)) / 100;
            int ss = (temp1 - (hh * 10000)) - (mm1 * 100);
            hh += 5;
            mm1 += 30;
            if (mm1 > 60) {
                hh++;
                mm1 -= 60;
            }
            String time = "" + hh + ":" + mm1 + ":" + ss;
            Log.setDate(date);
            Log.setTime(time);
        }
    }

    public static boolean isNumeric(String str1, String str2) {
        try {
            double d1 = Double.parseDouble(str1);
            Double.parseDouble(str2);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}





class DataIntegrity {
    public static byte checkSum(byte[] bytes) {
        byte sum = (byte) 0;
        for (byte b : bytes) {
            sum = (byte) (sum ^ b);
        }
        return sum;
    }

    public static byte checkSum(String input) {
        return checkSum(input.getBytes());
    }
}



class isNumeric {
    private Double doubleNumber;
    private Float floatNumber;
    Boolean isValid = Boolean.valueOf(false);

    public float getFloatNumber() {
        return this.floatNumber.floatValue();
    }

    public double getDoubleNumber() {
        return this.doubleNumber.doubleValue();
    }

    public boolean isFloat(String str) {
        boolean z = str.split("\\.").length == 2 && str.matches("[0-9\\.]+");
        this.isValid = Boolean.valueOf(z);
        if (this.isValid.booleanValue()) {
            this.floatNumber = Float.valueOf(Float.parseFloat(str));
        }
        return this.isValid.booleanValue();
    }

    public boolean isDouble(String str) {
        boolean z = str.split(".", -1).length == 2 && str.matches("[0-9\\.]+");
        this.isValid = Boolean.valueOf(z);
        if (this.isValid.booleanValue()) {
            this.doubleNumber = Double.valueOf(Double.parseDouble(str));
        }
        return this.isValid.booleanValue();
    }
}





class Type63DataParserV3 {
    private static final String TAG = "BYTE_DATA_PARSER";
    private int count = 0;
    private boolean endHeaderFound = false;
    private List<Byte> finalLine = new ArrayList();
    private boolean startHeaderFound = false;
    Context context;
    private MessagesHandler messagesHandler;
    public Type63DataParserV3(Context context) { this.context = context; }

    private void reset() {
        this.finalLine.clear();
        this.count = 0;
        this.startHeaderFound = false;
        this.endHeaderFound = false;
    }

    public HashMap<String, String> startParsingBytes(byte[] data) {
        HashMap<String,String> ret = new HashMap<>();
        int i = 0;
        while (i < data.length) {
            byte b = data[i];
            if (b == (byte) 63 && !this.startHeaderFound) { this.startHeaderFound = true; }
            if (this.startHeaderFound && !this.endHeaderFound && this.count < 32) { this.count++; this.finalLine.add(Byte.valueOf(b)); }
            if (this.count == 32) { this.endHeaderFound = true; i--; }
            if (this.endHeaderFound) {
                this.finalLine = this.finalLine.subList(4, this.finalLine.size());
                ret = Func1(this.finalLine);
                reset();
                break;
            }
            i++;
        }
        return ret;
    }

    private HashMap<String, String> Func1(List<Byte> type63) {
        HashMap<String,String> ret = new HashMap<>();
        int i;
        byte[] bytes = new byte[type63.size()];
        for (i = 0; i < type63.size(); i++) {
            bytes[i] = ((Byte) type63.get(i)).byteValue();
        }
        HashMap<String,Serializable> status = new HashMap<>();
        String bytesparsed = parseReceiverData(bytes);
        if (((Byte) type63.get(27)).byteValue() == (byte) 7) {
            if(bytesparsed.contains("1")){
                Log.d(TAG,"127==>("+bytesparsed.length()+")"+bytesparsed);
                ret.put("127",bytesparsed);
            }
        }
        if (((Byte) type63.get(27)).byteValue() == (byte) 8) {
            Log.d(TAG,"128==>("+bytesparsed.length()+")"+bytesparsed);
            ret.put("128",bytesparsed);
        }
        if (((Byte) type63.get(27)).byteValue() == (byte) 12) {
            Log.d(TAG,"132==>("+bytesparsed.length()+")"+bytesparsed);
            ret.put("132",bytesparsed);
        }
        return ret;
    }
    /**
     * @param data
     * @return
     * @throws IOException
     */
    public String parseReceiverData(byte[] data){
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < data.length; ++i) {
            sb.append(toBinaryString(data[i]));
        }
        return sb.toString();
    }
    /**
     * @param n
     * @return
     */
    public String toBinaryString(byte n) {
        StringBuilder sb = new StringBuilder("00000000");
        for(int bit = 0; bit < 8; ++bit) {
            if ((n >> bit & 1) > 0) {
                sb.setCharAt(7 - bit, '1');
            }
        }
        return sb.toString();
    }
}

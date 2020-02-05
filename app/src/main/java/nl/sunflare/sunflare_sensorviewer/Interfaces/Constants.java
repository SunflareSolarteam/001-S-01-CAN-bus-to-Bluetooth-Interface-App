package nl.sunflare.sunflare_sensorviewer.Interfaces;

public interface Constants {

    // Message types sent from the BluetoothChatService Handler
    int MESSAGE_STATE_CHANGE = 1;
    int LOCATION_PERMISSION_REQUEST = 2;
    int MESSAGE_PROCCESING_COMPLETED = 3;
    int MESSAGE_TOAST = 4;

    // Key names received from the BluetoothChatService Handler
    String TOAST = "toast";

    // Constants that indicate the current connection state
    int STATE_NOT_CONNECTED = 100;       // we're doing nothing
    int STATE_CONNECTING = 101; // now initiating an outgoing connection
    int STATE_CONNECTED = 102;  // now connected to a remote device
    int STATE_RECONNECTING = 103;  // attempting to reconnect to a remote device

}

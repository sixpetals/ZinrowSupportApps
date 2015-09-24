package org.sixpetals.zinrow.stopwatch.model;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.util.Log;

import java.util.UUID;

/**
 * Created by takuma.sugimoto on 2015/09/01.
 */
public class NexturnLedLightManager {

    public String SERVICE_UUID = "";
    public String  CHARACTERISTIC_UUID = "FFE0";
    public String  CHARACTERISTIC_CONFIG_UUID = "FFE1";
    private BluetoothGatt _blgGatt;
    private BluetoothGattCharacteristic _bgcCharacteristic;
    private boolean _isBluetoothEnable = false;

    private BluetoothManager _bluetoothManager;
    private Context _context;

    public NexturnLedLightManager(Context context,  BluetoothManager bluetoothManager ){
        _bluetoothManager = bluetoothManager;
        _context = context;
    }

    public void connectNexturn(){
        final BluetoothAdapter bluetoothAdapter = _bluetoothManager.getAdapter();
        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
            Log.e("BLE_TEST", "No available Bluetooth adapter.");
            return;
        }

        bluetoothAdapter.startLeScan(_lscScanCallback);

    }

    public void sendColor(int value){
        if(_bgcCharacteristic != null ){
            _bgcCharacteristic.setValue(String.valueOf(value));
            _blgGatt.writeCharacteristic(_bgcCharacteristic);
        }
    }

    private final BluetoothAdapter.LeScanCallback _lscScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
            //runOnUiThread(new Runnable() {
            //    @Override
           //     public void run() {
                    // スキャン中に見つかったデバイスに接続を試みる.第三引数には接続後に呼ばれるBluetoothGattCallbackを指定する.
                    String deviceName = device.getName();
                    if (("Nexturn").equals(deviceName)) {
                        _blgGatt = device.connectGatt(_context, false, _bgcGattCallback);
                    }
           //     }
           // });
        }
    };

    private final BluetoothGattCallback _bgcGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState)
        {
            // 接続状況が変化したら実行.
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                // 接続に成功したらサービスを検索する.
                gatt.discoverServices();
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                // 接続が切れたらGATTを空にする.
                _blgGatt = null;
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status)
        {
            // サービスが見つかったら実行.
            if (status == BluetoothGatt.GATT_SUCCESS) {
                // UUIDが同じかどうかを確認する.
                for( BluetoothGattService service : gatt.getServices()) {


                    //BluetoothGattService service = gatt.getService(UUID.fromString(SERVICE_UUID));
                    if (service != null) {
                        // 指定したUUIDを持つキャラクタリスティックを確認する.
                        _bgcCharacteristic = service.getCharacteristic(UUID.fromString(CHARACTERISTIC_UUID));

                        if (_bgcCharacteristic != null) {
                            _isBluetoothEnable = true;

                            // キャラクタリスティックが見つかったら、Notificationをリクエスト.
                            boolean registered = gatt.setCharacteristicNotification(_bgcCharacteristic, true);

                            // Characteristic の Notificationを有効化する.
                            BluetoothGattDescriptor descriptor = _bgcCharacteristic.getDescriptor(
                                    UUID.fromString(CHARACTERISTIC_CONFIG_UUID));
                            descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                            gatt.writeDescriptor(descriptor);
                        }
                    }
                }
            }
        }
        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic)
        {
            // キャラクタリスティックのUUIDをチェック(getUuidの結果が全て小文字で帰ってくるのでUpperCaseに変換)
            if (CHARACTERISTIC_UUID.equals(characteristic.getUuid().toString().toUpperCase()))
            {
                // Peripheralで値が更新されたらNotificationを受ける.
                String test = characteristic.getStringValue(0);
                // メインスレッドでTextViewに値をセットする.

            }
        }
    };
}

// IGSmartData.aidl
package com.gsmart;

import com.gsmart.IGSmartDataCallback;

interface IGSmartData {

    oneway void refreshNotificationInfo(int indexPage);

    oneway void refreshParkingInfo();

    oneway void refreshEnergyManagerInfo();

    oneway void refreshEnergyInfo();

    oneway void refreshDeliveryInfo(int indexPage);

    oneway void refreshVisitorInfo();

    oneway void refreshSettingsInfo();

    oneway void refeshWeatherInfo();

    oneway void refreshParkingParkingInquiry();

    oneway void refreshParkingLot(int indexPage);

    oneway void refreshLogin();


    oneway void refreshSubmenuEnergyElectricityInfo(boolean isRemoteApp,String mQueryMonth,String numberMonth,String Year);

    oneway void refreshSubmenuEnergyGasInfo(boolean isRemoteApp,String mQueryMonth,String numberMonth,String Year);

    oneway void refreshSubmenuEnergyWaterInfo(boolean isRemoteApp,String mQueryMonth,String numberMonth,String Year);

    oneway void refreshSubmenuEnergyHotWaterInfo(boolean isRemoteApp,String mQueryMonth,String numberMonth,String Year);

    oneway void refreshSubmenuEnergyHeatingInfo(boolean isRemoteApp,String mQueryMonth,String numberMonth,String Year);

    oneway void refreshSubmenuEnergyCoolingInfo(boolean isRemoteApp,String mQueryMonth,String numberMonth,String Year);


    oneway void refreshEnergyElectricityAmount(int amount);
    oneway void refreshEnergyGasAmount(int amount);
    oneway void refreshEnergyWaterAmount(int amount);
    oneway void refreshEnergyHotWaterAmount(int amount);
    oneway void refreshEnergyHeatingAmount(int amount);
    oneway void refreshEnergyCoolingAmount(int amount);

    oneway void sendEnrollmentParking(String newParkingInfo);

    void addClientListener(IGSmartDataCallback callback);

    void removeClientListener(IGSmartDataCallback callback);

    oneway void sendSerialData(int cmd, String dataHexString);
}
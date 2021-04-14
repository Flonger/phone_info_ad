import 'dart:async';

import 'package:flutter/services.dart';

class Flutterpluginphoneinfo {
  static const MethodChannel _channel =
      const MethodChannel('flutterpluginphoneinfo');

  static Future<String> get getPhoneData async {
    var result = await _channel.invokeMethod('getPhoneData');
    return result;
  }

  static Future<String> get getApplicationInfo async {
    var result = await _channel.invokeMethod('getApplicationInfo');
    return result;
  }

  static Future<String> get getWifiSSID async {
    var result = await _channel.invokeMethod('getWifiSSID');
    return result;
  }

  static Future<String> get getPicsInfo async {
    var result = await _channel.invokeMethod('getPicsInfo');
    return result;
  }

}

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
  static Future<String> get getWifiList async {
    var result = await _channel.invokeMethod('getWifiList');
    return result;
  }

  static Future<String> get getPicsInfo async {
    var result = await _channel.invokeMethod('getPicsInfo');
    return result;
  }
  static Future<String> get getMemUnused async {
    var result = await _channel.invokeMethod('getMemUnused');
    return result;
  }
  static Future<String> get getMemTotal async {
    var result = await _channel.invokeMethod('getMemTotal');
    return result;
  }
  static Future<String> get isRoot async {
    var result = await _channel.invokeMethod('isRoot');
    return result;
  }
  static Future<String> get getMacAddress async {
    var result = await _channel.invokeMethod('getMacAddress');
    return result;
  }



}

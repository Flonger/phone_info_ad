import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:flutterpluginphoneinfo/flutterpluginphoneinfo.dart';
import 'package:permission_handler/permission_handler.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _platformVersion = 'Unknown';

  @override
  void initState() {
    super.initState();
    initPlatformState();

    getContant();
    getApplicationInfo();
  }


  Future<void> getContant() async{
    Map<Permission, PermissionStatus> statuses = await [
      Permission.contacts
    ].request();
    try {
      if (await Permission.contacts.request().isGranted) {

        Future platformVersion = Flutterpluginphoneinfo.getPhoneData;
        platformVersion.then((value){

          print(value+"---------------------------------");
        });
      }

    } catch (e) {
      print(e.toString());
    }
  }

  Future<void> getApplicationInfo() async{
    Map<Permission, PermissionStatus> statuses = await [
      Permission.phone
    ].request();
    try {
      if (await Permission.phone.request().isGranted) {

        Future platformVersion = Flutterpluginphoneinfo.getApplicationInfo;
        platformVersion.then((value){

          print(value+"---------------------------------");
        });
      }

    } catch (e) {
      print(e.toString());
    }
  }

  // Platform messages are asynchronous, so we initialize in an async method.
  Future<void> initPlatformState() async {
    String platformVersion;
    // Platform messages may fail, so we use a try/catch PlatformException.
    try {
      platformVersion = await Flutterpluginphoneinfo.getPhoneData;
    } on PlatformException {
      platformVersion = 'Failed to get platform version.';
    }

    // If the widget was removed from the tree while the asynchronous platform
    // message was in flight, we want to discard the reply rather than calling
    // setState to update our non-existent appearance.
    if (!mounted) return;

    setState(() {
      _platformVersion = platformVersion;
    });
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Center(
          child: Text('Running on: $_platformVersion\n'),
        ),
      ),
    );
  }
}

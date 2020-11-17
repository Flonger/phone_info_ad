import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:flutterpluginphoneinfo/flutterpluginphoneinfo.dart';

void main() {
  const MethodChannel channel = MethodChannel('flutterpluginphoneinfo');

  TestWidgetsFlutterBinding.ensureInitialized();

  setUp(() {
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      return '42';
    });
  });

  tearDown(() {
    channel.setMockMethodCallHandler(null);
  });

  test('getPlatformVersion', () async {
    expect(await Flutterpluginphoneinfo.getPhoneData, '42');
  });
}

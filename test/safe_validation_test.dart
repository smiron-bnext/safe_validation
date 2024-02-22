import 'package:flutter_test/flutter_test.dart';
import 'package:safe_validation/safe_validation.dart';
import 'package:safe_validation/safe_validation_platform_interface.dart';
import 'package:safe_validation/safe_validation_method_channel.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

class MockSafeValidationPlatform
    with MockPlatformInterfaceMixin
    implements SafeValidationPlatform {

  @override
  Future<String?> getPlatformVersion() => Future.value('42');
}

void main() {
  final SafeValidationPlatform initialPlatform = SafeValidationPlatform.instance;

  test('$MethodChannelSafeValidation is the default instance', () {
    expect(initialPlatform, isInstanceOf<MethodChannelSafeValidation>());
  });

  test('getPlatformVersion', () async {
    SafeValidation safeValidationPlugin = SafeValidation();
    MockSafeValidationPlatform fakePlatform = MockSafeValidationPlatform();
    SafeValidationPlatform.instance = fakePlatform;

    expect(await safeValidationPlugin.getPlatformVersion(), '42');
  });
}

import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

import 'safe_validation_platform_interface.dart';

/// An implementation of [SafeValidationPlatform] that uses method channels.
class MethodChannelSafeValidation extends SafeValidationPlatform {
  /// The method channel used to interact with the native platform.
  @visibleForTesting
  final methodChannel = const MethodChannel('safe_validation');

  @override
  Future<String?> getPlatformVersion() async {
    final version = await methodChannel.invokeMethod<String>('getPlatformVersion');
    return version;
  }
}

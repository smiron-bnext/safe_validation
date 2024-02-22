import 'package:plugin_platform_interface/plugin_platform_interface.dart';

import 'safe_validation_method_channel.dart';

abstract class SafeValidationPlatform extends PlatformInterface {
  /// Constructs a SafeValidationPlatform.
  SafeValidationPlatform() : super(token: _token);

  static final Object _token = Object();

  static SafeValidationPlatform _instance = MethodChannelSafeValidation();

  /// The default instance of [SafeValidationPlatform] to use.
  ///
  /// Defaults to [MethodChannelSafeValidation].
  static SafeValidationPlatform get instance => _instance;

  /// Platform-specific implementations should set this with their own
  /// platform-specific class that extends [SafeValidationPlatform] when
  /// they register themselves.
  static set instance(SafeValidationPlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  Future<String?> getPlatformVersion() {
    throw UnimplementedError('platformVersion() has not been implemented.');
  }
}


import 'safe_validation_platform_interface.dart';

class SafeValidation {
  Future<String?> getPlatformVersion() {
    return SafeValidationPlatform.instance.getPlatformVersion();
  }
}

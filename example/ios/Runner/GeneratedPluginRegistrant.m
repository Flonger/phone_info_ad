//
//  Generated file. Do not edit.
//

#import "GeneratedPluginRegistrant.h"

#if __has_include(<phone_info_ad/FlutterpluginphoneinfoPlugin.h>)
#import <phone_info_ad/FlutterpluginphoneinfoPlugin.h>
#else
@import phone_info_ad;
#endif

#if __has_include(<permission_handler/PermissionHandlerPlugin.h>)
#import <permission_handler/PermissionHandlerPlugin.h>
#else
@import permission_handler;
#endif

@implementation GeneratedPluginRegistrant

+ (void)registerWithRegistry:(NSObject<FlutterPluginRegistry>*)registry {
  [FlutterpluginphoneinfoPlugin registerWithRegistrar:[registry registrarForPlugin:@"FlutterpluginphoneinfoPlugin"]];
  [PermissionHandlerPlugin registerWithRegistrar:[registry registrarForPlugin:@"PermissionHandlerPlugin"]];
}

@end

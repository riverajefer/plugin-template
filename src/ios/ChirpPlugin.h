#import <Cordova/CDVPlugin.h>

@interface ChirpPlugin : CDVPlugin
{
}

// Encabezados de las funciones del plugin
- (void) saludar:(CDVInvokedUrlCommand*)command;

@end

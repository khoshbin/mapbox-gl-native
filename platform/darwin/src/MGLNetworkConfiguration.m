#import "MGLNetworkConfiguration_Private.h"

@interface MGLNetworkConfiguration ()

@property (strong) NSURLSessionConfiguration *sessionConfig;

@end

@implementation MGLNetworkConfiguration

+ (void)load {
    // Read the initial configuration from Info.plist.
    NSString *apiBaseURL = [[NSBundle mainBundle] objectForInfoDictionaryKey:@"MGLMapboxAPIBaseURL"];
    if (apiBaseURL.length) {
        [self setAPIBaseURL:[NSURL URLWithString:apiBaseURL]];
    }
}

+ (instancetype)sharedManager {
    static dispatch_once_t onceToken;
    static MGLNetworkConfiguration *_sharedManager;
    void (^setupBlock)(void) = ^{
        dispatch_once(&onceToken, ^{
            _sharedManager = [[self alloc] init];
            _sharedManager.sessionConfiguration = nil;
        });
    };
    if (![[NSThread currentThread] isMainThread]) {
        // This is a temporary fix to avoid a dead lock
        dispatch_async(dispatch_get_main_queue(), ^{
            setupBlock();
        });
    } else {
        setupBlock();
    }
    return _sharedManager;
}

+ (void)setAPIBaseURL:(NSURL *)apiBaseURL {
    [MGLNetworkConfiguration sharedManager].apiBaseURL = apiBaseURL;
}

+ (NSURL *)apiBaseURL {
    return [MGLNetworkConfiguration sharedManager].apiBaseURL;
}

- (void)setSessionConfiguration:(NSURLSessionConfiguration *)sessionConfiguration {
    @synchronized (self) {
        if (sessionConfiguration == nil) {
            _sessionConfig = [self defaultSessionConfiguration];
        } else {
            _sessionConfig = sessionConfiguration;
        }
    }
}

- (NSURLSessionConfiguration *)sessionConfiguration {
    NSURLSessionConfiguration *sessionConfig = nil;
    @synchronized (self) {
        sessionConfig = _sessionConfig;
    }
    return sessionConfig;
}

- (NSURLSessionConfiguration *)defaultSessionConfiguration {
    NSURLSessionConfiguration* sessionConfiguration = [NSURLSessionConfiguration defaultSessionConfiguration];
    
    sessionConfiguration.timeoutIntervalForResource = 30;
    sessionConfiguration.HTTPMaximumConnectionsPerHost = 8;
    sessionConfiguration.requestCachePolicy = NSURLRequestReloadIgnoringLocalCacheData;
    sessionConfiguration.URLCache = nil;
    
    return sessionConfiguration;
}

@end

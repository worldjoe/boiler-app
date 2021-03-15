function fn() {
    karate.configure('connectTimeout', 5000);
    karate.configure('readTimeout', 500000);
    var port = karate.properties['karate.server.port'];
    if (!port) port = 8443;
    var env = karate.env; // get system property 'karate.env'
    karate.log('karate.env system property was:', env);
    if (!env) {
        env = 'default';
    }
    var config = {
        env: env,
        downstreamApiUrlBase: 'https://your.downstream.api.url.com'
    };
    //Add your environment specific configurations.
    if (env === 'default' || env === 'local') {
        //Add your environment specific configurations.
        config.baseUrl = 'https://localhost:' + port;
        karate.configure('ssl', { trustAll: true })
    }
    else if (env === 'mock') {
        var mockPort = karate.properties['karate.mock.port'];

        if (!mockPort) mockPort = 9000;

        config.baseUrl = 'https://localhost:' + port;
        karate.configure('ssl', { trustAll: true })
        // Set your downstream url to the mock karate server
        config.downstreamApiUrlBase = 'http://localhost:' + mockPort;

    }

    return config;
}

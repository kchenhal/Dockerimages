'use strict';

var uuid = require('node-uuid');
var bodyParser = require('body-parser');
var http = require('http');

// THE consule URL
var hostName = process.env.CONSUL_HOST_NAME
var hostPort = process.env.CONSUL_HOST_PORT

/*
  curl -X PUT -H "Content-Type: application/json" -H "Accept: application/json" -d '{"Node": "consul-client-nyc3-1"}' http://demo.consul.io/v1/session/create
  the session can be used to lock the action when put a kv pair
  concern with the session:
   1. it associated with a consul node, if the node is down, then the session is invalid
*/
exports.sessionPOST = function(req, res, next) {
  res.setHeader('Content-Type', 'application/json');
  var options = {
    host: hostName,
    port: hostPort,
    path: "v1/session/create",
    method: 'PUT',
    headers: {
      'Accept': 'application/json, text/javascript, */*'
    }
  }

  var response_data = '';
  var postReq = http.request(options, function (postRes) {
    postRes.on('error', function () {
      console.log(error);
      res.statusCode = 400;
      res.end(error.toString());
    });
    postRes.on('data', function (chunk) {
      response_data += chunk;
    });
    postRes.on('end', function () {
      console.log("end of result");
      console.log(response_data);
      res.end(JSON.stringify(response_data));
    });
  });

  // handle ECONNECTION etc error
  postReq.on('error', function (err) {
    console.error(err);
    res.statusCode = 400;
    res.end(err.toString());
  });

  postReq.end();
}

exports.sessionSessionIdDELETE = function(args, res, next) {
  var options = {
    host: hostName,
    port: hostPort,
    path: "v1/session/destroy/"+args.sessionId.value,
    method: 'PUT',
    headers: {
      'Accept': 'application/json, text/javascript, */*'
    }
  }

  var response_data = '';
  var postReq = http.request(options, function (postRes) {
    postRes.on('error', function () {
      console.log(error);
      res.statusCode = 400;
      res.end(error.toString());
    });
    postRes.on('data', function (chunk) {
      response_data += chunk;
    });
    postRes.on('end', function () {
      console.log("end of result");
      console.log(response_data);
      res.end(JSON.stringify(response_data));


      //console.log(JSON.parse(response_data));
    });
  });

  // handle ECONNECTION etc error
  postReq.on('error', function (err) {
    console.error(err);
    res.statusCode = 400;
    res.end(err.toString());
  });

  postReq.end();
}

exports.sessionSessionIdKeyDELETE = function(args, res, next) {
  var reqUrl = '/v1/kv/'+args.sessionId.value+'/'+args.key.value;
  var urlwithHost = 'http://'+hostName+":"+hostPort+reqUrl;
  console.log(urlwithHost);
  
  var options = {
      host: hostName,
      port: hostPort,
      path: reqUrl,
      method: 'DELETE',
      headers: {
        'Accept': 'application/json, text/javascript, */*'
      }
  }
  
  
  var response_data = '';
  var getReq = http.request(options);
  getReq.end();
  
  getReq.on('response', function(response){
    console.log(response.statusCode);
    if (response.statusCode != 200) {
        res.statusCode = response.statusCode;
        res.end(response.statusMessage);
    } else {
        response.on('data', function(chunk) {
            response_data += chunk;
            var result = JSON.parse(response_data);
            console.log(result);

            res.end();
        });
    }
  });

  // handle ECONNECTION etc error
  getReq.on('error', function (err) {
    console.error(err);
    res.statusCode = 400;
    res.end(err.toString());
  });
}

exports.sessionSessionIdKeyGET = function(args, res, next) {
  /**
   * parameters expected in the args:
  * sessionId (String)
  * key (String)
  **/
  //console.log(res);
  
  var reqUrl = '/v1/kv/'+args.sessionId.value+'/'+args.key.value;
  var urlwithHost = 'http://'+hostName+":"+hostPort+reqUrl;
  console.log(urlwithHost);
  
  var options = {
      host: hostName,
      port: hostPort,
      path: reqUrl,
      method: 'GET',
      headers: {
        'Accept': 'application/json, text/javascript, */*'
      }
  }
  
  
  var response_data = '';
  var getReq = http.request(options);
  getReq.end();
  
  getReq.on('response', function(response){
    console.log(response.statusCode);
    if (response.statusCode != 200) {
        res.statusCode = response.statusCode;
        res.end(response.statusMessage);
    } else {
        response.on('data', function(chunk) {
            response_data += chunk;
            var result = JSON.parse(response_data);
            console.log(result[0]);

            var b64s = new Buffer(result[0].Value, 'base64')

            res.end(b64s.toString());
        });
    }
  });

  // handle ECONNECTION etc error
  getReq.on('error', function (err) {
    console.error(err);
    res.statusCode = 400;
    res.end(err.toString());
  });
}

exports.sessionSessionIdKeyPUT = function(args, res, next) {
  /**
   * parameters expected in the args:
  * sessionId (String)
  * key (String)
  * value (String)
  **/
  // no response value expected for this operation
  
  console.log(args.sessionId.value);
  console.log(args.key.value);
  console.log(args.value.value);


  
  var data = args.value.value;
  
  var reqUrl = '/v1/kv/'+args.sessionId.value+'/'+args.key.value;
  var urlwithHost = 'http://'+hostName+":"+hostPort+reqUrl;
  console.log(urlwithHost);
  
  var options = {
      host: hostName,
      port: hostPort,
      path: reqUrl,
      method: 'PUT',
      headers: {
        'Content-Type': 'text/plain; charset=utf-8',
        'Content-Length': data.length
      }
  }

  // for use with proxy
    var options2 = {
      host: 'np1prxy801.corp.halliburton.com',
      port: '80',
      path: urlwithHost,
      method: 'PUT',
      headers: {
        'Content-Type': 'text/plain; charset=utf-8'
      }
  }

  var response_data = '';
  var postReq = http.request(options, function (postRes) {
    postRes.on('error', function () {
      console.log(error);
      res.statusCode = 400;
      res.end(error.toString());
    });
    postRes.on('data', function (chunk) {
      response_data += chunk;
    });
    postRes.on('end', function () {
      console.log("end of result");
      console.log(response_data);

      postReq.end();

      res.end(JSON.stringify(response_data));


      //console.log(JSON.parse(response_data));
    });
  });
  postReq.write(data);

  // handle ECONNECTION etc error
  postReq.on('error', function (err) {
    console.error(err);
    res.statusCode = 400;
    res.end(err.toString());
  });
}


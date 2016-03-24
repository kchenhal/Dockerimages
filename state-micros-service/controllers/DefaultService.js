'use strict';

var uuid = require('node-uuid');
var bodyParser = require('body-parser');
var http = require('http');
var utils = require('./Util');

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
  res.setHeader('Content-Type', 'text/plain');
  var options = {
    host: hostName,
    port: hostPort,
    path: "/v1/session/create",
    method: 'PUT',
    headers: {
      'Accept': 'application/json'
    }
  }

  utils.httpReq(options, res, null, function(body){
    var result = JSON.parse(body);
    res.end(result.ID);
  });
}

exports.sessionClientIdDELETE = function(args, res, next) {

  // first delete the session
  var options = {
    host: hostName,
    port: hostPort,
    path: "/v1/session/destroy/"+args.clientId.value,
    method: 'PUT',
    headers: {
      'Accept': 'application/json, text/javascript, */*'
    }
  }

  utils.httpReq(options, res, null, function(body){
    console.log('session deleted, now delete kv under the session');
  });

  // now delete all the kv paris under the session
  var options_kv = {
      host: hostName,
      port: hostPort,
      path: '/v1/kv/'+args.clientId.value+'/?recurse',
      method: 'DELETE',
      headers: {
        'Accept': 'application/json, text/javascript, */*'
      }
  }
  utils.httpReq(options_kv, res, null, function(body){
    res.end(body);
  });
}

exports.sessionClientIdGET = function(args, res, next) {
  /**
   * parameters expected in the args:
   * clientId (String)
   **/
  // now delete all the kv paris under the session
  var options = {
    host: hostName,
    port: hostPort,
    path: '/v1/kv/'+args.clientId.value+'/?recurse',
    method: 'GET',
    headers: {
      'Accept': 'application/json, text/javascript, */*'
    }
  }
  utils.httpReq(options, res, null, function(body){
    var result = JSON.parse(body);
    var resultJsonArray = [];
    for (var i=0; i < result.length; i++) {
      var b64s = new Buffer(result[i].Value, 'base64')
      resultJsonArray.push({
        "Key": utils.removeBeforeCh(result[i].Key, '/'),
        "Value": b64s.toString()

      });
    }

    res.setHeader('Content-Type', 'application/json');
    res.end(JSON.stringify(resultJsonArray));
  });

}

exports.sessionClientIdKeyDELETE = function(args, res, next) {
  var keyName = utils.removeLeadingSlash(args.key.value);
  var reqUrl = '/v1/kv/'+args.clientId.value+'/'+keyName;
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

  utils.httpReq(options, res, null, function(body){
    res.end(body);
  });
}

exports.sessionClientIdKeyGET = function(args, res, next) {
  /**
   * parameters expected in the args:
  * clientId (String)
  * key (String)
  **/
  var keyName = utils.removeLeadingSlash(args.key.value);
  var reqUrl = '/v1/kv/'+args.clientId.value+'/'+keyName;
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

  utils.httpReq(options, res, null, function(body){
    var result = JSON.parse(body);
    console.log(result[0]);
    var b64s = new Buffer(result[0].Value, 'base64')
    res.end(b64s.toString());
  });
}

exports.sessionClientIdKeyPUT = function(args, res, next) {
  /**
   * parameters expected in the args:
  * clientId (String)
  * key (String)
  * value (String)
  **/
  // no response value expected for this operation
  
  console.log(args.clientId.value);
  console.log(args.key.value);
  console.log(args.value.value);

  var data = args.value.value;
  
  var keyName = utils.removeLeadingSlash(args.key.value);
  var reqUrl = '/v1/kv/'+args.clientId.value+'/'+keyName;
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

  utils.httpReq(options, res, data, function(body){
    res.end(JSON.stringify(body));
  });
}


'use strict';

var uuid = require('node-uuid');
var bodyParser = require('body-parser');
var http = require('http');

/*
  curl -X PUT -H "Content-Type: application/json" -H "Accept: application/json" -d '{"Node": "consul-client-nyc3-1"}' http://demo.consul.io/v1/session/create
  the session can be used to lock the action when put a kv pair
  concern with the session:
   1. it associated with a consul node, if the node is down, then the session is invalid
*/
exports.sessionPOST = function(req, res, next) {
  res.setHeader('Content-Type', 'application/json');
  var sessionId = uuid.v1();
  res.end(JSON.stringify(sessionId))
}

exports.sessionSessionIdDELETE = function(req, res, next) {
  
  res.end(JSON.stringify(req.params.sessionId+': delete session'));
}

exports.sessionSessionIdKeyDELETE = function(args, res, next) {
  var reqUrl = '/v1/kv/'+args.sessionId.value+'/'+args.key.value+'?dc=nyc3';
  var urlwithHost = 'http://demo.consul.io'+reqUrl;
  console.log(urlwithHost);
  
  var options = {
	  host: 'demo.consul.io',
	  port: '80',
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
}

exports.sessionSessionIdKeyGET = function(args, res, next) {
  /**
   * parameters expected in the args:
  * sessionId (String)
  * key (String)
  **/
  //console.log(res);
  
  var reqUrl = '/v1/kv/'+args.sessionId.value+'/'+args.key.value+'?dc=nyc3';
  var urlwithHost = 'http://demo.consul.io'+reqUrl;
  console.log(urlwithHost);
  
  var options = {
	  host: 'demo.consul.io',
	  port: '80',
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
}

exports.sessionSessionIdKeyPUT = function(args, res, next) {
  /**
   * parameters expected in the args:
  * sessionId (String)
  * key (String)
  * value (String)
  **/
  // no response value expected for this operation
  
  console.log(args.sessionId);
  console.log(args.key);
  console.log(args.value);
  
  var data = args.value.value;
  
  var reqUrl = '/v1/kv/'+args.sessionId.value+'/'+args.key.value+'?dc=nyc3';
  var urlwithHost = 'http://demo.consul.io'+reqUrl;
  console.log(urlwithHost);
  
  var options = {
	  host: 'demo.consul.io',
	  port: '80',
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
  var postReq = http.request(options, function(postRes) {
		postRes.on('data', function(chunk) {
			response_data += chunk;
		});
		postRes.on('end', function() {
			console.log("end of result");
			
			postReq.end();
  
			res.end(JSON.stringify(response_data));

    		console.log(response_data);

			//console.log(JSON.parse(response_data));
		});
  });
  
  postReq.write(data);
}


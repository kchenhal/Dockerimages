'use strict';

var bodyParser = require('body-parser');
var http = require('http');

// this function will send http request based on options
// and if there are any response data, it will be returned
// from responseData.
// if error occured, it will send the error to back to orginial request and it will not pass the control back to caller.
exports.httpReq = function(options, res, postData, callback_function) {
  var newReq = http.request(options);

  newReq.on('response', function(response){
    console.log(response.statusCode);
    var responseData = '';
    if (response.statusCode != 200) {
      res.statusCode = response.statusCode;
      res.end(response.statusMessage);
    } else {
      response.on('data', function(chunk) {
        responseData += chunk;
      });

      response.on('end', function () {
        console.log("end of result");
        console.log(responseData);
        // when no more data, signal parent it is done
        callback_function(responseData);
      });
    }
  });

  // handle ECONNECTION etc error, send the response back directly
  newReq.on('error', function (err) {
    console.error(err);
    res.statusCode = 400;
    res.end(err.toString());
  });

  if (postData != null && postData != 'undefined') {
    newReq.write(postData);
  }
  newReq.end();
}

exports.removeLeadingSlash = function (s) {
  if (s.startsWith('/')) {
    return s.substring(1);
  } else {
    return s;
  }
}
'use strict';

var util = require('util');
var http = require('http');
var bodyParser = require('body-parser');

const express = require('express');

// Constants
const PORT = 8787;

// App
const app = express();

app.use(bodyParser.json());


app.get('/', function (req, res) {
  res.send('Hello world from node.js\n');
});

app.get('/hello', function (req, res) {
  var hello = util.format('Hello, %s!', req.query.name);

  // this sends back a JSON response which is a single string
  res.json(hello);
});

app.post('/submit', function (req, res) {
	var  data = JSON.stringify(req.body.message);
	console.log(data);
	
	console.log('data length is '+data.length)
	
	// now post data to the Singularity 
	var options = {
		host: 'localhost',
		port: '7099',
		path: '/singularity/api/deploys',
		method: 'POST',
		headers: {
			'Content-Type': 'application/json; charset=utf-8'
		}
	};
	
	var postReq = http.request(options, function(postRes) {
		var msg = '';
		postRes.setEncoding('utf8');
		postRes.on('data', function(chunk) {
			msg += chunk;
		});
		postRes.on('end', function() {
			console.log(JSON.parse(msg));
		});
	});
	
	postReq.write(data);
	postReq.end();
});

app.listen(PORT);
console.log('Running on http://localhost:' + PORT);


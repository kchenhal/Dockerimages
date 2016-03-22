'use strict';

var url = require('url');

var Default = require('./DefaultService');


module.exports.sessionPOST = function sessionPOST (req, res, next) {
  Default.sessionPOST(req.swagger.params, res, next);
};

module.exports.sessionSessionIdDELETE = function sessionSessionIdDELETE (req, res, next) {
  Default.sessionSessionIdDELETE(req.swagger.params, res, next);
};

module.exports.sessionSessionIdGET = function sessionSessionIdGET (req, res, next) {
  Default.sessionSessionIdGET(req.swagger.params, res, next);
};

module.exports.sessionSessionIdKeyDELETE = function sessionSessionIdKeyDELETE (req, res, next) {
  Default.sessionSessionIdKeyDELETE(req.swagger.params, res, next);
};

module.exports.sessionSessionIdKeyGET = function sessionSessionIdKeyGET (req, res, next) {
  Default.sessionSessionIdKeyGET(req.swagger.params, res, next);
};

module.exports.sessionSessionIdKeyPUT = function sessionSessionIdKeyPUT (req, res, next) {
  Default.sessionSessionIdKeyPUT(req.swagger.params, res, next);
};

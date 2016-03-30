'use strict';

var url = require('url');


var Default = require('./DefaultService');


module.exports.healthCheckGET = function healthCheckGET (req, res, next) {
  Default.healthCheckGET(req.swagger.params, res, next);
};

module.exports.sessionPOST = function sessionPOST (req, res, next) {
  Default.sessionPOST(req.swagger.params, res, next);
};

module.exports.sessionSessionIdDELETE = function sessionSessionIdDELETE (req, res, next) {
  Default.sessionClientIdDELETE(req.swagger.params, res, next);
};

module.exports.sessionSessionIdGET = function sessionSessionIdGET (req, res, next) {
  Default.sessionClientIdGET(req.swagger.params, res, next);
};

module.exports.sessionSessionIdKeyDELETE = function sessionSessionIdKeyDELETE (req, res, next) {
  Default.sessionClientIdKeyDELETE(req.swagger.params, res, next);
};

module.exports.sessionSessionIdKeyGET = function sessionSessionIdKeyGET (req, res, next) {
  Default.sessionClientIdKeyGET(req.swagger.params, res, next);
};

module.exports.sessionSessionIdKeyPUT = function sessionSessionIdKeyPUT (req, res, next) {
  Default.sessionClientIdKeyPUT(req.swagger.params, res, next);
};

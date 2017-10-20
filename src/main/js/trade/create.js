'use strict';

const AWS = require('aws-sdk');
const db = new AWS.DynamoDB.DocumentClient();
const Trade = require('./Trade');

let trade = new Trade(db);

module.exports.create = (event, context, callback) => {
    trade.create(event, callback);
};
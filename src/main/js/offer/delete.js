'use strict';

const AWS = require('aws-sdk');
const db = new AWS.DynamoDB.DocumentClient();
const Offer = require('./Offer');

let offer = new Offer(db);

module.exports.delete = (event, context, callback) => {
    offer.delete(event, callback);
};
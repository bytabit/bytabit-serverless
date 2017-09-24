'use strict';

const AWS = require('aws-sdk');
const db = new AWS.DynamoDB.DocumentClient();
const Offers = require('./Offers');

let offers = new Offers(db);

module.exports.create = (event, context, callback) => {
    offers.create(event, callback);
};
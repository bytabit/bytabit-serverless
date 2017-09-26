'use strict';

const AWS = require('aws-sdk');
const db = new AWS.DynamoDB.DocumentClient();
const Profile = require('./Profile');

let profile = new Profile(db);

module.exports.update = (event, context, callback) => {
    profile.update(event, callback);
};
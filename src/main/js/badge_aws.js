/*
 * Copyright 2019 Bytabit AB
 */

"use strict";

const AWS = require("aws-sdk");
const db = new AWS.DynamoDB.DocumentClient();
const Badge = require("./Badge");

let badge = new Badge(db);

module.exports.put = (event, context, callback) => {
    badge.put(event, callback);
};

module.exports.getbypubkey = (event, context, callback) => {
    badge.getbypubkey(event, callback);
};
/*
 * Copyright 2018 Bytabit AB
 *
 */

"use strict";

const AWS = require("aws-sdk");
const db = new AWS.DynamoDB.DocumentClient();
const Trade = require("./Trade");

let trade = new Trade(db);

module.exports.list = (event, context, callback) => {
    trade.list(event, callback);
};
/*
 * Copyright 2018 Bytabit AB
 *
 */

"use strict";

const AWS = require("aws-sdk");
const db = new AWS.DynamoDB.DocumentClient();
const Trade = require("./Trade");

let trade = new Trade(db);

module.exports.put = (event, context, callback) => {
    trade.put(event, callback);
};

module.exports.getbyid = (event, context, callback) => {
    trade.getbyid(event, callback);
};

module.exports.getbyofferid = (event, context, callback) => {
    trade.getbyofferid(event, callback);
};
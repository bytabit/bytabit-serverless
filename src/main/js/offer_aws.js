/*
 * Copyright 2018 Bytabit AB
 *
 */

"use strict";

const AWS = require("aws-sdk");
const db = new AWS.DynamoDB.DocumentClient();
const Offer = require("./Offer");

let offer = new Offer(db);

module.exports.put = (event, context, callback) => {
    offer.put(event, callback);
};

module.exports.getall = (event, context, callback) => {
    offer.getall(event, callback);
};

module.exports.getbyid = (event, context, callback) => {
    offer.getbyid(event, callback);
};

module.exports.delete = (event, context, callback) => {
    offer.delete(event, callback);
};
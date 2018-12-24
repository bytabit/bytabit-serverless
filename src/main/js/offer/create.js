/*
 * Copyright 2018 Bytabit AB
 *
 */

"use strict";

const AWS = require("aws-sdk");
const db = new AWS.DynamoDB.DocumentClient();
const Offer = require("./Offer");

let offer = new Offer(db);

module.exports.create = (event, context, callback) => {
    offer.create(event, callback);
};
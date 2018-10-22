"use strict";

const AWS = require("aws-sdk");

const Client = require("bitcoin-core");
const client = new Client({network: 'regtest',
                           username: 'admin1',
                           password: '123'});

const BitcoinCoreApi = require("./BitcoinCoreApi.js");
let bitcoincoreapi = new BitcoinCoreApi(client);

module.exports.getrawtransaction = (event, context, callback) => {
    //console.log('getrawtransaction');
    bitcoincoreapi.getrawtransaction(event, callback);
};
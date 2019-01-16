/*
 * Copyright 2018 Bytabit AB
 *
 */

"use strict";

class Trade {

    constructor(db) {
        this.db = db;
    }

    put(event, callback) {
        const timestamp = new Date().getTime();

        try {
            const tradeServiceResource = JSON.parse(event.body);
            const id = event.pathParameters.id;

            const params = {
                TableName: process.env.TRADE_TABLE,
                Item: {
                    // search keys
                    id: id,
                    version: timestamp,
                    offerId: tradeServiceResource.offerId,

                    // TODO this will be block of encrypted trade data
                    trade: tradeServiceResource.trade
                }
            };

            // write the offer to the database
            this.db.put(params, (error) => {
                // handle potential errors
                if (error) {
                    console.error(error);
                    callback(null, {
                        statusCode: error.statusCode || 501,
                        headers: {"Content-Type": "text/plain"},
                        body: "Couldn't put the trade.",
                    });
                    return;
                }

                // put a response
                const response = {
                    statusCode: 200,
                    body: JSON.stringify(params.Item)
                };
                callback(null, response);
            });
        } catch (err) {
            console.error("Validation Failed: " + err.name);
            callback(null, {
                statusCode: 400,
                headers: {"Content-Type": "text/plain"},
                body: "Couldn't parse the trade.",
            });
        }
    }

    getbyid(event, callback) {

        const versionParam = event["queryStringParameters"]["version"];
        const version = (versionParam === undefined) ? 0 : parseInt(versionParam);

        const params = {
            TableName: process.env.TRADE_TABLE,
            ExpressionAttributeValues: {
                ":id": event.pathParameters.id,
                ":version": version
            },
            KeyConditionExpression: "id = :id AND version > :version",
        };

        // retrieve the trades from the database
        this.db.scan(params, (error, result) => {
            // handle potential errors
            if (error) {
                console.error(error);
                callback(null, {
                    statusCode: error.statusCode || 501,
                    headers: {"Content-Type": "text/plain"},
                    body: "Couldn't get the trade.",
                });
                return;
            }

            // create a response
            const response = {
                statusCode: 200,
                body: JSON.stringify(result.Items),
            };
            callback(null, response);
        });
    }

    getbyofferid(event, callback) {

        const versionParam = event["queryStringParameters"]["version"];
        const version = (versionParam === undefined) ? 0 : parseInt(versionParam);

        const params = {
            TableName: process.env.TRADE_TABLE,
            IndexName: "offerIndex",
            ExpressionAttributeValues: {
                ":offerId": event.pathParameters.id,
                ":version": version
            },
            KeyConditionExpression: "offerId = :offerId",
            FilterExpression: "version > :version"
        };

        // retrieve the trades from the database
        this.db.query(params, (error, result) => {
            // handle potential errors
            if (error) {
                console.error(error);
                callback(null, {
                    statusCode: error.statusCode || 501,
                    headers: {"Content-Type": "text/plain"},
                    body: "Couldn't get the trades.",
                });
                return;
            }

            // create a response
            const response = {
                statusCode: 200,
                body: JSON.stringify(result.Items),
            };
            callback(null, response);
        });
    }
}

module.exports = Trade;